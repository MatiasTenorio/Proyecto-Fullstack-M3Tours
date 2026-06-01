package com.M3Tours.detallepago.DTO;

import lombok.Data;

@Data
public class DetallePagoDTO {
    private Integer id;
    private String numeroBoleta;
    private String tipoPago;
    private String estado;
    private String nombreTour;
    private String numeroAsiento;
    private Double subtotal;
    private Double impuesto;
    private Double total;
 
}
 