package com.example.smartchef.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DespensaResponseDto(
        Integer usuarioId,
        Integer ingredienteId,
        String ingredienteNombre,
        BigDecimal cantidad,
        LocalDate caducidad
) {}