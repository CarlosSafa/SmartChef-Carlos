package com.example.smartchef;

import com.example.smartchef.dtos.response.IngredienteResponseDto;
import com.example.smartchef.mappers.IngredienteMapper;
import com.example.smartchef.modelos.Categoria;
import com.example.smartchef.modelos.Ingrediente;
import com.example.smartchef.modelos.UnidadMedida;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MapperTests {

    @Autowired
    private IngredienteMapper ingredienteMapper;

    @Test
    @DisplayName("Probar conversión de la entidad Ingrediente a IngredienteResponseDto")
    void testIngredienteMapper() {
        Ingrediente ingrediente = Ingrediente.builder()
                .id(10)
                .nombre("Aceite de Oliva")
                .unidadMedida(UnidadMedida.MILILITROS)
                .categoria(Categoria.DESPENSA)
                .calorias(new BigDecimal("8.84"))
                .precio(new BigDecimal("0.0090"))
                .build();

        IngredienteResponseDto responseDto = ingredienteMapper.aResponseDto(ingrediente);

        assertNotNull(responseDto);
        assertEquals(10, responseDto.id());
        assertEquals("Aceite de Oliva", responseDto.nombre());
        assertEquals(UnidadMedida.MILILITROS, responseDto.unidadMedida());
        assertEquals(Categoria.DESPENSA, responseDto.categoria());
    }
}
