package com.reservasport.reportes.repository;

import com.reservasport.reportes.model.EstadoReporte;
import com.reservasport.reportes.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface ReporteRepository extends JpaRepository<Reporte, Long> {

    List<Reporte> findByEstado(EstadoReporte estado);
}
