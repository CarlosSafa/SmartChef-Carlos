package com.example.smartchef.dtos.request;

import com.example.smartchef.modelos.Categoria;
import com.example.smartchef.modelos.UnidadMedida;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record IngredienteRequestDto(
        @NotBlank(message = "El nombre del ingrediente es obligatorio")
        String nombre,

        @NotNull(message = "La unidad de medida es obligatoria (GRAMOS, MILILITROS o UNIDADES)")
        UnidadMedida unidadMedida,

        @NotNull(message = "La categoría es obligatoria")
        Categoria categoria,

        @NotNull(message = "Las calorías son obligatorias")
        @PositiveOrZero(message = "Las calorías no pueden ser negativas")
        BigDecimal calorias,

        @NotNull(message = "El precio es obligatorio")
        @PositiveOrZero(message = "El precio no puede ser negativo")
        BigDecimal precio
) {}
