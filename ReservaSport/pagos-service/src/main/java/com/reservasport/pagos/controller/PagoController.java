package com.reservasport.pagos.controller;

import com.reservasport.pagos.dto.PagoRequest;
import com.reservasport.pagos.dto.PagoResponse;
import com.reservasport.pagos.model.EstadoPago;
import com.reservasport.pagos.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @PostMapping
    public ResponseEntity<PagoResponse> crearPago(
            @Valid @RequestBody PagoRequest request
    ) {
        PagoResponse response = pagoService.crearPago(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<PagoResponse>> listarPagos() {
        return ResponseEntity.ok(
                pagoService.listarPagos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoResponse> obtenerPagoPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                pagoService.obtenerPagoPorId(id)
        );
    }

    @GetMapping("/reserva/{reservaId}")
    public ResponseEntity<List<PagoResponse>> listarPorReserva(
            @PathVariable Long reservaId
    ) {
        return ResponseEntity.ok(
                pagoService.listarPorReserva(reservaId)
        );
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<PagoResponse> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoPago estado
    ) {
        return ResponseEntity.ok(
                pagoService.actualizarEstado(id, estado)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(
            @PathVariable Long id
    ) {
        pagoService.eliminarPago(id);

        return ResponseEntity.noContent().build();
    }
}
