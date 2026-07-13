package com.reservasport.resenas.controller;

import com.reservasport.resenas.dto.ResenaRequest;
import com.reservasport.resenas.dto.ResenaResponse;
import com.reservasport.resenas.service.ResenaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resenas")
@RequiredArgsConstructor
public class ResenaController {

    private final ResenaService service;

    @PostMapping
    public ResponseEntity<ResenaResponse> crear(
            @Valid @RequestBody ResenaRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.crearResena(request));

    }

    @GetMapping
    public ResponseEntity<List<ResenaResponse>> listar() {

        return ResponseEntity.ok(service.listarResenas());

    }

    @GetMapping("/{id}")
    public ResponseEntity<ResenaResponse> obtener(@PathVariable Long id) {

        return ResponseEntity.ok(service.obtenerPorId(id));

    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ResenaResponse>> usuario(
            @PathVariable Long usuarioId) {

        return ResponseEntity.ok(service.listarPorUsuario(usuarioId));

    }

    @GetMapping("/cancha/{canchaId}")
    public ResponseEntity<List<ResenaResponse>> cancha(
            @PathVariable Long canchaId) {

        return ResponseEntity.ok(service.listarPorCancha(canchaId));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        service.eliminarResena(id);

        return ResponseEntity.noContent().build();

    }

}
