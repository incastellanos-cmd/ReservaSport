package com.reservasport.complejos.controller;

import com.reservasport.complejos.dto.ComplejoRequest;
import com.reservasport.complejos.dto.ComplejoResponse;
import com.reservasport.complejos.service.ComplejoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complejos")
@RequiredArgsConstructor
public class ComplejoController {

    private final ComplejoService complejoService;

    @PostMapping
    public ResponseEntity<ComplejoResponse> crearComplejo(
            @Valid @RequestBody ComplejoRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(complejoService.crearComplejo(request));
    }

    @GetMapping
    public ResponseEntity<List<ComplejoResponse>> obtenerComplejos() {

        return ResponseEntity.ok(
                complejoService.obtenerComplejos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComplejoResponse> obtenerComplejoPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                complejoService.obtenerComplejoPorId(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComplejoResponse> actualizarComplejo(
            @PathVariable Long id,
            @Valid @RequestBody ComplejoRequest request) {

        return ResponseEntity.ok(
                complejoService.actualizarComplejo(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarComplejo(
            @PathVariable Long id) {

        complejoService.eliminarComplejo(id);

        return ResponseEntity.noContent().build();
    }
}
