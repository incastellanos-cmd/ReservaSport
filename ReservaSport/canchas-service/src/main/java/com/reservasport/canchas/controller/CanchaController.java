package com.reservasport.canchas.controller;

import com.reservasport.canchas.dto.CanchaRequest;
import com.reservasport.canchas.dto.CanchaResponse;
import com.reservasport.canchas.service.CanchaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/canchas")
@RequiredArgsConstructor
public class CanchaController {

    private final CanchaService canchaService;

    @PostMapping
    public ResponseEntity<CanchaResponse> crearCancha(
            @Valid @RequestBody CanchaRequest request) {

        CanchaResponse response = canchaService.crearCancha(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CanchaResponse>> obtenerCanchas() {

        return ResponseEntity.ok(canchaService.obtenerCanchas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CanchaResponse> obtenerCanchaPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(canchaService.obtenerCanchaPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CanchaResponse> actualizarCancha(
            @PathVariable Long id,
            @Valid @RequestBody CanchaRequest request) {

        return ResponseEntity.ok(
                canchaService.actualizarCancha(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCancha(
            @PathVariable Long id) {

        canchaService.eliminarCancha(id);

        return ResponseEntity.noContent().build();
    }
}
