package com.example.smartchef.controladores;

import com.example.smartchef.dtos.response.RecetaDetalleResponseDto;
import com.example.smartchef.servicios.RecetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recetas")
@RequiredArgsConstructor
public class RecetaController {

    private final RecetaService recetaService;

    // 2. GET /api/v1/recetas/3  (público)
    @GetMapping("/{id}")
    public RecetaDetalleResponseDto buscar(@PathVariable Integer id) {
        return recetaService.buscarPorId(id);
    }
}
