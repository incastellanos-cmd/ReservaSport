package com.reservasport.reservas.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long usuarioId;

    @Column(nullable = false)
    private Long canchaId;

    @Column(nullable = false)
    private Long horarioId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorReserva;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoReserva estado;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    public void prePersist() {

        if (estado == null) {
            estado = EstadoReserva.PENDIENTE;
        }

        fechaCreacion = LocalDateTime.now();
    }
}
