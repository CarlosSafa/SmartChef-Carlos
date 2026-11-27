package com.example.smartchef.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDate;

// 15. Un día de la semana con sus calorías
public record ResumenDiaDto(
        LocalDate fecha,
        BigDecimal calorias
) {}
