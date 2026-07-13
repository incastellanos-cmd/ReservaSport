package com.reservasport.reservas.client;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CanchaClientResponse {

    private Long id;
    private String nombre;
    private String tipo;
    private BigDecimal precioHora;
    private Boolean disponible;
}
