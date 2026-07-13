package com.reservasport.pagos.dto;

import com.reservasport.pagos.model.EstadoPago;
import com.reservasport.pagos.model.MetodoPago;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoResponse {

    private Long id;
    private Long reservaId;
    private BigDecimal monto;
    private MetodoPago metodoPago;
    private EstadoPago estado;
    private String codigoTransaccion;
    private LocalDateTime fechaPago;
    private String observacion;
}
