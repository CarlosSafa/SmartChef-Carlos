package com.example.smartchef.controladores;

import com.example.smartchef.dtos.response.RecetaDetalleResponseDto;
import com.example.smartchef.excepciones.RecursoNoEncontradoException;
import com.example.smartchef.excepciones.ReglaNegocioException;
import com.example.smartchef.servicios.RecetaService;
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

// Prueba los endpoints 1, 2 y 3 llamando a su URL, como hace Postman
@WebMvcTest(RecetaController.class)
class RecetaControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private RecetaService recetaService;

    private final RecetaDetalleResponseDto tortilla = new RecetaDetalleResponseDto(
            1, "Tortilla", "La de toda la vida", 1, 25, 4,
            new BigDecimal("0.63"), new BigDecimal("205.00"), List.of(), List.of(), null);

    private final String recetaJson = """
            {
              "nombre": "Tortilla", "descripcion": "La de toda la vida",
              "dificultad": 1, "tiempo": 25, "raciones": 4,
              "ingredientes": [ { "ingredienteId": 1, "cantidad": 6 } ],
              "pasos": [ { "numero": 1, "descripcion": "Batir los huevos" } ]
            }
            """;

    @Test
    @DisplayName("Endpoint 1 · GET /api/v1/recetas?nombre=tort → 200 con la página de recetas")
    void endpoint1_catalogo() throws Exception {
        given(recetaService.listar(any(), any(), any(), any())).willReturn(new PageImpl<>(List.of(tortilla)));

        mockMvc.perform(get("/api/v1/recetas").param("nombre", "tort"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nombre").value("Tortilla"));
    }

    @Test
    @DisplayName("Endpoint 2 · GET /api/v1/recetas/1 → 200 con coste y calorías por ración")
    void endpoint2_detalle() throws Exception {
        given(recetaService.buscarPorId(1)).willReturn(tortilla);

        mockMvc.perform(get("/api/v1/recetas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.costePorRacion").value(0.63))
                .andExpect(jsonPath("$.caloriasPorRacion").value(205.00));
    }

    @Test
    @DisplayName("Endpoint 2 · GET /api/v1/recetas/99 → 404 si no existe")
    void endpoint2_noExiste() throws Exception {
        given(recetaService.buscarPorId(99)).willThrow(new RecursoNoEncontradoException("No existe la receta con id 99"));

        mockMvc.perform(get("/api/v1/recetas/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("No existe la receta con id 99"));
    }

    @Test
    @DisplayName("Endpoint 3 · POST /api/v1/recetas → 201 con cabecera Location")
    void endpoint3_alta() throws Exception {
        given(recetaService.crear(any())).willReturn(tortilla);

        mockMvc.perform(post("/api/v1/recetas").contentType(MediaType.APPLICATION_JSON).content(recetaJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Endpoint 3 · POST /api/v1/recetas con pasos con hueco → 422")
    void endpoint3_pasosConHueco() throws Exception {
        given(recetaService.crear(any())).willThrow(new ReglaNegocioException("Los pasos deben ir numerados 1, 2, 3... sin huecos"));

        mockMvc.perform(post("/api/v1/recetas").contentType(MediaType.APPLICATION_JSON).content(recetaJson))
                .andExpect(status().is(422));
    }

    @Test
    @DisplayName("Endpoint 3 · POST /api/v1/recetas con 20 raciones → 400 indicando el campo")
    void endpoint3_racionesNoValidas() throws Exception {
        String json = recetaJson.replace("\"raciones\": 4", "\"raciones\": 20");

        mockMvc.perform(post("/api/v1/recetas").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.raciones").exists());
    }
}
