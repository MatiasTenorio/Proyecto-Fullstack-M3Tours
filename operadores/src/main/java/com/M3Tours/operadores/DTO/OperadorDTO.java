package com.M3Tours.operadores.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OperadorDTO {
    private Integer id;
    private Integer empresaId;
    private Integer usuarioId;
    private String telefono;
}