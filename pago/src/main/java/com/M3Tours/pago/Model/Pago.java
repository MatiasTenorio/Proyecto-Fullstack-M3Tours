package com.M3Tours.pago.Model;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="pagos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pago {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name="orden_compra", nullable=false)
    private Integer ordenCompra;

    @Column(name="usuario_id", nullable=false)
    private Integer usuarioId;

    @Column(name="reserva_id", nullable=false)
    private Integer reservaId;

    @Column(name="costo", nullable=false)
    private Double costo;

    @Column(name="fecha_pago")
    private LocalDate fechaPago;

    @CreationTimestamp
    @Column(name="fecha_emision", updatable=false)
    private LocalDate fechaEmision;
}
