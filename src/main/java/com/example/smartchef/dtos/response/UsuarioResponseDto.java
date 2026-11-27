package com.example.smartchef.dtos.response;

import com.example.smartchef.modelos.Rol;

import java.util.List;

public record UsuarioResponseDto(
        Integer id,
        String nombre,
        String email,
        Rol rol,
        List<String> preferencias
) {}