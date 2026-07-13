package com.reservasport.notificaciones.exception;

public class NotificacionNoEncontradaException extends RuntimeException {
    public NotificacionNoEncontradaException(Long id) {
        super("No se encontró la notificación con id: " + id);
    }
}
