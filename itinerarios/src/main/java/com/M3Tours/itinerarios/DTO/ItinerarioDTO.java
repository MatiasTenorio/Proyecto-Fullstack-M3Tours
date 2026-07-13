package com.M3Tours.itinerarios.DTO;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItinerarioDTO {

    private Integer tourId;

    private Integer destinoId;

    private Integer dia;

    private String descripcion;

    private LocalTime horaInicio;

    private LocalTime horaFin;

    private String lugar;
}
