package com.example.smartchef.repositorios;

import java.math.BigDecimal;

// Proyección por interfaz (Q5): una fila del resultado de la consulta nativa de la semana
public interface CaloriasDiaProyeccion {

    // Día de la semana: 1 = lunes ... 7 = domingo
    Integer getDia();

    BigDecimal getCalorias();
}
