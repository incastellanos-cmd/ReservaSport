package com.reservasport.promociones.repository;

import com.reservasport.promociones.model.EstadoPromocion;
import com.reservasport.promociones.model.Promocion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PromocionRepository extends JpaRepository<Promocion, Long>  {

    List<Promocion> findByEstado(EstadoPromocion estado);
}
