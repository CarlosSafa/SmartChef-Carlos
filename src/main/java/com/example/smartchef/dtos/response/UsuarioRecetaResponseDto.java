package com.example.smartchef.dtos.response;

import java.time.LocalDateTime;

public record UsuarioRecetaResponseDto(
        Integer id,
        Integer usuarioId,
        Integer recetaId,
        String recetaNombre,
        LocalDateTime fecha,
        Integer tipo
) {}