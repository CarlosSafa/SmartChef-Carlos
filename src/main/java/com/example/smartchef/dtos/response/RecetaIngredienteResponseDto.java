package com.example.smartchef.dtos.response;

import com.example.smartchef.modelos.UnidadMedida;

import java.math.BigDecimal;

// Un ingrediente dentro del detalle de una receta
public record RecetaIngredienteResponseDto(
        Integer ingredienteId,
        String nombreIngrediente,
        BigDecimal cantidad,
        UnidadMedida unidadMedida
) {}
