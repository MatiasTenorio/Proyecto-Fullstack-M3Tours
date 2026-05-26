package com.M3Tours.operadores.DTO;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OperadorDTO {

    private Integer empresaId;

    private String nombre;

    private String apellido;

    private String rut;

    private String email;

    private String telefono;
}