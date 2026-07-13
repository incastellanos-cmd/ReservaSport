package com.reservasport.promociones.service;

import com.reservasport.promociones.dto.PromocionRequest;
import com.reservasport.promociones.dto.PromocionResponse;
import com.reservasport.promociones.exception.PromocionNoEncontradaException;
import com.reservasport.promociones.model.EstadoPromocion;
import com.reservasport.promociones.model.Promocion;
import com.reservasport.promociones.repository.PromocionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromocionServiceImpl implements PromocionService {

    private final PromocionRepository promocionRepository;

    @Override
    public PromocionResponse crearPromocion(PromocionRequest request) {

        Promocion promocion = Promocion.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .tipo(request.getTipo())
                .valor(request.getValor())
                .fechaInicio(request.getFechaInicio())
                .fechaFin(request.getFechaFin())
                .estado(EstadoPromocion.ACTIVA)
                .build();

        Promocion guardada = promocionRepository.save(promocion);

        log.info("Promoción creada con id {}", guardada.getId());

        return convertirAResponse(guardada);
    }

    @Override
    public List<PromocionResponse> listarPromociones() {

        return promocionRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Override
    public PromocionResponse obtenerPromocionPorId(Long id) {

        return convertirAResponse(buscarPromocion(id));
    }

    @Override
    public List<PromocionResponse> listarPorEstado(EstadoPromocion estado) {

        return promocionRepository.findByEstado(estado)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Override
    public PromocionResponse actualizarEstado(Long id, EstadoPromocion estado) {

        Promocion promocion = buscarPromocion(id);

        promocion.setEstado(estado);

        Promocion actualizada = promocionRepository.save(promocion);

        log.info("Estado actualizado para promoción {}", id);

        return convertirAResponse(actualizada);
    }

    @Override
    public void eliminarPromocion(Long id) {

        Promocion promocion = buscarPromocion(id);

        promocionRepository.delete(promocion);

        log.info("Promoción eliminada {}", id);
    }

    private Promocion buscarPromocion(Long id) {

        return promocionRepository.findById(id)
                .orElseThrow(() ->
                        new PromocionNoEncontradaException(id));
    }

    private PromocionResponse convertirAResponse(Promocion promocion) {

        return PromocionResponse.builder()
                .id(promocion.getId())
                .nombre(promocion.getNombre())
                .descripcion(promocion.getDescripcion())
                .tipo(promocion.getTipo())
                .valor(promocion.getValor())
                .estado(promocion.getEstado())
                .fechaInicio(promocion.getFechaInicio())
                .fechaFin(promocion.getFechaFin())
                .build();
    }
}
