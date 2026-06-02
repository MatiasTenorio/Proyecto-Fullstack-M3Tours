package com.M3Tours.operadores.DTO;

import lombok.Data;

@Data
public class OperadorDTO {

    private Integer empresaId;

    private Integer usuarioId;

    private String nombre;

    private String apellido;

    private String rut;

    private String email;

    private String telefono;
}