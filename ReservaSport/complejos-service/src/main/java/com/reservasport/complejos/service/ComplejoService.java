package com.reservasport.complejos.service;

import com.reservasport.complejos.dto.ComplejoRequest;
import com.reservasport.complejos.dto.ComplejoResponse;

import java.util.List;

public interface ComplejoService {

    ComplejoResponse crearComplejo(ComplejoRequest request);

    List<ComplejoResponse> obtenerComplejos();

    ComplejoResponse obtenerComplejoPorId(Long id);

    ComplejoResponse actualizarComplejo(Long id, ComplejoRequest request);

    void eliminarComplejo(Long id);

}
