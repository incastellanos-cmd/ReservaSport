package com.reservasport.reservas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "canchas-service",
        url = "${clients.canchas.url}")
public interface CanchaClient {

    @GetMapping("/api/canchas/{id}")
    CanchaClientResponse obtenerCanchaPorId
            (@PathVariable("id")Long id);
}
