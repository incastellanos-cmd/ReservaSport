package com.reservasport.complejos.service;

import com.reservasport.complejos.dto.ComplejoRequest;
import com.reservasport.complejos.dto.ComplejoResponse;
import com.reservasport.complejos.exception.ComplejoNoEncontradoException;
import com.reservasport.complejos.exception.NombreComplejoDuplicadoException;
import com.reservasport.complejos.model.Complejo;
import com.reservasport.complejos.repository.ComplejoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComplejoServiceImpl implements ComplejoService {

    private final ComplejoRepository complejoRepository;

    @Override
    public ComplejoResponse crearComplejo(ComplejoRequest request) {

        log.info("Creando complejo con nombre: {}", request.getNombre());

        if (complejoRepository.existsByNombreIgnoreCase(request.getNombre())) {
            log.warn("Nombre de complejo duplicado: {}", request.getNombre());

            throw new NombreComplejoDuplicadoException(
                    "Ya existe un complejo con ese nombre"
            );
        }

        Complejo complejo = Complejo.builder()
                .nombre(request.getNombre())
                .direccion(request.getDireccion())
                .ciudad(request.getCiudad())
                .telefono(request.getTelefono())
                .activo(true)
                .build();

        Complejo complejoGuardado = complejoRepository.save(complejo);

        log.info("Complejo creado correctamente con ID: {}", complejoGuardado.getId());

        return convertirAResponse(complejoGuardado);
    }

    @Override
    public List<ComplejoResponse> obtenerComplejos() {

        log.info("Consultando lista de complejos");

        return complejoRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Override
    public ComplejoResponse obtenerComplejoPorId(Long id) {

        log.info("Consultando complejo con ID: {}", id);

        return convertirAResponse(buscarComplejoPorId(id));
    }

    @Override
    public ComplejoResponse actualizarComplejo(
            Long id,
            ComplejoRequest request) {

        log.info("Actualizando complejo con ID: {}", id);

        Complejo complejo = buscarComplejoPorId(id);

        boolean nombreModificado =
                !complejo.getNombre().equalsIgnoreCase(request.getNombre());

        if (nombreModificado
                && complejoRepository.existsByNombreIgnoreCase(request.getNombre())) {

            log.warn(
                    "No se pudo actualizar el complejo con ID {}. Nombre duplicado: {}",
                    id,
                    request.getNombre()
            );

            throw new NombreComplejoDuplicadoException(
                    "Ya existe un complejo con ese nombre"
            );
        }

        complejo.setNombre(request.getNombre());
        complejo.setDireccion(request.getDireccion());
        complejo.setCiudad(request.getCiudad());
        complejo.setTelefono(request.getTelefono());

        Complejo complejoActualizado = complejoRepository.save(complejo);

        log.info("Complejo actualizado correctamente con ID: {}", id);

        return convertirAResponse(complejoActualizado);
    }

    @Override
    public void eliminarComplejo(Long id) {

        log.info("Eliminando complejo con ID: {}", id);

        Complejo complejo = buscarComplejoPorId(id);

        complejoRepository.delete(complejo);

        log.info("Complejo eliminado correctamente con ID: {}", id);
    }

    private Complejo buscarComplejoPorId(Long id) {

        return complejoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Complejo no encontrado con ID: {}", id);

                    return new ComplejoNoEncontradoException(
                            "Complejo no encontrado"
                    );
                });
    }

    private ComplejoResponse convertirAResponse(Complejo complejo) {

        return ComplejoResponse.builder()
                .id(complejo.getId())
                .nombre(complejo.getNombre())
                .direccion(complejo.getDireccion())
                .ciudad(complejo.getCiudad())
                .telefono(complejo.getTelefono())
                .activo(complejo.getActivo())
                .fechaCreacion(complejo.getFechaCreacion())
                .build();
    }
}
