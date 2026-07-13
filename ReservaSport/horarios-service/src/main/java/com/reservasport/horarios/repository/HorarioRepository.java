package com.reservasport.horarios.repository;

import com.reservasport.horarios.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;

public interface HorarioRepository extends JpaRepository<Horario, Long> {

    boolean existsByCanchaIdAndFechaAndHoraInicioAndHoraFin(
            Long canchaId,
            LocalDate fecha,
            LocalTime horaInicio,
            LocalTime horaFin
    );
}
