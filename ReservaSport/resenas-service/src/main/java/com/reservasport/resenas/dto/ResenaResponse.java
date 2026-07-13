package com.reservasport.resenas.dto;

import com.reservasport.resenas.model.EstadoResena;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResenaResponse {

    private Long id;
    private Long usuarioId;
    private Long canchaId;
    private Integer calificacion;
    private String comentario;
    private EstadoResena estado;
    private LocalDateTime fechaCreacion;
}
