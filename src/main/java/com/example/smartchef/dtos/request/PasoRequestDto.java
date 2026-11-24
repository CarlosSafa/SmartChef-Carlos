package com.example.smartchef.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PasoRequestDto(
        @NotNull(message = "El número de paso es obligatorio")
        @Positive(message = "El número de paso debe ser un entero positivo")
        Integer numero,

        @NotBlank(message = "La descripción del paso no puede estar vacía")
        String descripcion
) {}