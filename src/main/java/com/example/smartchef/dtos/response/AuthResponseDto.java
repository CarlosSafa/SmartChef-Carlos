package com.example.smartchef.dtos.response;

public record AuthResponseDto(
        String token,
        String tipoToken,
        Integer usuarioId,
        String nombre,
        String email,
        Integer rol
) {
    public AuthResponseDto(String token, Integer usuarioId, String nombre, String email, Integer rol) {
        this(token, "Bearer", usuarioId, nombre, email, rol);
    }
}