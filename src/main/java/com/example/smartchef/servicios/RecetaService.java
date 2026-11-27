package com.example.smartchef.servicios;

import com.example.smartchef.dtos.request.RecetaCreacionDto;
import com.example.smartchef.dtos.response.RecetaDetalleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecetaService {

    Page<RecetaDetalleResponseDto> listar(String nombre, Integer dificultad, Integer tiempoMax, Pageable pageable);

    RecetaDetalleResponseDto buscarPorId(Integer id);

    RecetaDetalleResponseDto crear(RecetaCreacionDto peticion);
}
