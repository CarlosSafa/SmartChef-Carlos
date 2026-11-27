package com.example.smartchef.controladores;

import com.example.smartchef.dtos.response.UsuarioRecetaResponseDto;
import com.example.smartchef.excepciones.ConflictoException;
import com.example.smartchef.servicios.RegistroService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Prueba el endpoint 14 llamando a su URL
@WebMvcTest(RegistroController.class)
class RegistroControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private RegistroService registroService;

    private final String cenaJson = """
            { "recetaId": 3, "tipo": 3 }
            """;

    @Test
    @DisplayName("Endpoint 14 · POST /api/v1/registros?usuarioId=1 → 201")
    void endpoint14_registrar() throws Exception {
        given(registroService.registrar(eq(1), any()))
                .willReturn(new UsuarioRecetaResponseDto(10, 1, 3, "Gazpacho", LocalDateTime.now(), 3));

        mockMvc.perform(post("/api/v1/registros").param("usuarioId", "1")
                        .contentType(MediaType.APPLICATION_JSON).content(cenaJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.recetaNombre").value("Gazpacho"));
    }

    @Test
    @DisplayName("Endpoint 14 · POST con una cena ya registrada hoy → 409")
    void endpoint14_duplicado() throws Exception {
        given(registroService.registrar(eq(1), any()))
                .willThrow(new ConflictoException("Ya tienes un registro hoy para ese momento del día"));

        mockMvc.perform(post("/api/v1/registros").param("usuarioId", "1")
                        .contentType(MediaType.APPLICATION_JSON).content(cenaJson))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Endpoint 14 · POST con un momento que no es 1, 2 ni 3 → 400")
    void endpoint14_momentoNoValido() throws Exception {
        mockMvc.perform(post("/api/v1/registros").param("usuarioId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "recetaId": 3, "tipo": 5 }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.tipo").exists());
    }
}
