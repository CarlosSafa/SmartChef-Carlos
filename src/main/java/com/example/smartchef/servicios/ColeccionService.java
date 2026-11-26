package com.example.smartchef.servicios;

import com.example.smartchef.dtos.response.ColeccionResponseDto;

import java.util.List;

public interface ColeccionService {

    List<ColeccionResponseDto> listar(Integer usuarioId);

    ColeccionResponseDto guardarReceta(Integer usuarioId, String nombre, Integer recetaId);
}
