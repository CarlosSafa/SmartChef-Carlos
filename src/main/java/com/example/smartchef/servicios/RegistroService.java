package com.example.smartchef.servicios;

import com.example.smartchef.dtos.request.RegistroComidaRequestDto;
import com.example.smartchef.dtos.response.RegistroComidaResponseDto;
import com.example.smartchef.dtos.response.SemanaResponseDto;

import java.time.LocalDate;

public interface RegistroService {

    RegistroComidaResponseDto registrar(Integer usuarioId, RegistroComidaRequestDto peticion);

    SemanaResponseDto semana(Integer usuarioId, LocalDate fecha);
}
