package com.example.smartchef.dtos.response;

import java.math.BigDecimal;

public record RecetaIngredienteResponseDto(
        Integer ingredienteId,
        String nombreIngrediente,
        BigDecimal cantidad,
        Integer unidadMedida,
        BigDecimal caloriasCalculadas,
        BigDecimal costeCalculado
) {}