package com.example.smartchef.servicios;

import com.example.smartchef.dtos.request.RegistroComidaRequestDto;
import com.example.smartchef.dtos.response.RegistroComidaResponseDto;
import com.example.smartchef.dtos.response.ResumenDiaDto;

import java.time.LocalDate;
import java.util.List;

public interface    RegistroService {

    RegistroComidaResponseDto registrar(Integer usuarioId, RegistroComidaRequestDto peticion);

    List<ResumenDiaDto> semana(Integer usuarioId, LocalDate fecha);
}
