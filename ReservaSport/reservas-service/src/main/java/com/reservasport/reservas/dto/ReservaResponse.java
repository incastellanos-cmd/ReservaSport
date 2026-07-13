package com.reservasport.reservas.dto;

import com.reservasport.reservas.model.EstadoReserva;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaResponse {

    private Long id;
    private Long usuarioId;
    private Long canchaId;
    private Long horarioId;
    private BigDecimal valorReserva;
    private EstadoReserva estado;
    private LocalDateTime fechaCreacion;

}
