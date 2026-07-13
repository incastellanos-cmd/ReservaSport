package com.reservasport.pagos.repository;

import com.reservasport.pagos.model.EstadoPago;
import com.reservasport.pagos.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByReservaId(Long reservaId);

    Optional<Pago> findByCodigoTransaccion(String codigoTransaccion);

    boolean existsByReservaIdAndEstado(
            Long reservaId,
            EstadoPago estado
    );
}
