package com.reservasport.complejos.repository;

import com.reservasport.complejos.model.Complejo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplejoRepository  extends JpaRepository<Complejo, Long> {

    boolean existsByNombreIgnoreCase(String nombre);



}
