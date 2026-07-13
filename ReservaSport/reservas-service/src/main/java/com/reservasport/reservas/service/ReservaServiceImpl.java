package com.reservasport.reservas.service;

import com.reservasport.reservas.client.CanchaClient;
import com.reservasport.reservas.client.CanchaClientResponse;
import com.reservasport.reservas.client.HorarioClient;
import com.reservasport.reservas.client.HorarioClientResponse;
import com.reservasport.reservas.client.UsuarioClient;
import com.reservasport.reservas.client.UsuarioClientResponse;
import com.reservasport.reservas.dto.ReservaRequest;
import com.reservasport.reservas.dto.ReservaResponse;
import com.reservasport.reservas.exception.HorarioNoDisponibleException;
import com.reservasport.reservas.exception.RecursoRemotoInvalidoException;
import com.reservasport.reservas.exception.ReservaNoEncontradaException;
import com.reservasport.reservas.model.EstadoReserva;
import com.reservasport.reservas.model.Reserva;
import com.reservasport.reservas.repository.ReservaRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioClient usuarioClient;
    private final CanchaClient canchaClient;
    private final HorarioClient horarioClient;

    @Override
    @Transactional
    public ReservaResponse crearReserva(ReservaRequest request) {

        log.info(
                "Creando reserva para usuario {}, cancha {} y horario {}",
                request.getUsuarioId(),
                request.getCanchaId(),
                request.getHorarioId()
        );

        UsuarioClientResponse usuario =
                obtenerUsuarioRemoto(request.getUsuarioId());

        validarUsuario(usuario);

        CanchaClientResponse cancha =
                obtenerCanchaRemota(request.getCanchaId());

        validarCancha(cancha);

        HorarioClientResponse horario =
                obtenerHorarioRemoto(request.getHorarioId());

        validarHorario(horario, request.getCanchaId());

        boolean horarioReservado =
                reservaRepository.existsByHorarioIdAndEstadoNot(
                        request.getHorarioId(),
                        EstadoReserva.CANCELADA
                );

        if (horarioReservado) {
            log.warn(
                    "El horario {} ya tiene una reserva activa",
                    request.getHorarioId()
            );

            throw new HorarioNoDisponibleException(
                    "El horario ya se encuentra reservado"
            );
        }

        Reserva reserva = Reserva.builder()
                .usuarioId(request.getUsuarioId())
                .canchaId(request.getCanchaId())
                .horarioId(request.getHorarioId())
                .valorReserva(cancha.getPrecioHora())
                .estado(EstadoReserva.PENDIENTE)
                .build();

        Reserva reservaGuardada = reservaRepository.save(reserva);

        actualizarDisponibilidadHorario(
                request.getHorarioId(),
                false
        );

        log.info(
                "Reserva creada correctamente con ID: {}",
                reservaGuardada.getId()
        );

        return convertirAResponse(reservaGuardada);
    }

    @Override
    public List<ReservaResponse> obtenerReservas() {

        log.info("Consultando lista de reservas");

        return reservaRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Override
    public ReservaResponse obtenerReservaPorId(Long id) {

        log.info("Consultando reserva con ID: {}", id);

        return convertirAResponse(buscarReservaPorId(id));
    }

    @Override
    @Transactional
    public ReservaResponse confirmarReserva(Long id) {

        log.info("Confirmando reserva con ID: {}", id);

        Reserva reserva = buscarReservaPorId(id);

        if (reserva.getEstado() == EstadoReserva.CANCELADA) {
            throw new RecursoRemotoInvalidoException(
                    "No se puede confirmar una reserva cancelada"
            );
        }

        if (reserva.getEstado() != EstadoReserva.CONFIRMADA) {
            reserva.setEstado(EstadoReserva.CONFIRMADA);
            reserva = reservaRepository.save(reserva);
        }

        log.info("Reserva confirmada correctamente con ID: {}", id);

        return convertirAResponse(reserva);
    }

    @Override
    @Transactional
    public void cancelarReserva(Long id) {

        log.info("Cancelando reserva con ID: {}", id);

        Reserva reserva = buscarReservaPorId(id);

        if (reserva.getEstado() == EstadoReserva.CANCELADA) {
            log.info("La reserva {} ya estaba cancelada", id);
            return;
        }

        reserva.setEstado(EstadoReserva.CANCELADA);
        reservaRepository.save(reserva);

        actualizarDisponibilidadHorario(
                reserva.getHorarioId(),
                true
        );

        log.info("Reserva cancelada correctamente con ID: {}", id);
    }

    private Reserva buscarReservaPorId(Long id) {

        return reservaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Reserva no encontrada con ID: {}", id);

                    return new ReservaNoEncontradaException(
                            "Reserva no encontrada"
                    );
                });
    }

    private UsuarioClientResponse obtenerUsuarioRemoto(Long usuarioId) {

        try {
            return usuarioClient.obtenerUsuarioPorId(usuarioId);
        } catch (FeignException ex) {
            log.warn(
                    "No fue posible consultar el usuario con ID: {}",
                    usuarioId
            );

            throw new RecursoRemotoInvalidoException(
                    "El usuario indicado no existe o no está disponible"
            );
        }
    }

    private CanchaClientResponse obtenerCanchaRemota(Long canchaId) {

        try {
            return canchaClient.obtenerCanchaPorId(canchaId);
        } catch (FeignException ex) {
            log.warn(
                    "No fue posible consultar la cancha con ID: {}",
                    canchaId
            );

            throw new RecursoRemotoInvalidoException(
                    "La cancha indicada no existe o no está disponible"
            );
        }
    }

    private HorarioClientResponse obtenerHorarioRemoto(Long horarioId) {

        try {
            return horarioClient.obtenerHorarioPorId(horarioId);
        } catch (FeignException ex) {
            log.warn(
                    "No fue posible consultar el horario con ID: {}",
                    horarioId
            );

            throw new RecursoRemotoInvalidoException(
                    "El horario indicado no existe o no está disponible"
            );
        }
    }

    private void actualizarDisponibilidadHorario(
            Long horarioId,
            Boolean disponible) {

        try {
            horarioClient.actualizarDisponibilidad(
                    horarioId,
                    disponible
            );
        } catch (FeignException ex) {
            log.error(
                    "No fue posible actualizar la disponibilidad del horario {}",
                    horarioId
            );

            throw new RecursoRemotoInvalidoException(
                    "No fue posible actualizar la disponibilidad del horario"
            );
        }
    }

    private void validarUsuario(UsuarioClientResponse usuario) {

        if (usuario == null
                || usuario.getId() == null
                || !Boolean.TRUE.equals(usuario.getActivo())) {

            throw new RecursoRemotoInvalidoException(
                    "El usuario no existe o se encuentra inactivo"
            );
        }
    }

    private void validarCancha(CanchaClientResponse cancha) {

        if (cancha == null || cancha.getId() == null) {
            throw new RecursoRemotoInvalidoException(
                    "La cancha indicada no existe"
            );
        }

        if (!Boolean.TRUE.equals(cancha.getDisponible())) {
            throw new RecursoRemotoInvalidoException(
                    "La cancha no se encuentra disponible"
            );
        }

        if (cancha.getPrecioHora() == null) {
            throw new RecursoRemotoInvalidoException(
                    "La cancha no tiene un precio por hora configurado"
            );
        }
    }

    private void validarHorario(
            HorarioClientResponse horario,
            Long canchaId) {

        if (horario == null || horario.getId() == null) {
            throw new RecursoRemotoInvalidoException(
                    "El horario indicado no existe"
            );
        }

        if (!Boolean.TRUE.equals(horario.getDisponible())) {
            throw new HorarioNoDisponibleException(
                    "El horario no se encuentra disponible"
            );
        }

        if (!canchaId.equals(horario.getCanchaId())) {
            throw new RecursoRemotoInvalidoException(
                    "El horario no pertenece a la cancha seleccionada"
            );
        }
    }

    private ReservaResponse convertirAResponse(Reserva reserva) {

        return ReservaResponse.builder()
                .id(reserva.getId())
                .usuarioId(reserva.getUsuarioId())
                .canchaId(reserva.getCanchaId())
                .horarioId(reserva.getHorarioId())
                .valorReserva(reserva.getValorReserva())
                .estado(reserva.getEstado())
                .fechaCreacion(reserva.getFechaCreacion())
                .build();
    }
}
