package com.example.smartchef.dtos.response;

import java.util.List;

// 16. Respuesta del informe de recetas populares
public record InformeRecetasPopularesDto(
        List<RecetaPopularDto> masCocinadas,
        List<RecetaPopularDto> masGuardadas
) {}
