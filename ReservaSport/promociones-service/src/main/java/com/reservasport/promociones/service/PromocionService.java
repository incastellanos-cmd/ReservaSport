package com.reservasport.promociones.service;

import com.reservasport.promociones.dto.PromocionRequest;
import com.reservasport.promociones.dto.PromocionResponse;
import com.reservasport.promociones.model.EstadoPromocion;

import java.util.List;

public interface PromocionService {

    PromocionResponse crearPromocion(PromocionRequest request);

    List<PromocionResponse> listarPromociones();

    PromocionResponse obtenerPromocionPorId(Long id);

    List<PromocionResponse> listarPorEstado(EstadoPromocion estado);

    PromocionResponse actualizarEstado(Long id, EstadoPromocion estado);

    void eliminarPromocion(Long id);

}
