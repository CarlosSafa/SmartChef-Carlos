package com.example.smartchef.dtos.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record RecetaIngredienteRequestDto(
        @NotNull(message = "El ID del ingrediente es obligatorio")
        Integer ingredienteId,

        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad debe ser mayor que cero")
        BigDecimal cantidad
) {}