package com.example.smartchef.controladores;

import com.example.smartchef.dtos.request.IngredienteRequestDto;
import com.example.smartchef.dtos.response.IngredienteResponseDto;
import com.example.smartchef.servicios.IngredienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/ingredientes")
@RequiredArgsConstructor
public class IngredienteController {

    private final IngredienteService ingredienteService;

    // 6. GET /api/v1/ingredientes?texto=tom&page=0&size=10&sort=nombre,asc  (público)
    @GetMapping
    public Page<IngredienteResponseDto> listar(
            @RequestParam(required = false) String texto,
            @PageableDefault(size = 10, sort = "nombre", direction = Sort.Direction.ASC) Pageable pageable) {
        return ingredienteService.listar(texto, pageable);
    }

    // GET /api/v1/ingredientes/5
    @GetMapping("/{id}")
    public IngredienteResponseDto buscar(@PathVariable Integer id) {
        return ingredienteService.buscarPorId(id);
    }

    // POST /api/v1/ingredientes  (CHEF y ADMIN; la seguridad se añade en la Fase 4)
    @PostMapping
    public ResponseEntity<IngredienteResponseDto> crear(@Valid @RequestBody IngredienteRequestDto peticion) {
        IngredienteResponseDto creado = ingredienteService.crear(peticion);

        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.id())
                .toUri();

        return ResponseEntity.created(ubicacion).body(creado);   // 201 + cabecera Location
    }
}
