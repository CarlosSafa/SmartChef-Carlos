package com.example.smartchef.dtos.response;

import com.example.smartchef.modelos.Momento;

import java.time.LocalDate;

public record RegistroComidaResponseDto(
        Integer id,
        Integer usuarioId,
        Integer recetaId,
        String recetaNombre,
        String descripcion,
        LocalDate fecha,
        Momento momento
) {}
