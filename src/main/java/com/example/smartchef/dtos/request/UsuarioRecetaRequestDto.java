package com.example.smartchef.dtos.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UsuarioRecetaRequestDto(
        @NotNull(message = "El ID de la receta es obligatorio")
        Integer recetaId,

        // Momento del día: 1 = desayuno, 2 = comida, 3 = cena
        @NotNull(message = "El momento del día es obligatorio")
        @Min(value = 1, message = "El momento debe ser 1 (desayuno), 2 (comida) o 3 (cena)")
        @Max(value = 3, message = "El momento debe ser 1 (desayuno), 2 (comida) o 3 (cena)")
        Integer tipo
) {}
