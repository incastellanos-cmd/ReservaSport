package com.reservasport.notificaciones.repository;

import com.reservasport.notificaciones.model.EstadoNotificacion;
import com.reservasport.notificaciones.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findByUsuarioId(Long usuarioId);

    List<Notificacion> findByReservaId(Long reservaId);

    List<Notificacion> findByEstado(EstadoNotificacion estado);
}
