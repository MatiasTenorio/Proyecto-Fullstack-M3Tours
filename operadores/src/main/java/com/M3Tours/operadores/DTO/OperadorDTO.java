package com.M3Tours.operadores.DTO;

import lombok.Data;

@Data
public class OperadorDTO {
    private Integer id;

    private Integer empresaId;
    
    private Integer usuarioId;
    
    private String telefono;
}