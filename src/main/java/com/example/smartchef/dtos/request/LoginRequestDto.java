package com.example.smartchef.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Email no válido")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        String contrasena
) {}