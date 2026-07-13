package com.reservasport.promociones.exception;

public class PromocionNoEncontradaException extends RuntimeException {
    public PromocionNoEncontradaException(Long id) {
        super("No se encontró la promoción con id: " + id);

    }
}
