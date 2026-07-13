package com.reservasport.notificaciones.dto;

import com.reservasport.notificaciones.model.EstadoNotificacion;
import com.reservasport.notificaciones.model.TipoNotificacion;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacionResponse {

    private Long id;
    private Long usuarioId;
    private Long reservaId;
    private TipoNotificacion tipo;
    private String asunto;
    private String mensaje;
    private EstadoNotificacion estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaEnvio;
}
