package com.reservasport.horarios.controller;

import com.reservasport.horarios.dto.HorarioRequest;
import com.reservasport.horarios.dto.HorarioResponse;
import com.reservasport.horarios.service.HorarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/horarios")
@RequiredArgsConstructor
public class HorarioController {

    private final HorarioService horarioService;

    @PostMapping
    public ResponseEntity<HorarioResponse> crearHorario(
            @Valid @RequestBody HorarioRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(horarioService.crearHorario(request));
    }

    @GetMapping
    public ResponseEntity<List<HorarioResponse>> obtenerHorarios() {
        return ResponseEntity.ok(horarioService.obtenerHorarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HorarioResponse> obtenerHorarioPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(horarioService.obtenerHorarioPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HorarioResponse> actualizarHorario(
            @PathVariable Long id,
            @Valid @RequestBody HorarioRequest request) {

        return ResponseEntity.ok(
                horarioService.actualizarHorario(id, request)
        );
    }

    @PatchMapping("/{id}/disponibilidad")
    public ResponseEntity<HorarioResponse> actualizarDisponibilidad(
            @PathVariable ("id")Long id,
            @RequestParam ("disponible") Boolean disponible){

        return ResponseEntity.ok(
                horarioService.actualizarDisponibilidad(id, disponible)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHorario(
            @PathVariable Long id) {

        horarioService.eliminarHorario(id);

        return ResponseEntity.noContent().build();
    }
}
