package com.reservasport.reportes.dto;

import com.reservasport.reportes.model.TipoReporte;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteRequest {

    @NotBlank
    private String nombre;

    @NotNull
    private TipoReporte tipo;

    private String descripcion;
}
