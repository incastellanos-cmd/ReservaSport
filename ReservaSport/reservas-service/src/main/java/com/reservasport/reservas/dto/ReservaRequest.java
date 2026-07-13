package com.reservasport.reservas.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaRequest {

    @NotNull(message = "El ID del usuario es obligatorio")
    @Positive(message = "El ID del usuario debe ser mayor que cero")
    private Long usuarioId;

    @NotNull(message = "El ID de la cancha es obligatorio")
    @Positive(message = "El ID de la cancha debe ser mayor que cero")
    private Long canchaId;

    @NotNull(message = "El ID del horario es obligatorio")
    @Positive(message = "El ID del horario debe ser mayor que cero")
    private Long horarioId;
}

