package com.reservasport.canchas.repository;

import com.reservasport.canchas.model.Cancha;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CanchaRepository extends JpaRepository<Cancha, Long> {

    boolean existsByNombreIgnoreCase(String nombre);
}
