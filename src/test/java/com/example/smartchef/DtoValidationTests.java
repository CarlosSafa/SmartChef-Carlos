package com.example.smartchef;

import com.example.smartchef.dtos.request.IngredienteRequestDto;
import com.example.smartchef.dtos.request.RecetaCreacionDto;
import com.example.smartchef.dtos.request.RegistroRequestDto;
import com.example.smartchef.dtos.request.RegistroComidaRequestDto;
import com.example.smartchef.modelos.Categoria;
import com.example.smartchef.modelos.Dificultad;
import com.example.smartchef.modelos.Momento;
import com.example.smartchef.modelos.UnidadMedida;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DtoValidationTests {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("RegistroRequestDto válido no genera violaciones")
    void testRegistroRequestDto_Valido() {
        RegistroRequestDto dto = new RegistroRequestDto("Carlos Gómez", "carlos@example.com", "password123");
        Set<ConstraintViolation<RegistroRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Un DTO correcto no debe dar fallos de validación");
    }

    @Test
    @DisplayName("RegistroRequestDto con email inválido y campos vacíos debe fallar")
    void testRegistroRequestDto_Invalido() {
        RegistroRequestDto dto = new RegistroRequestDto("", "email-invalido", "123");
        Set<ConstraintViolation<RegistroRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Debe detectar errores de validación");
        assertTrue(violations.size() >= 3, "Debe fallar nombre vacío, email incorrecto y contraseña corta");
    }

    @Test
    @DisplayName("IngredienteRequestDto con valores negativos debe fallar")
    void testIngredienteRequestDto_PrecioNegativo() {
        IngredienteRequestDto dto = new IngredienteRequestDto(
                "Patata",
                UnidadMedida.GRAMOS,
                Categoria.VERDULERIA,
                new BigDecimal("-10.00"), // Calorías negativas
                new BigDecimal("-0.50")   // Precio negativo
        );

        Set<ConstraintViolation<IngredienteRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty(), "No debe permitir calorías o precios negativos");
    }

    @Test
    @DisplayName("RecetaCreacionDto sin ingredientes o sin pasos debe fallar")
    void testRecetaCreacionDto_SinIngredientes() {
        RecetaCreacionDto dto = new RecetaCreacionDto(
                "Paella",
                "Receta típica",
                Dificultad.MEDIA,
                45,
                4,
                List.of(), // Lista de ingredientes vacía
                List.of()  // Lista de pasos vacía
        );

        Set<ConstraintViolation<RecetaCreacionDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("ingredientes")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("pasos")));
    }

    @Test
    @DisplayName("RegistroComidaRequestDto con fecha futura debe fallar (RN-07)")
    void testRegistroComida_FechaFutura() {
        RegistroComidaRequestDto dto = new RegistroComidaRequestDto(1, null, LocalDate.now().plusDays(1), Momento.CENA);

        Set<ConstraintViolation<RegistroComidaRequestDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("fecha", violations.iterator().next().getPropertyPath().toString());
    }
}