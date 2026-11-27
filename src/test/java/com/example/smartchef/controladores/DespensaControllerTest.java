package com.example.smartchef.controladores;

import com.example.smartchef.dtos.response.DespensaResponseDto;
import com.example.smartchef.excepciones.RecursoNoEncontradoException;
import com.example.smartchef.servicios.DespensaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Prueba los endpoints 8 y 9 llamando a su URL
@WebMvcTest(DespensaController.class)
class DespensaControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private DespensaService despensaService;

    private final DespensaResponseDto tomates =
            new DespensaResponseDto(1, 5, "Tomate", new BigDecimal("500"), LocalDate.of(2026, 12, 5));

    @Test
    @DisplayName("Endpoint 8 · GET /api/v1/despensa?usuarioId=1 → 200 con la despensa")
    void endpoint8_miDespensa() throws Exception {
        given(despensaService.listar(1)).willReturn(List.of(tomates));

        mockMvc.perform(get("/api/v1/despensa").param("usuarioId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ingredienteNombre").value("Tomate"));
    }

    @Test
    @DisplayName("Endpoint 9 · PUT /api/v1/despensa/5?usuarioId=1 → 200 con lo guardado")
    void endpoint9_guardar() throws Exception {
        given(despensaService.guardar(eq(1), eq(5), any())).willReturn(tomates);

        mockMvc.perform(put("/api/v1/despensa/5").param("usuarioId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "cantidad": 500, "caducidad": "2026-12-05" }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad").value(500));
    }

    @Test
    @DisplayName("Endpoint 9 · PUT con cantidad 0 → 204 (se quita de la despensa)")
    void endpoint9_cantidadCero() throws Exception {
        given(despensaService.guardar(eq(1), eq(5), any())).willReturn(null);

        mockMvc.perform(put("/api/v1/despensa/5").param("usuarioId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "cantidad": 0 }
                                """))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Endpoint 9 · PUT con cantidad negativa → 400")
    void endpoint9_cantidadNegativa() throws Exception {
        mockMvc.perform(put("/api/v1/despensa/5").param("usuarioId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "cantidad": -5 }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.cantidad").exists());
    }

    @Test
    @DisplayName("Endpoint 9 · PUT con un ingrediente que no existe → 404")
    void endpoint9_ingredienteNoExiste() throws Exception {
        given(despensaService.guardar(eq(1), eq(9999), any()))
                .willThrow(new RecursoNoEncontradoException("No existe el ingrediente con id 9999"));

        mockMvc.perform(put("/api/v1/despensa/9999").param("usuarioId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "cantidad": 1 }
                                """))
                .andExpect(status().isNotFound());
    }
}
