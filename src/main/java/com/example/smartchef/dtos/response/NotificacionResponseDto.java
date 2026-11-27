package com.example.smartchef.dtos.response;

import com.example.smartchef.modelos.RecursoNotificacion;
import com.example.smartchef.modelos.TipoNotificacion;

import java.time.LocalDateTime;

// Mismo formato que el ejemplo de la sección 3.8 del enunciado
public record NotificacionResponseDto(
        Integer id,
        TipoNotificacion tipo,
        String mensaje,
        RecursoNotificacion recurso,
        Integer recursoId,
        Boolean leida,
        LocalDateTime creadaEn
) {}
