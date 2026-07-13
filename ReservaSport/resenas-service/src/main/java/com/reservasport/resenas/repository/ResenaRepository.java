package com.reservasport.resenas.repository;

import com.reservasport.resenas.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResenaRepository extends JpaRepository<Resena, Long>  {

    List<Resena> findByCanchaId(Long canchaId);

    List<Resena> findByUsuarioId(Long usuarioId);
}
