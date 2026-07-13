package com.reservasport.pagos.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaResponse {

    private Long id;
    private Long usuarioId;
    private Long canchaId;
    private Long horarioId;
    private BigDecimal montoTotal;
    private String estado;
}
