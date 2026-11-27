package com.example.smartchef.servicios;

import com.example.smartchef.dtos.request.RecetaCreacionDto;
import com.example.smartchef.dtos.response.RecetaDetalleResponseDto;
import com.example.smartchef.modelos.Dificultad;
import com.example.smartchef.repositorios.RecetaTarjetaView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecetaService {

    Page<RecetaTarjetaView> listar(String nombre, List<Dificultad> dificultades, Integer tiempoMax, Pageable pageable);

    RecetaDetalleResponseDto buscarPorId(Integer id);

    // autorId: el chef que la publica (provisional hasta el login de la Fase 4)
    RecetaDetalleResponseDto crear(Integer autorId, RecetaCreacionDto peticion);
}
