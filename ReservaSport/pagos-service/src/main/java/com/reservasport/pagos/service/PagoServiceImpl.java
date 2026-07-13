package com.reservasport.pagos.service;

import com.reservasport.pagos.client.ReservaClient;
import com.reservasport.pagos.dto.PagoRequest;
import com.reservasport.pagos.dto.PagoResponse;
import com.reservasport.pagos.dto.ReservaResponse;
import com.reservasport.pagos.exception.PagoDuplicadoException;
import com.reservasport.pagos.exception.PagoInvalidoException;
import com.reservasport.pagos.exception.PagoNoEncontradoException;
import com.reservasport.pagos.model.EstadoPago;
import com.reservasport.pagos.model.Pago;
import com.reservasport.pagos.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;
    private final ReservaClient reservaClient;

    @Override
    public PagoResponse crearPago(PagoRequest request) {
        log.info(
                "Creando pago para la reserva {}",
                request.getReservaId()
        );

        ReservaResponse reserva =
                reservaClient.obtenerReservaPorId(request.getReservaId());

        validarReserva(reserva);

        boolean pagoAprobadoExistente =
                pagoRepository.existsByReservaIdAndEstado(
                        request.getReservaId(),
                        EstadoPago.APROBADO
                );

        if (pagoAprobadoExistente) {
            throw new PagoDuplicadoException(request.getReservaId());
        }

        Pago pago = Pago.builder()
                .reservaId(request.getReservaId())
                .monto(request.getMonto())
                .metodoPago(request.getMetodoPago())
                .estado(EstadoPago.PENDIENTE)
                .codigoTransaccion(generarCodigoTransaccion())
                .observacion(request.getObservacion())
                .build();

        Pago pagoGuardado = pagoRepository.save(pago);

        log.info(
                "Pago creado correctamente con id {}",
                pagoGuardado.getId()
        );

        return convertirAResponse(pagoGuardado);
    }

    @Override
    public List<PagoResponse> listarPagos() {
        return pagoRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Override
    public PagoResponse obtenerPagoPorId(Long id) {
        Pago pago = buscarPago(id);
        return convertirAResponse(pago);
    }

    @Override
    public List<PagoResponse> listarPorReserva(Long reservaId) {
        return pagoRepository.findByReservaId(reservaId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Override
    public PagoResponse actualizarEstado(
            Long id,
            EstadoPago estado
    ) {
        Pago pago = buscarPago(id);

        pago.setEstado(estado);

        Pago pagoActualizado = pagoRepository.save(pago);

        log.info(
                "Estado del pago {} actualizado a {}",
                id,
                estado
        );

        return convertirAResponse(pagoActualizado);
    }

    @Override
    public void eliminarPago(Long id) {
        Pago pago = buscarPago(id);

        pagoRepository.delete(pago);

        log.info("Pago {} eliminado", id);
    }

    private Pago buscarPago(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() ->
                        new PagoNoEncontradoException(id)
                );
    }

    private void validarReserva(ReservaResponse reserva) {
        if (reserva == null) {
            throw new PagoInvalidoException(
                    "No fue posible obtener la reserva"
            );
        }

        if ("CANCELADA".equalsIgnoreCase(reserva.getEstado())) {
            throw new PagoInvalidoException(
                    "No se puede pagar una reserva cancelada"
            );
        }
    }

    private String generarCodigoTransaccion() {
        return "PAGO-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    private PagoResponse convertirAResponse(Pago pago) {
        return PagoResponse.builder()
                .id(pago.getId())
                .reservaId(pago.getReservaId())
                .monto(pago.getMonto())
                .metodoPago(pago.getMetodoPago())
                .estado(pago.getEstado())
                .codigoTransaccion(pago.getCodigoTransaccion())
                .fechaPago(pago.getFechaPago())
                .observacion(pago.getObservacion())
                .build();
    }
}
