package com.example.smartchef.servicios;

import com.example.smartchef.dtos.request.PasoRequestDto;
import com.example.smartchef.dtos.request.RecetaCreacionDto;
import com.example.smartchef.dtos.request.RecetaIngredienteRequestDto;
import com.example.smartchef.excepciones.ReglaNegocioException;
import com.example.smartchef.mappers.RecetaIngredienteMapper;
import com.example.smartchef.mappers.RecetaMapper;
import com.example.smartchef.repositorios.IngredienteRepository;
import com.example.smartchef.repositorios.RecetaRepository;
import com.example.smartchef.servicios.impl.RecetaServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

// Reglas de negocio del alta de receta (RN-01), sin base de datos: los repositorios son simuladores
@ExtendWith(MockitoExtension.class)
class RecetaServiceTest {

    @Mock private RecetaRepository recetaRepository;
    @Mock private IngredienteRepository ingredienteRepository;
    @Mock private RecetaMapper recetaMapper;
    @Mock private RecetaIngredienteMapper recetaIngredienteMapper;

    @InjectMocks private RecetaServiceImpl recetaService;

    @Test
    @DisplayName("RN-01 · No crea la receta si los pasos tienen huecos (1, 3) y no guarda nada")
    void noCreaSiLosPasosTienenHuecos() {
        RecetaCreacionDto peticion = new RecetaCreacionDto(
                "Tortilla", "Prueba", 1, 25, 4,
                List.of(new RecetaIngredienteRequestDto(1, new BigDecimal("6"))),
                List.of(new PasoRequestDto(1, "Batir"), new PasoRequestDto(3, "Cuajar")));

        assertThatThrownBy(() -> recetaService.crear(peticion))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("sin huecos");

        then(recetaRepository).should(never()).save(any());
    }

    @Test
    @DisplayName("No crea la receta si un ingrediente está repetido y no guarda nada")
    void noCreaSiHayIngredientesRepetidos() {
        RecetaCreacionDto peticion = new RecetaCreacionDto(
                "Tortilla", "Prueba", 1, 25, 4,
                List.of(new RecetaIngredienteRequestDto(1, new BigDecimal("6")),
                        new RecetaIngredienteRequestDto(1, new BigDecimal("2"))),
                List.of(new PasoRequestDto(1, "Batir")));

        assertThatThrownBy(() -> recetaService.crear(peticion))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("repetido");

        then(recetaRepository).should(never()).save(any());
    }
}
