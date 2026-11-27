package com.example.smartchef.controladores;

import com.example.smartchef.dtos.response.IngredienteResponseDto;
import com.example.smartchef.modelos.Categoria;
import com.example.smartchef.modelos.UnidadMedida;
import com.example.smartchef.servicios.IngredienteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Prueba el endpoint 6 llamando a su URL
@WebMvcTest(IngredienteController.class)
class IngredienteControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private IngredienteService ingredienteService;

    @Test
    @DisplayName("Endpoint 6 · GET /api/v1/ingredientes?texto=tom → 200 con la página de ingredientes")
    void endpoint6_catalogo() throws Exception {
        IngredienteResponseDto tomate = new IngredienteResponseDto(
                1, "Tomate", UnidadMedida.GRAMOS, Categoria.VERDULERIA, new BigDecimal("0.18"), new BigDecimal("0.0025"));
        given(ingredienteService.listar(any(), any())).willReturn(new PageImpl<>(List.of(tomate)));

        mockMvc.perform(get("/api/v1/ingredientes").param("texto", "tom"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nombre").value("Tomate"))
                .andExpect(jsonPath("$.content[0].unidadMedida").value("GRAMOS"));
    }
}
