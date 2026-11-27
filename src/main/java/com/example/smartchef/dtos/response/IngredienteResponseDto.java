package com.example.smartchef.dtos.response;

import com.example.smartchef.modelos.Categoria;
import com.example.smartchef.modelos.UnidadMedida;

import java.math.BigDecimal;

public record IngredienteResponseDto(
        Integer id,
        String nombre,
        UnidadMedida unidadMedida,
        Categoria categoria,
        BigDecimal calorias,
        BigDecimal precio
) {}
