package com.M3Tours.operadores.DTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    public String mensaje;
    public String detalle;
    public int status;
    public LocalDateTime timeStamp;
}