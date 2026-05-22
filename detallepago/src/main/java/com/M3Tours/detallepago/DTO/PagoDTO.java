package com.M3Tours.detallepago.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PagoDTO {
    private Integer id;
    private Integer ordenCompra;
    private Integer usuarioId;
    private Integer reservaId;
    private Double costo;
    private LocalDate fechaPago;
    private LocalDate fechaEmision;
}