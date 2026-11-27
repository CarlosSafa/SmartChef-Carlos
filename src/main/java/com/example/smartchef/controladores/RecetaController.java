package com.example.smartchef.controladores;

import com.example.smartchef.dtos.request.RecetaCreacionDto;
import com.example.smartchef.dtos.response.RecetaDetalleResponseDto;
import com.example.smartchef.modelos.Dificultad;
import com.example.smartchef.servicios.RecetaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/recetas")
@RequiredArgsConstructor
public class RecetaController {

    private final RecetaService recetaService;

    // 1. GET /api/v1/recetas?nombre=tort&dificultad=FACIL&tiempoMax=30&page=0&size=10  (público)
    @GetMapping
    public Page<RecetaDetalleResponseDto> listar(@RequestParam(required = false) String nombre,
                                                 @RequestParam(required = false) Dificultad dificultad,
                                                 @RequestParam(required = false) Integer tiempoMax,
                                                 @PageableDefault(size = 10) Pageable pageable) {
        return recetaService.listar(nombre, dificultad, tiempoMax, pageable);
    }

    // 2. GET /api/v1/recetas/3  (público)
    @GetMapping("/{id}")
    public RecetaDetalleResponseDto buscar(@PathVariable Integer id) {
        return recetaService.buscarPorId(id);
    }

    // 3. POST /api/v1/recetas?usuarioId=2  (CHEF; usuarioId es el autor, provisional hasta el login de la Fase 4)
    @PostMapping
    public ResponseEntity<RecetaDetalleResponseDto> crear(@RequestParam Integer usuarioId,
                                                          @Valid @RequestBody RecetaCreacionDto peticion) {
        RecetaDetalleResponseDto creada = recetaService.crear(usuarioId, peticion);

        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creada.id())
                .toUri();

        return ResponseEntity.created(ubicacion).body(creada);
    }
}
