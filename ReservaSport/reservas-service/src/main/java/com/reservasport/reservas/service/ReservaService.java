package com.reservasport.reservas.service;

import com.reservasport.reservas.dto.ReservaRequest;
import com.reservasport.reservas.dto.ReservaResponse;

import java.util.List;

public interface ReservaService {

    ReservaResponse crearReserva(ReservaRequest request);

    List<ReservaResponse> obtenerReservas();

    ReservaResponse obtenerReservaPorId(Long id);

    ReservaResponse confirmarReserva(Long id);

    void cancelarReserva(Long id);
}
