package com.example.smartchef.dtos.request;

import jakarta.validation.constraints.NotNull;

public record UsuarioRecetaRequestDto(
        @NotNull(message = "El ID de la receta es obligatorio")
        Integer recetaId,

        Integer tipo
) {}