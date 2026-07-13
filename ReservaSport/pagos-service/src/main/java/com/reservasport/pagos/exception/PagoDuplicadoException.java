package com.reservasport.pagos.exception;

public class PagoDuplicadoException extends RuntimeException {


    public PagoDuplicadoException(Long reservaId) {
        super(
                "La reserva con id " + reservaId
                        + " ya posee un pago aprobado"
        );
    }
}
