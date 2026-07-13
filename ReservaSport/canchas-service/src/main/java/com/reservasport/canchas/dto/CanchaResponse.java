package com.reservasport.canchas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CanchaResponse {

    private Long id;
    private String nombre;
    private String tipo;
    private BigDecimal precioHora;
    private Boolean disponible;
    private LocalDateTime fechaCreacion;
}
