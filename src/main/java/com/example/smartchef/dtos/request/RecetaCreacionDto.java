package com.example.smartchef.dtos.request;

import com.example.smartchef.modelos.Dificultad;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record RecetaCreacionDto(
        @NotBlank(message = "El nombre de la receta es obligatorio")
        @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
        String nombre,

        String descripcion,

        @NotNull(message = "La dificultad es obligatoria (FACIL, MEDIA o DIFICIL)")
        Dificultad dificultad,

        @NotNull(message = "El tiempo de preparación es obligatorio")
        @Positive(message = "El tiempo debe ser mayor a 0 minutos")
        Integer tiempo,

        @NotNull(message = "El número de raciones es obligatorio")
        @Min(value = 1, message = "Mínimo 1 ración")
        @Max(value = 12, message = "Máximo 12 raciones")
        Integer raciones,

        @NotEmpty(message = "La receta debe incluir al menos un ingrediente")
        @Size(max = 30, message = "Una receta no puede tener más de 30 ingredientes")
        @Valid
        List<RecetaIngredienteRequestDto> ingredientes,

        @NotEmpty(message = "La receta debe incluir al menos un paso")
        @Valid
        List<PasoRequestDto> pasos
) {}