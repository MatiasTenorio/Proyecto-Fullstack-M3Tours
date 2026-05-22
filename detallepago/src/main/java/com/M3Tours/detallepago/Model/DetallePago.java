package com.M3Tours.detallepago.Model;

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
@Table(name = "detalle_pagos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetallePago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
 
    @Column(name = "numero_boleta")
    private String numeroBoleta;
 
    @Column(name = "tipo_pago")
    private String tipoPago;
 
    @Column(name = "estado")
    private String estado;
 
    @Column(name = "nombre_tour")
    private String nombreTour;
 
    @Column(name = "numero_asiento")
    private String numeroAsiento;
 
    @Column(name = "subtotal")
    private Double subtotal;
 
    @Column(name = "impuesto")
    private Double impuesto;
 
    @Column(name = "total")
    private Double total;
 
}