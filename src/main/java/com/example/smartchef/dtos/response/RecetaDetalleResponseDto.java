package com.example.smartchef.dtos.response;

import java.math.BigDecimal;
import java.util.List;

public record RecetaDetalleResponseDto(
        Integer id,
        String nombre,
        String descripcion,
        Integer dificultad,
        Integer tiempo,
        Integer raciones,
        BigDecimal costePorRacion,
        BigDecimal caloriasPorRacion,
        List<RecetaIngredienteResponseDto> ingredientes,
        List<PasoResponseDto> pasos,
        List<String> fotosUrl
) {}