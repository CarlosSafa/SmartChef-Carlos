package com.example.smartchef.dtos.response;

import com.example.smartchef.modelos.Dificultad;

import java.math.BigDecimal;
import java.util.List;

public record RecetaDetalleResponseDto(
        Integer id,
        String nombre,
        String descripcion,
        Dificultad dificultad,
        Integer tiempo,
        Integer raciones,
        BigDecimal costePorRacion,
        BigDecimal caloriasPorRacion,
        List<RecetaIngredienteResponseDto> ingredientes,
        List<PasoResponseDto> pasos
) {}