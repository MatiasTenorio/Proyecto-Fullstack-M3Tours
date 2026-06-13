package com.M3Tours.operadores.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Integer id;
    private String usuario;
    private String nombre;
    private String apellido;
    private String email;
    private String rut;
    private String psw;
    private LocalDateTime fechaRegistro;
}