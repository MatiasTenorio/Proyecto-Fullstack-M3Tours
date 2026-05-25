package com.M3Tours.categoria.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaDTO {

    private String nombre;

    private String descripcion;

    private String estado;
}