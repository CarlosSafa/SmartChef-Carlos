package com.example.smartchef.dtos.response;

import com.example.smartchef.modelos.Rol;

public record AuthResponseDto(
        String token,
        String tipoToken,
        Integer usuarioId,
        String nombre,
        String email,
        Rol rol
) {
    public AuthResponseDto(String token, Integer usuarioId, String nombre, String email, Rol rol) {
        this(token, "Bearer", usuarioId, nombre, email, rol);
    }
}