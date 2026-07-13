package com.reservasport.promociones.controller;

import com.reservasport.promociones.dto.PromocionRequest;
import com.reservasport.promociones.dto.PromocionResponse;
import com.reservasport.promociones.model.EstadoPromocion;
import com.reservasport.promociones.service.PromocionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promociones")
@RequiredArgsConstructor
public class PromocionController {

    private final PromocionService promocionService;

    @PostMapping
    public ResponseEntity<PromocionResponse> crearPromocion(
            @Valid @RequestBody PromocionRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(promocionService.crearPromocion(request));
    }

    @GetMapping
    public ResponseEntity<List<PromocionResponse>> listarPromociones() {

        return ResponseEntity.ok(promocionService.listarPromociones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromocionResponse> obtenerPromocion(
            @PathVariable Long id) {

        return ResponseEntity.ok(promocionService.obtenerPromocionPorId(id));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PromocionResponse>> listarPorEstado(
            @PathVariable EstadoPromocion estado) {

        return ResponseEntity.ok(promocionService.listarPorEstado(estado));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<PromocionResponse> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoPromocion estado) {

        return ResponseEntity.ok(promocionService.actualizarEstado(id, estado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPromocion(
            @PathVariable Long id) {

        promocionService.eliminarPromocion(id);

        return ResponseEntity.noContent().build();
    }
}
