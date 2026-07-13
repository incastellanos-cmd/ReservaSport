package com.reservasport.notificaciones.service;

import com.reservasport.notificaciones.dto.NotificacionRequest;
import com.reservasport.notificaciones.dto.NotificacionResponse;
import com.reservasport.notificaciones.model.EstadoNotificacion;

import java.util.List;
public interface NotificacionService {

    NotificacionResponse crearNotificacion(NotificacionRequest request);

    List<NotificacionResponse> listarNotificaciones();

    NotificacionResponse obtenerPorId(Long id);

    List<NotificacionResponse> listarPorUsuario(Long usuarioId);

    List<NotificacionResponse> listarPorReserva(Long reservaId);

    NotificacionResponse actualizarEstado(
            Long id,
            EstadoNotificacion estado
    );

    void eliminarNotificacion(Long id);
}
