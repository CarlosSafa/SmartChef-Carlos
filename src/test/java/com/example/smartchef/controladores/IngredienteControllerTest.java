package com.example.smartchef.controladores;

import com.example.smartchef.dtos.response.IngredienteResponseDto;
import com.example.smartchef.excepciones.ConflictoException;
import com.example.smartchef.excepciones.RecursoNoEncontradoException;
import com.example.smartchef.modelos.Categoria;
import com.example.smartchef.modelos.UnidadMedida;
import com.example.smartchef.servicios.IngredienteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Prueba el endpoint 6 y los extra de ingredientes llamando a su URL
@WebMvcTest(IngredienteController.class)
class IngredienteControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private IngredienteService ingredienteService;

    private final IngredienteResponseDto tomate =
            new IngredienteResponseDto(1, "Tomate", UnidadMedida.GRAMOS, Categoria.VERDULERIA, new BigDecimal("0.18"), new BigDecimal("0.0025"));

    private final String tomateJson = """
            { "nombre": "Tomate", "unidadMedida": "GRAMOS", "categoria": "VERDULERIA", "calorias": 0.18, "precio": 0.0025 }
            """;

    @Test
    @DisplayName("Endpoint 6 · GET /api/v1/ingredientes?texto=tom → 200 con la página de ingredientes")
    void endpoint6_catalogo() throws Exception {
        given(ingredienteService.listar(any(), any())).willReturn(new PageImpl<>(List.of(tomate)));

        mockMvc.perform(get("/api/v1/ingredientes").param("texto", "tom"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nombre").value("Tomate"));
    }

    @Test
    @DisplayName("Extra · GET /api/v1/ingredientes/99 → 404 si no existe")
    void buscar_noExiste() throws Exception {
        given(ingredienteService.buscarPorId(99)).willThrow(new RecursoNoEncontradoException("No existe el ingrediente con id 99"));

        mockMvc.perform(get("/api/v1/ingredientes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Extra · POST /api/v1/ingredientes → 201 con cabecera Location")
    void alta() throws Exception {
        given(ingredienteService.crear(any())).willReturn(tomate);

        mockMvc.perform(post("/api/v1/ingredientes").contentType(MediaType.APPLICATION_JSON).content(tomateJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    @DisplayName("Extra · POST /api/v1/ingredientes repetido → 409")
    void alta_duplicado() throws Exception {
        given(ingredienteService.crear(any())).willThrow(new ConflictoException("Ya existe un ingrediente con el nombre Tomate"));

        mockMvc.perform(post("/api/v1/ingredientes").contentType(MediaType.APPLICATION_JSON).content(tomateJson))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Extra · POST /api/v1/ingredientes con precio negativo → 400 indicando el campo")
    void alta_precioNegativo() throws Exception {
        String json = tomateJson.replace("0.0025", "-1");

        mockMvc.perform(post("/api/v1/ingredientes").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.precio").exists());
    }
}
