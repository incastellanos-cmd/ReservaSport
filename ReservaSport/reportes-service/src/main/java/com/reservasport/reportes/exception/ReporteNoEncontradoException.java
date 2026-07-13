package com.reservasport.reportes.exception;

public class ReporteNoEncontradoException extends RuntimeException {
    public ReporteNoEncontradoException(Long id) {
        super("No se encontró el reporte con id: " + id);
    }


}
