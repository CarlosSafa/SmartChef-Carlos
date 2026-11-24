package com.example.smartchef.servicios;

import com.example.smartchef.dtos.request.IngredienteRequestDto;
import com.example.smartchef.dtos.response.IngredienteResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IngredienteService {

    // Endpoint 6: catálogo de ingredientes paginado, con filtro opcional por texto
    Page<IngredienteResponseDto> listar(String texto, Pageable pageable);

    IngredienteResponseDto buscarPorId(Integer id);

    // Los CHEF y ADMIN pueden añadir ingredientes al catálogo común
    IngredienteResponseDto crear(IngredienteRequestDto peticion);
}
