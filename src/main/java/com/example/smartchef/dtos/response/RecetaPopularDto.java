package com.example.smartchef.dtos.response;

// Una fila del informe: qué receta y cuántas veces (cocinada o guardada)
public record RecetaPopularDto(
        Integer recetaId,
        String nombre,
        Long total
) {}
