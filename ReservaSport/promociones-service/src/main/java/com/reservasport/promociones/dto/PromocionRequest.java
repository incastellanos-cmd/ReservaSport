package com.reservasport.promociones.dto;

import com.reservasport.promociones.model.TipoPromocion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromocionRequest {

    @NotBlank
    private String nombre;

    private String descripcion;

    @NotNull
    private TipoPromocion tipo;

    @NotNull
    private BigDecimal valor;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;
}
