package com.reservasport.resenas.exception;

public class ResenaNoEncontradaException extends RuntimeException {
    public ResenaNoEncontradaException(Long id) {
        super("No se encontró la reseña con id: " + id);

    }
}
