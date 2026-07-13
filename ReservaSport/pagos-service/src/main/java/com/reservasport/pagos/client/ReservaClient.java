package com.reservasport.pagos.client;

import com.reservasport.pagos.dto.ReservaResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "reservas-service",
        url = "${clients.reservas.url}"
)
public interface ReservaClient {

    @GetMapping("/api/reservas/{id}")
    ReservaResponse obtenerReservaPorId(
            @PathVariable("id") Long id
    );
}
