package com.example.smartchef.servicios;

import com.example.smartchef.dtos.request.RegistroComidaRequestDto;
import com.example.smartchef.dtos.response.RegistroComidaResponseDto;

public interface RegistroService {

    RegistroComidaResponseDto registrar(Integer usuarioId, RegistroComidaRequestDto peticion);
}
