package com.reservasport.promociones.dto;

import com.reservasport.promociones.model.EstadoPromocion;
import com.reservasport.promociones.model.TipoPromocion;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromocionResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private TipoPromocion tipo;
    private BigDecimal valor;
    private EstadoPromocion estado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
