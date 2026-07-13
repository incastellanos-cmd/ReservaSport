package com.reservasport.notificaciones.service;

import com.reservasport.notificaciones.dto.NotificacionRequest;
import com.reservasport.notificaciones.dto.NotificacionResponse;
import com.reservasport.notificaciones.exception.NotificacionNoEncontradaException;
import com.reservasport.notificaciones.model.EstadoNotificacion;
import com.reservasport.notificaciones.model.Notificacion;
import com.reservasport.notificaciones.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionRepository notificacionRepository;

    @Override
    public NotificacionResponse crearNotificacion(
            NotificacionRequest request
    ) {
        Notificacion notificacion = Notificacion.builder()
                .usuarioId(request.getUsuarioId())
                .reservaId(request.getReservaId())
                .tipo(request.getTipo())
                .asunto(request.getAsunto())
                .mensaje(request.getMensaje())
                .estado(EstadoNotificacion.PENDIENTE)
                .build();

        Notificacion guardada =
                notificacionRepository.save(notificacion);

        log.info(
                "Notificación creada con id {}",
                guardada.getId()
        );

        return convertirAResponse(guardada);
    }

    @Override
    public List<NotificacionResponse> listarNotificaciones() {
        return notificacionRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Override
    public NotificacionResponse obtenerPorId(Long id) {
        return convertirAResponse(buscarPorId(id));
    }

    @Override
    public List<NotificacionResponse> listarPorUsuario(
            Long usuarioId
    ) {
        return notificacionRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Override
    public List<NotificacionResponse> listarPorReserva(
            Long reservaId
    ) {
        return notificacionRepository.findByReservaId(reservaId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Override
    public NotificacionResponse actualizarEstado(
            Long id,
            EstadoNotificacion estado
    ) {
        Notificacion notificacion = buscarPorId(id);

        notificacion.setEstado(estado);

        if (estado == EstadoNotificacion.ENVIADA) {
            notificacion.setFechaEnvio(LocalDateTime.now());
        }

        Notificacion actualizada =
                notificacionRepository.save(notificacion);

        log.info(
                "Estado de la notificación {} actualizado a {}",
                id,
                estado
        );

        return convertirAResponse(actualizada);
    }

    @Override
    public void eliminarNotificacion(Long id) {
        Notificacion notificacion = buscarPorId(id);
        notificacionRepository.delete(notificacion);

        log.info("Notificación {} eliminada", id);
    }

    private Notificacion buscarPorId(Long id) {
        return notificacionRepository.findById(id)
                .orElseThrow(() ->
                        new NotificacionNoEncontradaException(id)
                );
    }

    private NotificacionResponse convertirAResponse(
            Notificacion notificacion
    ) {
        return NotificacionResponse.builder()
                .id(notificacion.getId())
                .usuarioId(notificacion.getUsuarioId())
                .reservaId(notificacion.getReservaId())
                .tipo(notificacion.getTipo())
                .asunto(notificacion.getAsunto())
                .mensaje(notificacion.getMensaje())
                .estado(notificacion.getEstado())
                .fechaCreacion(notificacion.getFechaCreacion())
                .fechaEnvio(notificacion.getFechaEnvio())
                .build();
    }
}
