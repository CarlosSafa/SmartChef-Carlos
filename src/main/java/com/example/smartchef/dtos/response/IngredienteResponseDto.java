package com.example.smartchef.dtos.response;

import java.math.BigDecimal;

public record IngredienteResponseDto(
        Integer id,
        String nombre,
        Integer unidadMedida,
        BigDecimal calorias,
        BigDecimal precio
) {}