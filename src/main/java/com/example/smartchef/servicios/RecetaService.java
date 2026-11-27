package com.example.smartchef.servicios;

import com.example.smartchef.dtos.request.RecetaCreacionDto;
import com.example.smartchef.dtos.response.RecetaDetalleResponseDto;

public interface RecetaService {

    RecetaDetalleResponseDto buscarPorId(Integer id);

    RecetaDetalleResponseDto crear(RecetaCreacionDto peticion);
}
