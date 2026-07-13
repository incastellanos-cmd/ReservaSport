package com.reservasport.reservas.controller;

import com.reservasport.reservas.dto.ReservaRequest;
import com.reservasport.reservas.dto.ReservaResponse;
import com.reservasport.reservas.service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @PostMapping
    public ResponseEntity<ReservaResponse> crearReserva(
            @Valid @RequestBody ReservaRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservaService.crearReserva(request));
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponse>> obtenerReservas() {

        return ResponseEntity.ok(
                reservaService.obtenerReservas()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponse> obtenerReservaPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                reservaService.obtenerReservaPorId(id)
        );
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<ReservaResponse> confirmarReserva(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                reservaService.confirmarReserva(id)
        );
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarReserva(
            @PathVariable Long id) {

        reservaService.cancelarReserva(id);

        return ResponseEntity.noContent().build();
    }
}
