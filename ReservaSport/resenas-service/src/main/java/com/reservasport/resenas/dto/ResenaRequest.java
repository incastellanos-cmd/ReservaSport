package com.reservasport.resenas.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResenaRequest {

    @NotNull
    private Long usuarioId;

    @NotNull
    private Long canchaId;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer calificacion;

    @Size(max = 1000)
    private String comentario;

}
