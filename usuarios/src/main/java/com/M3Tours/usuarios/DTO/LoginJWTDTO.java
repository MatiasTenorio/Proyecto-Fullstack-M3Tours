package com.M3Tours.usuarios.DTO;

import lombok.Data;

@Data
public class LoginJWTDTO {
    private String username;
    private String password;
}