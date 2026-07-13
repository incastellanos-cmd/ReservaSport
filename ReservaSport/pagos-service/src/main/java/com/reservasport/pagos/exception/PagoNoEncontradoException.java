package com.reservasport.pagos.exception;

public class PagoNoEncontradoException extends RuntimeException {

    public PagoNoEncontradoException(Long id) {
        super("No se encontró el pago con id: " + id);
    }
}
