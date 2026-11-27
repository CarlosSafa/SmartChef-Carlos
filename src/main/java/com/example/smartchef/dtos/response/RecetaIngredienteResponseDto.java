package com.example.smartchef.dtos.response;

import com.example.smartchef.modelos.UnidadMedida;

import java.math.BigDecimal;

public record RecetaIngredienteResponseDto(
        Integer ingredienteId,
        String nombreIngrediente,
        BigDecimal cantidad,
        UnidadMedida unidadMedida,
        BigDecimal caloriasCalculadas,
        BigDecimal costeCalculado
) {}