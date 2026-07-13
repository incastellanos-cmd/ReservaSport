package com.reservasport.canchas.service;

import com.reservasport.canchas.dto.CanchaRequest;
import com.reservasport.canchas.dto.CanchaResponse;
import com.reservasport.canchas.exception.CanchaNoEncontradaException;
import com.reservasport.canchas.exception.NombreCanchaDuplicadoException;
import com.reservasport.canchas.model.Cancha;
import com.reservasport.canchas.repository.CanchaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CanchaServiceImpl implements CanchaService {

    private final CanchaRepository canchaRepository;

    @Override
    public CanchaResponse crearCancha(CanchaRequest request) {

        log.info("Creando cancha con nombre: {}", request.getNombre());

        if (canchaRepository.existsByNombreIgnoreCase(request.getNombre())) {
            log.warn("No se pudo crear la cancha. Nombre duplicado: {}", request.getNombre());

            throw new NombreCanchaDuplicadoException("Ya existe una cancha con ese nombre");
        }

        Cancha cancha = Cancha.builder()
                .nombre(request.getNombre())
                .tipo(request.getTipo())
                .precioHora(request.getPrecioHora())
                .disponible(true)
                .build();

        Cancha canchaGuardada = canchaRepository.save(cancha);

        log.info("Cancha creada correctamente con ID: {}", canchaGuardada.getId());

        return convertirAResponse(canchaGuardada);
    }

    @Override
    public List<CanchaResponse> obtenerCanchas() {

        log.info("Consultando lista de canchas");

        return canchaRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Override
    public CanchaResponse obtenerCanchaPorId(Long id) {

        log.info("Consultando cancha con ID: {}", id);

        Cancha cancha = buscarCanchaPorId(id);

        return convertirAResponse(cancha);
    }

    @Override
    public CanchaResponse actualizarCancha(Long id, CanchaRequest request) {

        log.info("Actualizando cancha con ID: {}", id);

        Cancha cancha = buscarCanchaPorId(id);

        boolean nombreModificado =
                !cancha.getNombre().equalsIgnoreCase(request.getNombre());

        if (nombreModificado
                && canchaRepository.existsByNombreIgnoreCase(request.getNombre())) {

            log.warn(
                    "No se pudo actualizar la cancha con ID {}. Nombre duplicado: {}",
                    id,
                    request.getNombre()
            );

            throw new NombreCanchaDuplicadoException("Ya existe una cancha con ese nombre");
        }

        cancha.setNombre(request.getNombre());
        cancha.setTipo(request.getTipo());
        cancha.setPrecioHora(request.getPrecioHora());

        Cancha canchaActualizada = canchaRepository.save(cancha);

        log.info("Cancha actualizada correctamente con ID: {}", id);

        return convertirAResponse(canchaActualizada);
    }

    @Override
    public void eliminarCancha(Long id) {

        log.info("Eliminando cancha con ID: {}", id);

        Cancha cancha = buscarCanchaPorId(id);

        canchaRepository.delete(cancha);

        log.info("Cancha eliminada correctamente con ID: {}", id);
    }

    private Cancha buscarCanchaPorId(Long id) {

        return canchaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cancha no encontrada con ID: {}", id);

                    return new CanchaNoEncontradaException("Cancha no encontrada");
                });
    }

    private CanchaResponse convertirAResponse(Cancha cancha) {

        return CanchaResponse.builder()
                .id(cancha.getId())
                .nombre(cancha.getNombre())
                .tipo(cancha.getTipo())
                .precioHora(cancha.getPrecioHora())
                .disponible(cancha.getDisponible())
                .fechaCreacion(cancha.getFechaCreacion())
                .build();
    }


}
