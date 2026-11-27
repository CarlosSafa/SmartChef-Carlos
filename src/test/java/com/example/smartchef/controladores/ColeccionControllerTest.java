package com.example.smartchef.controladores;

import com.example.smartchef.dtos.response.ColeccionResponseDto;
import com.example.smartchef.excepciones.ConflictoException;
import com.example.smartchef.servicios.ColeccionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Prueba los endpoints 12 y 13 llamando a su URL
@WebMvcTest(ColeccionController.class)
class ColeccionControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private ColeccionService coleccionService;

    private final ColeccionResponseDto favoritas = new ColeccionResponseDto(1, "Favoritas", 1, List.of());

    @Test
    @DisplayName("Endpoint 12 · GET /api/v1/colecciones?usuarioId=1 → 200 con mis colecciones")
    void endpoint12_misColecciones() throws Exception {
        given(coleccionService.listar(1)).willReturn(List.of(favoritas));

        mockMvc.perform(get("/api/v1/colecciones").param("usuarioId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Favoritas"));
    }

    @Test
    @DisplayName("Endpoint 13 · PUT /api/v1/colecciones/Favoritas/recetas/3?usuarioId=1 → 200")
    void endpoint13_guardarReceta() throws Exception {
        given(coleccionService.guardarReceta(1, "Favoritas", 3)).willReturn(favoritas);

        mockMvc.perform(put("/api/v1/colecciones/Favoritas/recetas/3").param("usuarioId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Favoritas"));
    }

    @Test
    @DisplayName("Endpoint 13 · PUT con una receta que ya está en la colección → 409")
    void endpoint13_recetaRepetida() throws Exception {
        given(coleccionService.guardarReceta(1, "Favoritas", 3))
                .willThrow(new ConflictoException("La receta 3 ya está en la colección Favoritas"));

        mockMvc.perform(put("/api/v1/colecciones/Favoritas/recetas/3").param("usuarioId", "1"))
                .andExpect(status().isConflict());
    }
}
