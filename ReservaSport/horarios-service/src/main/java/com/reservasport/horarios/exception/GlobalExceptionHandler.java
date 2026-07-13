package com.reservasport.horarios.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HorarioNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> manejarHorarioNoEncontrado(
            HorarioNoEncontradoException ex) {

        return construirRespuesta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(HorarioDuplicadoException.class)
    public ResponseEntity<Map<String, Object>> manejarHorarioDuplicado(
            HorarioDuplicadoException ex) {

        return construirRespuesta(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(RangoHorarioInvalidoException.class)
    public ResponseEntity<Map<String, Object>> manejarRangoInvalido(
            RangoHorarioInvalidoException ex) {

        return construirRespuesta(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidaciones(
            MethodArgumentNotValidException ex) {

        Map<String, String> detalles = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                detalles.put(error.getField(), error.getDefaultMessage()));

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("status", 400);
        respuesta.put("error", "BAD_REQUEST");
        respuesta.put("message", "Error de validación");
        respuesta.put("details", detalles);

        return ResponseEntity.badRequest().body(respuesta);
    }

    private ResponseEntity<Map<String, Object>> construirRespuesta(
            HttpStatus status,
            String mensaje) {

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("status", status.value());
        respuesta.put("error", status.name());
        respuesta.put("message", mensaje);

        return ResponseEntity.status(status).body(respuesta);
    }
}
