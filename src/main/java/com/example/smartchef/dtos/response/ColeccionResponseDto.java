package com.example.smartchef.dtos.response;

import java.util.List;

public record ColeccionResponseDto(
        Integer id,
        String nombre,
        Integer usuarioId,
        List<RecetaDetalleResponseDto> recetas
) {}