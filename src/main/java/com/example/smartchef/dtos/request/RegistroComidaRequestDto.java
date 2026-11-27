package com.example.smartchef.dtos.request;

import com.example.smartchef.modelos.Momento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

// 14. Lo que se ha cocinado: con receta, o comida libre con solo descripción
public record RegistroComidaRequestDto(
        Integer recetaId,

        @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
        String descripcion,

        // RN-07: no se registran fechas futuras
        @NotNull(message = "La fecha es obligatoria")
        @PastOrPresent(message = "No se pueden registrar fechas futuras")
        LocalDate fecha,

        @NotNull(message = "El momento es obligatorio (DESAYUNO, COMIDA o CENA)")
        Momento momento
) {}
