package com.example.smartchef.servicios.impl;

import com.example.smartchef.dtos.response.ColeccionResponseDto;
import com.example.smartchef.excepciones.ConflictoException;
import com.example.smartchef.excepciones.RecursoNoEncontradoException;
import com.example.smartchef.mappers.ColeccionMapper;
import com.example.smartchef.modelos.Coleccion;
import com.example.smartchef.modelos.Receta;
import com.example.smartchef.modelos.Usuario;
import com.example.smartchef.repositorios.ColeccionRepository;
import com.example.smartchef.repositorios.RecetaRepository;
import com.example.smartchef.repositorios.UsuarioRepository;
import com.example.smartchef.servicios.ColeccionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ColeccionServiceImpl implements ColeccionService {

    private final ColeccionRepository coleccionRepository;
    private final UsuarioRepository usuarioRepository;
    private final RecetaRepository recetaRepository;
    private final ColeccionMapper coleccionMapper;

    @Override
    public List<ColeccionResponseDto> listar(Integer usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RecursoNoEncontradoException("No existe el usuario con id " + usuarioId);
        }
        List<Coleccion> colecciones = coleccionRepository.findByUsuarioId(usuarioId);
        return coleccionMapper.aResponseDtoList(colecciones);
    }

    @Override
    @Transactional
    public ColeccionResponseDto guardarReceta(Integer usuarioId, String nombre, Integer recetaId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el usuario con id " + usuarioId));
        Receta receta = recetaRepository.findById(recetaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la receta con id " + recetaId));

        Coleccion coleccion = coleccionRepository.findByUsuarioIdAndNombreIgnoreCase(usuarioId, nombre)
                .orElse(null);

        if (coleccion == null) {
            coleccion = new Coleccion();
            coleccion.setNombre(nombre);
            coleccion.setUsuario(usuario);
            log.info("Colección '{}' creada para el usuario {}", nombre, usuarioId);
        }

        // RN-08: sin recetas repetidas en una colección
        if (coleccion.getRecetas().contains(receta)) {
            throw new ConflictoException("La receta " + recetaId + " ya está en la colección " + nombre);
        }

        coleccion.getRecetas().add(receta);
        Coleccion guardada = coleccionRepository.save(coleccion);

        log.info("Receta {} guardada en la colección '{}' del usuario {}", recetaId, nombre, usuarioId);
        return coleccionMapper.aResponseDto(guardada);
    }
}
