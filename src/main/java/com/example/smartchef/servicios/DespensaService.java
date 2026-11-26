package com.example.smartchef.servicios;

import com.example.smartchef.dtos.request.DespensaRequestDto;
import com.example.smartchef.dtos.response.DespensaResponseDto;

import java.util.List;

public interface DespensaService {

    List<DespensaResponseDto> listar(Integer usuarioId);

    DespensaResponseDto guardar(Integer usuarioId, Integer ingredienteId, DespensaRequestDto peticion);
}
