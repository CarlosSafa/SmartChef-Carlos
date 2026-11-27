package com.example.smartchef.controladores;

import com.example.smartchef.dtos.response.InformeRecetasPopularesDto;
import com.example.smartchef.dtos.response.RecetaPopularDto;
import com.example.smartchef.excepciones.ReglaNegocioException;
import com.example.smartchef.servicios.InformeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Prueba el endpoint 16 llamando a su URL
@WebMvcTest(InformeController.class)
class InformeControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private InformeService informeService;

    @Test
    @DisplayName("Endpoint 16 · GET /api/v1/informes/recetas-populares?desde=&hasta= → 200 con los DTO del informe")
    void endpoint16_informe() throws Exception {
        LocalDate desde = LocalDate.of(2026, 11, 1);
        LocalDate hasta = LocalDate.of(2026, 11, 30);
        given(informeService.recetasPopulares(desde, hasta)).willReturn(new InformeRecetasPopularesDto(
                List.of(new RecetaPopularDto(1, "Paella", 5L)),
                List.of(new RecetaPopularDto(2, "Sopa", 3L))));

        mockMvc.perform(get("/api/v1/informes/recetas-populares")
                        .param("desde", "2026-11-01").param("hasta", "2026-11-30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.masCocinadas[0].nombre").value("Paella"))
                .andExpect(jsonPath("$.masCocinadas[0].total").value(5))
                .andExpect(jsonPath("$.masGuardadas[0].nombre").value("Sopa"));
    }

    @Test
    @DisplayName("Endpoint 16 · GET con 'desde' posterior a 'hasta' → 422")
    void endpoint16_fechasAlReves() throws Exception {
        LocalDate desde = LocalDate.of(2026, 11, 30);
        LocalDate hasta = LocalDate.of(2026, 11, 1);
        given(informeService.recetasPopulares(desde, hasta))
                .willThrow(new ReglaNegocioException("La fecha 'desde' no puede ser posterior a 'hasta'"));

        mockMvc.perform(get("/api/v1/informes/recetas-populares")
                        .param("desde", "2026-11-30").param("hasta", "2026-11-01"))
                .andExpect(status().is(422));
    }
}
