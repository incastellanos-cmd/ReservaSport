package com.reservasport.horarios.service;

import com.reservasport.horarios.dto.HorarioRequest;
import com.reservasport.horarios.dto.HorarioResponse;
import com.reservasport.horarios.exception.HorarioDuplicadoException;
import com.reservasport.horarios.exception.HorarioNoEncontradoException;
import com.reservasport.horarios.exception.RangoHorarioInvalidoException;
import com.reservasport.horarios.model.Horario;
import com.reservasport.horarios.repository.HorarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HorarioServiceImpl implements HorarioService{

    private final HorarioRepository horarioRepository;

    @Override
    public HorarioResponse crearHorario(HorarioRequest request) {

        validarRangoHorario(request);

        log.info(
                "Creando horario para cancha {} en fecha {}",
                request.getCanchaId(),
                request.getFecha()
        );

        if (existeHorario(request)) {
            log.warn(
                    "Horario duplicado para cancha {} en fecha {}",
                    request.getCanchaId(),
                    request.getFecha()
            );

            throw new HorarioDuplicadoException(
                    "Ya existe un horario con esos datos"
            );
        }

        Horario horario = Horario.builder()
                .canchaId(request.getCanchaId())
                .fecha(request.getFecha())
                .horaInicio(request.getHoraInicio())
                .horaFin(request.getHoraFin())
                .disponible(true)
                .build();

        Horario horarioGuardado = horarioRepository.save(horario);

        log.info("Horario creado con ID: {}", horarioGuardado.getId());

        return convertirAResponse(horarioGuardado);
    }

    @Override
    public List<HorarioResponse> obtenerHorarios() {

        log.info("Consultando lista de horarios");

        return horarioRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Override
    public HorarioResponse obtenerHorarioPorId(Long id) {

        log.info("Consultando horario con ID: {}", id);

        return convertirAResponse(buscarHorarioPorId(id));
    }

    @Override
    public HorarioResponse actualizarHorario(
            Long id,
            HorarioRequest request) {

        validarRangoHorario(request);

        log.info("Actualizando horario con ID: {}", id);

        Horario horario = buscarHorarioPorId(id);

        boolean datosModificados =
                !horario.getCanchaId().equals(request.getCanchaId())
                        || !horario.getFecha().equals(request.getFecha())
                        || !horario.getHoraInicio().equals(request.getHoraInicio())
                        || !horario.getHoraFin().equals(request.getHoraFin());

        if (datosModificados && existeHorario(request)) {
            log.warn(
                    "No se pudo actualizar el horario con ID {} por duplicidad",
                    id
            );

            throw new HorarioDuplicadoException(
                    "Ya existe un horario con esos datos"
            );
        }

        horario.setCanchaId(request.getCanchaId());
        horario.setFecha(request.getFecha());
        horario.setHoraInicio(request.getHoraInicio());
        horario.setHoraFin(request.getHoraFin());

        Horario horarioActualizado = horarioRepository.save(horario);

        log.info("Horario actualizado con ID: {}", id);

        return convertirAResponse(horarioActualizado);
    }

    @Override
    public void eliminarHorario(Long id) {

        log.info("Eliminando horario con ID: {}", id);

        Horario horario = buscarHorarioPorId(id);

        horarioRepository.delete(horario);

        log.info("Horario eliminado con ID: {}", id);
    }

    private Horario buscarHorarioPorId(Long id) {

        return horarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Horario no encontrado con ID: {}", id);

                    return new HorarioNoEncontradoException(
                            "Horario no encontrado"
                    );
                });
    }

    private boolean existeHorario(HorarioRequest request) {

        return horarioRepository
                .existsByCanchaIdAndFechaAndHoraInicioAndHoraFin(
                        request.getCanchaId(),
                        request.getFecha(),
                        request.getHoraInicio(),
                        request.getHoraFin()
                );
    }

    private void validarRangoHorario(HorarioRequest request) {

        if (!request.getHoraFin().isAfter(request.getHoraInicio())) {
            throw new RangoHorarioInvalidoException(
                    "La hora de fin debe ser posterior a la hora de inicio"
            );
        }
    }

    private HorarioResponse convertirAResponse(Horario horario) {

        return HorarioResponse.builder()
                .id(horario.getId())
                .canchaId(horario.getCanchaId())
                .fecha(horario.getFecha())
                .horaInicio(horario.getHoraInicio())
                .horaFin(horario.getHoraFin())
                .disponible(horario.getDisponible())
                .fechaCreacion(horario.getFechaCreacion())
                .build();
    }

    @Override
    public HorarioResponse actualizarDisponibilidad(
            Long id,
            Boolean disponible) {

        log.info(
                "Actualizando disponibilidad del horario {} a {}",
                id,
                disponible
        );

        Horario horario = buscarHorarioPorId(id);

        horario.setDisponible(disponible);

        Horario horarioActualizado = horarioRepository.save(horario);

        log.info(
                "Disponibilidad del horario {} actualizada correctamente",
                id
        );

        return convertirAResponse(horarioActualizado);
    }
}
