package com.M3Tours.tour.Model;

import java.time.LocalDate;

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
@Table(name="tours")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tour {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name="operador_id")
    private Integer operadorId;

    @Column(name="reserva_id")
    private Integer reservaId;

    @Column(name="numero_reservas")
    private int numeroReservas;

    @Column(name="ubicacion_inicial")
    private String ubicacionInicial;

    @Column(name="fecha_inicial")
    private LocalDate fechaInicial;
}