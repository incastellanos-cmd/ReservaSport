package com.reservasport.notificaciones.controller;

import com.reservasport.notificaciones.dto.NotificacionRequest;
import com.reservasport.notificaciones.dto.NotificacionResponse;
import com.reservasport.notificaciones.model.EstadoNotificacion;
import com.reservasport.notificaciones.service.NotificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;

    @PostMapping
    public ResponseEntity<NotificacionResponse> crear(
            @Valid @RequestBody NotificacionRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        notificacionService.crearNotificacion(request)
                );
    }

    @GetMapping
    public ResponseEntity<List<NotificacionResponse>> listar() {
        return ResponseEntity.ok(
                notificacionService.listarNotificaciones()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificacionResponse> obtenerPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                notificacionService.obtenerPorId(id)
        );
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<NotificacionResponse>> listarPorUsuario(
            @PathVariable Long usuarioId
    ) {
        return ResponseEntity.ok(
                notificacionService.listarPorUsuario(usuarioId)
        );
    }

    @GetMapping("/reserva/{reservaId}")
    public ResponseEntity<List<NotificacionResponse>> listarPorReserva(
            @PathVariable Long reservaId
    ) {
        return ResponseEntity.ok(
                notificacionService.listarPorReserva(reservaId)
        );
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<NotificacionResponse> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoNotificacion estado
    ) {
        return ResponseEntity.ok(
                notificacionService.actualizarEstado(id, estado)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id
    ) {
        notificacionService.eliminarNotificacion(id);
        return ResponseEntity.noContent().build();
    }
}
