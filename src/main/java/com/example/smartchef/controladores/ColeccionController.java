package com.example.smartchef.controladores;

import com.example.smartchef.dtos.response.ColeccionResponseDto;
import com.example.smartchef.servicios.ColeccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/colecciones")
@RequiredArgsConstructor
public class ColeccionController {

    private final ColeccionService coleccionService;

    // 12. GET /api/v1/colecciones?usuarioId=1
    // (usuarioId es provisional: en la Fase 4 saldrá del login)
    @GetMapping
    public List<ColeccionResponseDto> listar(@RequestParam Integer usuarioId) {
        return coleccionService.listar(usuarioId);
    }

    // 13. PUT /api/v1/colecciones/Cenas rápidas/recetas/3?usuarioId=1
    @PutMapping("/{nombre}/recetas/{recetaId}")
    public ColeccionResponseDto guardarReceta(@PathVariable String nombre,
                                              @PathVariable Integer recetaId,
                                              @RequestParam Integer usuarioId) {
        return coleccionService.guardarReceta(usuarioId, nombre, recetaId);
    }
}
