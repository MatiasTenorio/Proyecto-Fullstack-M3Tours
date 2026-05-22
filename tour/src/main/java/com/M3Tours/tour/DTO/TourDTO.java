package com.M3Tours.tour.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class TourDTO {
    private Integer id;
    private Integer operadorId;
    private int numeroReservas;
    private String ubicacionInicial;
    private LocalDate fechaInicial;
}