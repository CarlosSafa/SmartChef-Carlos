package com.example.smartchef.controladores;

import com.example.smartchef.dtos.request.RegistroComidaRequestDto;
import com.example.smartchef.dtos.response.RegistroComidaResponseDto;
import com.example.smartchef.dtos.response.ResumenDiaDto;
import com.example.smartchef.servicios.RegistroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/registros")
@RequiredArgsConstructor
public class RegistroController {

    private final RegistroService registroService;

    // 14. POST /api/v1/registros?usuarioId=1
    // (usuarioId es provisional: en la Fase 4 saldrá del login)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegistroComidaResponseDto registrar(@RequestParam Integer usuarioId,
                                              @Valid @RequestBody RegistroComidaRequestDto peticion) {
        return registroService.registrar(usuarioId, peticion);
    }

    // 15. GET /api/v1/registros/semana?usuarioId=5&fecha=2026-11-27  (sin fecha: la semana de hoy)
    @GetMapping("/semana")
    public List<ResumenDiaDto> semana(@RequestParam Integer usuarioId,
                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        if (fecha == null) {
            fecha = LocalDate.now();
        }
        return registroService.semana(usuarioId, fecha);
    }
}
