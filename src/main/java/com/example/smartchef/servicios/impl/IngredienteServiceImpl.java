package com.example.smartchef.servicios.impl;

import com.example.smartchef.dtos.request.IngredienteRequestDto;
import com.example.smartchef.dtos.response.IngredienteResponseDto;
import com.example.smartchef.excepciones.ConflictoException;
import com.example.smartchef.excepciones.RecursoNoEncontradoException;
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
        Page<Ingrediente> pagina = (texto == null || texto.isBlank())
                ? ingredienteRepository.findAll(pageable)
                : ingredienteRepository.findByNombreContainingIgnoreCase(texto.trim(), pageable);

        // map() de Page convierte el contenido sin perder el total ni la página
        return pagina.map(ingredienteMapper::aResponseDto);
    }

    @Override
    public IngredienteResponseDto buscarPorId(Integer id) {
        Ingrediente ingrediente = ingredienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el ingrediente con id " + id));
        return ingredienteMapper.aResponseDto(ingrediente);
    }

    @Override
    @Transactional
    public IngredienteResponseDto crear(IngredienteRequestDto peticion) {
        // Catálogo común: "el tomate es siempre el mismo tomate", así que no se repiten nombres
        String nombre = peticion.nombre().trim();
        if (ingredienteRepository.existsByNombreIgnoreCase(nombre)) {
            throw new ConflictoException("Ya existe un ingrediente con el nombre " + nombre);
        }

        Ingrediente ingrediente = ingredienteMapper.aEntidad(peticion);
        ingrediente.setNombre(nombre);
        Ingrediente guardado = ingredienteRepository.save(ingrediente);

        log.info("Ingrediente creado: id={}, nombre={}", guardado.getId(), guardado.getNombre());
        return ingredienteMapper.aResponseDto(guardado);
    }
}
