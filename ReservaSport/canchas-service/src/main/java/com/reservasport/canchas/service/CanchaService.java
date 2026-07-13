package com.reservasport.canchas.service;

import com.reservasport.canchas.dto.CanchaRequest;
import com.reservasport.canchas.dto.CanchaResponse;

import java.util.List;

public interface CanchaService {

    CanchaResponse crearCancha(CanchaRequest request);

    List<CanchaResponse> obtenerCanchas();

    CanchaResponse obtenerCanchaPorId(Long id);

    CanchaResponse actualizarCancha(Long id, CanchaRequest request);

    void eliminarCancha(Long id);
}
