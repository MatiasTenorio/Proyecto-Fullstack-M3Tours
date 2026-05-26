package com.M3Tours.usuarios.Model;

import java.time.LocalDateTime;

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
@Table(name="usuarios")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    // Atributos de "USUARIO"
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name="usuario", nullable=false)
    private String usuario;

    @Column(name="nombre", nullable=false)
    private String nombreUsuario;

    @Column(name="apellido", nullable=false)
    private String apellidoUsuario;

    @Column(name="email", nullable=false)
    private String emailUsuario;

    @Column(name="rut", nullable=false, length=10)
    private String rutUsuario;

    @Column(name="password", nullable=false)
    private String pswUsuario;
    
    @CreationTimestamp
    @Column(name="fecha_registro", nullable=false, updatable=false)
    private LocalDateTime fechaRegistro;    
}