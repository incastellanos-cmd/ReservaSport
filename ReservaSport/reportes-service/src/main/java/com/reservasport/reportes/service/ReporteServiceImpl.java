package com.reservasport.reportes.service;

import com.reservasport.reportes.dto.ReporteRequest;
import com.reservasport.reportes.dto.ReporteResponse;
import com.reservasport.reportes.exception.ReporteNoEncontradoException;
import com.reservasport.reportes.model.EstadoReporte;
import com.reservasport.reportes.model.Reporte;
import com.reservasport.reportes.repository.ReporteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReporteServiceImpl  implements ReporteService {

    private final ReporteRepository repository;

    @Override
    public ReporteResponse crearReporte(ReporteRequest request) {

        Reporte reporte = Reporte.builder()
                .nombre(request.getNombre())
                .tipo(request.getTipo())
                .descripcion(request.getDescripcion())
                .estado(EstadoReporte.PENDIENTE)
                .build();

        return convertir(repository.save(reporte));
    }

    @Override
    public List<ReporteResponse> listarReportes() {
        return repository.findAll().stream().map(this::convertir).toList();
    }

    @Override
    public ReporteResponse obtenerPorId(Long id) {
        return convertir(buscar(id));
    }

    @Override
    public List<ReporteResponse> listarPorEstado(EstadoReporte estado) {
        return repository.findByEstado(estado).stream().map(this::convertir).toList();
    }

    @Override
    public ReporteResponse actualizarEstado(Long id, EstadoReporte estado) {

        Reporte reporte = buscar(id);

        reporte.setEstado(estado);

        return convertir(repository.save(reporte));
    }

    @Override
    public void eliminarReporte(Long id) {
        repository.delete(buscar(id));
    }

    private Reporte buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ReporteNoEncontradoException(id));
    }

    private ReporteResponse convertir(Reporte reporte) {
        return ReporteResponse.builder()
                .id(reporte.getId())
                .nombre(reporte.getNombre())
                .tipo(reporte.getTipo())
                .estado(reporte.getEstado())
                .descripcion(reporte.getDescripcion())
                .fechaGeneracion(reporte.getFechaGeneracion())
                .build();
    }
}
