package com.reservasport.reportes.dto;

import com.reservasport.reportes.model.EstadoReporte;
import com.reservasport.reportes.model.TipoReporte;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteResponse {

    private Long id;
    private String nombre;
    private TipoReporte tipo;
    private EstadoReporte estado;
    private String descripcion;
    private LocalDateTime fechaGeneracion;

}
