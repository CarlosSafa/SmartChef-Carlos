package com.example.smartchef.controladores;

import com.example.smartchef.dtos.response.IngredienteResponseDto;
import com.example.smartchef.servicios.IngredienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ingredientes")
@RequiredArgsConstructor
public class IngredienteController {

    private final IngredienteService ingredienteService;

    // 6. GET /api/v1/ingredientes?texto=tom&page=0&size=10  (público)
    @GetMapping
    public Page<IngredienteResponseDto> listar(
            @RequestParam(required = false) String texto,
            @PageableDefault(size = 10) Pageable pageable) {
        return ingredienteService.listar(texto, pageable);
    }
}
