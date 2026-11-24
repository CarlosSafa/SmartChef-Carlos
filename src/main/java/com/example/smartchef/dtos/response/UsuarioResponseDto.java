package com.example.smartchef.dtos.response;

import java.util.List;

public record UsuarioResponseDto(
        Integer id,
        String nombre,
        String email,
        Integer rol,
        List<Integer> preferencias
) {}