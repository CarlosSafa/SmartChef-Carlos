package com.example.smartchef.servicios;

import com.example.smartchef.dtos.request.UsuarioRecetaRequestDto;
import com.example.smartchef.dtos.response.UsuarioRecetaResponseDto;

public interface RegistroService {

    UsuarioRecetaResponseDto registrar(Integer usuarioId, UsuarioRecetaRequestDto peticion);
}
