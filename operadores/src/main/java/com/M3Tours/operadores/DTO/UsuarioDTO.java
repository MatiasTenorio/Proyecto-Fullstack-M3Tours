package com.M3Tours.operadores.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Integer id;
    private String usuario;
    private String nombreUsuario;
    private String apellidoUsuario;
    private String emailUsuario;
    private String rutUsuario;
    private String pswUsuario;
    private LocalDateTime fechaRegistro;
}