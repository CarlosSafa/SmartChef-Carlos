package com.example.smartchef.servicios.impl;

import com.example.smartchef.dtos.response.IngredienteResponseDto;
import com.example.smartchef.mappers.IngredienteMapper;
import com.example.smartchef.modelos.Ingrediente;
import com.example.smartchef.repositorios.IngredienteRepository;
import com.example.smartchef.servicios.IngredienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class IngredienteServiceImpl implements IngredienteService {

    private final IngredienteRepository ingredienteRepository;
    private final IngredienteMapper ingredienteMapper;

    @Override
    public Page<IngredienteResponseDto> listar(String texto, Pageable pageable) {
        Page<Ingrediente> pagina;
        if (texto == null || texto.isBlank()) {
            pagina = ingredienteRepository.findAll(pageable);
        } else {
            pagina = ingredienteRepository.findByNombreContainingIgnoreCase(texto, pageable);
        }

        // map() de Page convierte el contenido sin perder el total ni la página (manual, apartado 11.2)
        return pagina.map(ingredienteMapper::aResponseDto);
    }
}
