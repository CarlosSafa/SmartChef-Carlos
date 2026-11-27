package com.example.smartchef.dtos.response;

// Nunca se devuelve la clave del fichero en disco (sección 3.4)
public record FotoResponseDto(
        Integer id,
        Integer recetaId,
        String nombreOriginal,
        Integer orden,
        Boolean portada
) {}
