package com.reservasport.reservas.repository;

import com.reservasport.reservas.model.EstadoReserva;
import com.reservasport.reservas.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    boolean existsByHorarioIdAndEstadoNot(
            Long horarioId,
            EstadoReserva estado
    );
}
