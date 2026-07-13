package com.M3Tours.destinos.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DestinoDTO {

    private String nombre;

    private String pais;

    private String ciudad;

    private String descripcion;

    private String imagenUrl;
}
