package com.reservasport.reservas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "horarios-service",
        url = "${clients.horarios.url}"
)
public interface HorarioClient {

    @GetMapping("/api/horarios/{id}")
    HorarioClientResponse obtenerHorarioPorId(
            @PathVariable Long id
    );

    @PatchMapping("/api/horarios/{id}/disponibilidad")
    HorarioClientResponse actualizarDisponibilidad(
            @PathVariable ("id")Long id,
            @RequestParam ("disponible")Boolean disponible
    );
}
