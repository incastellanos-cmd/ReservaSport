package com.reservasport.pagos.service;

import com.reservasport.pagos.dto.PagoRequest;
import com.reservasport.pagos.dto.PagoResponse;
import com.reservasport.pagos.model.EstadoPago;

import java.util.List;

public interface PagoService {


    PagoResponse crearPago(PagoRequest request);

    List<PagoResponse> listarPagos();

    PagoResponse obtenerPagoPorId(Long id);

    List<PagoResponse> listarPorReserva(Long reservaId);

    PagoResponse actualizarEstado(
            Long id,
            EstadoPago estado
    );

    void eliminarPago(Long id);
}
