package com.example.smartchef.controladores;

import com.example.smartchef.dtos.response.InformeRecetasPopularesDto;
import com.example.smartchef.servicios.InformeService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/informes")
@RequiredArgsConstructor
public class InformeController {

    private final InformeService informeService;

    // 16. GET /api/v1/informes/recetas-populares?desde=2026-11-01&hasta=2026-11-30  (ADMIN, CHEF; seguridad en Fase 4)
    @GetMapping("/recetas-populares")
    public InformeRecetasPopularesDto recetasPopulares(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return informeService.recetasPopulares(desde, hasta);
    }
}
