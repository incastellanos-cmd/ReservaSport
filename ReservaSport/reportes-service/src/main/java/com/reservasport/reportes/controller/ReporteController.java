package com.reservasport.reportes.controller;

import com.reservasport.reportes.dto.ReporteRequest;
import com.reservasport.reportes.dto.ReporteResponse;
import com.reservasport.reportes.model.EstadoReporte;
import com.reservasport.reportes.service.ReporteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService service;

    @PostMapping
    public ResponseEntity<ReporteResponse> crear(
            @Valid @RequestBody ReporteRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.crearReporte(request));
    }

    @GetMapping
    public ResponseEntity<List<ReporteResponse>> listar() {
        return ResponseEntity.ok(service.listarReportes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReporteResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ReporteResponse>> listarEstado(
            @PathVariable EstadoReporte estado) {

        return ResponseEntity.ok(service.listarPorEstado(estado));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ReporteResponse> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoReporte estado) {

        return ResponseEntity.ok(service.actualizarEstado(id, estado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        service.eliminarReporte(id);

        return ResponseEntity.noContent().build();
    }

}
