package com.example.smartchef.controladores;

import com.example.smartchef.dtos.response.RegistroComidaResponseDto;
import com.example.smartchef.excepciones.ConflictoException;
import com.example.smartchef.modelos.Momento;
import com.example.smartchef.servicios.RegistroService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

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
            { "recetaId": 3, "fecha": "%s", "momento": "CENA" }
            """.formatted(LocalDate.now());

    @Test
    @DisplayName("Endpoint 14 · POST /api/v1/registros?usuarioId=5 → 201")
    void endpoint14_registrar() throws Exception {
        given(registroService.registrar(eq(5), any()))
                .willReturn(new RegistroComidaResponseDto(10, 5, 3, "Ensalada de tomate", null, LocalDate.now(), Momento.CENA));

        mockMvc.perform(post("/api/v1/registros").param("usuarioId", "5")
                        .contentType(MediaType.APPLICATION_JSON).content(cenaJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.recetaNombre").value("Ensalada de tomate"))
                .andExpect(jsonPath("$.momento").value("CENA"));
    }

    @Test
    @DisplayName("Endpoint 14 · POST con una cena ya registrada ese día → 409")
    void endpoint14_duplicado() throws Exception {
        given(registroService.registrar(eq(5), any()))
                .willThrow(new ConflictoException("Ya tienes un registro ese día en CENA"));

        mockMvc.perform(post("/api/v1/registros").param("usuarioId", "5")
                        .contentType(MediaType.APPLICATION_JSON).content(cenaJson))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Endpoint 14 · POST con fecha futura → 400 indicando el campo (RN-07)")
    void endpoint14_fechaFutura() throws Exception {
        String json = """
                { "recetaId": 3, "fecha": "%s", "momento": "CENA" }
                """.formatted(LocalDate.now().plusDays(1));

        mockMvc.perform(post("/api/v1/registros").param("usuarioId", "5")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.fecha").exists());
    }
}
