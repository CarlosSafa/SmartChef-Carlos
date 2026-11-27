package com.example.smartchef.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

// 15. La semana completa: de lunes a domingo, con el total
public record SemanaResponseDto(
        LocalDate desde,
        LocalDate hasta,
        BigDecimal caloriasSemana,
        List<ResumenDiaDto> dias
) {}
