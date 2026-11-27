package com.example.smartchef.servicios;

import com.example.smartchef.dtos.request.RecetaCreacionDto;
import com.example.smartchef.dtos.response.RecetaDetalleResponseDto;
import com.example.smartchef.modelos.Dificultad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecetaService {

    Page<RecetaDetalleResponseDto> listar(String nombre, Dificultad dificultad, Integer tiempoMax, Pageable pageable);

    RecetaDetalleResponseDto buscarPorId(Integer id);

    // autorId: el chef que la publica (provisional hasta el login de la Fase 4)
    RecetaDetalleResponseDto crear(Integer autorId, RecetaCreacionDto peticion);
}
