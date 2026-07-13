package com.reservasport.reportes.service;

import com.reservasport.reportes.dto.ReporteRequest;
import com.reservasport.reportes.dto.ReporteResponse;
import com.reservasport.reportes.model.EstadoReporte;

import java.util.List;
public interface ReporteService {

    ReporteResponse crearReporte(ReporteRequest request);

    List<ReporteResponse> listarReportes();

    ReporteResponse obtenerPorId(Long id);

    List<ReporteResponse> listarPorEstado(EstadoReporte estado);

    ReporteResponse actualizarEstado(Long id, EstadoReporte estado);

    void eliminarReporte(Long id);
}
