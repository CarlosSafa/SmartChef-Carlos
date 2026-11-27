package com.example.smartchef;

import com.example.smartchef.dtos.request.RecetaCreacionDto;
import com.example.smartchef.dtos.request.RegistroComidaRequestDto;
import com.example.smartchef.modelos.Dificultad;
import com.example.smartchef.modelos.Momento;
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