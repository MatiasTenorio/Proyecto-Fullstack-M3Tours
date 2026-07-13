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
 
    @Column(name = "numero_boleta", nullable=false)
    private String numeroBoleta;
 
    @Column(name = "tipo_pago", nullable=false)
    private String tipoPago;
 
    @Column(name = "estado", nullable=false)
    private String estado;
 
    @Column(name = "nombre_tour", nullable=false)
    private String nombreTour;
 
    @Column(name = "numero_asiento", nullable=false)
    private String numeroAsiento;
 
    @Column(name = "subtotal", nullable=false)
    private Double subtotal;
 
    @Column(name = "impuesto", nullable=false)
    private Double impuesto;
 
    @Column(name = "total", nullable=false)
    private Double total;
 
}