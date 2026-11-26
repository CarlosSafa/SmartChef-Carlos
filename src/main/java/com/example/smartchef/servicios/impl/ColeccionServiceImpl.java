package com.example.smartchef.servicios.impl;

import com.example.smartchef.dtos.response.ColeccionResponseDto;
import com.example.smartchef.dtos.response.RecetaDetalleResponseDto;
import com.example.smartchef.excepciones.ConflictoException;
import com.example.smartchef.excepciones.RecursoNoEncontradoException;
import com.example.smartchef.mappers.RecetaMapper;
import com.example.smartchef.modelos.Coleccion;
import com.example.smartchef.modelos.ColeccionReceta;
import com.example.smartchef.modelos.ColeccionRecetaId;
import com.example.smartchef.modelos.Receta;
import com.example.smartchef.modelos.Usuario;
import com.example.smartchef.repositorios.ColeccionRecetaRepository;
import com.example.smartchef.repositorios.ColeccionRepository;
import com.example.smartchef.repositorios.RecetaRepository;
import com.example.smartchef.repositorios.UsuarioRepository;
import com.example.smartchef.servicios.ColeccionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ColeccionServiceImpl implements ColeccionService {

    private final ColeccionRepository coleccionRepository;
    private final ColeccionRecetaRepository coleccionRecetaRepository;
    private final UsuarioRepository usuarioRepository;
    private final RecetaRepository recetaRepository;
    private final RecetaMapper recetaMapper;

    @Override
    public List<ColeccionResponseDto> listar(Integer usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RecursoNoEncontradoException("No existe el usuario con id " + usuarioId);
        }
        List<Coleccion> colecciones = coleccionRepository.findByUsuarioId(usuarioId);

        List<ColeccionResponseDto> resultado = new ArrayList<>();
        for (Coleccion coleccion : colecciones) {
            resultado.add(aResponseDto(coleccion));
        }
        return resultado;
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
            coleccion = coleccionRepository.save(coleccion);
            log.info("Colección '{}' creada para el usuario {}", nombre, usuarioId);
        }

        ColeccionRecetaId id = new ColeccionRecetaId(coleccion.getId(), recetaId);
        if (coleccionRecetaRepository.existsById(id)) {
            throw new ConflictoException("La receta " + recetaId + " ya está en la colección " + nombre);
        }

        ColeccionReceta coleccionReceta = new ColeccionReceta();
        coleccionReceta.setId(id);
        coleccionReceta.setColeccion(coleccion);
        coleccionReceta.setReceta(receta);
        coleccionRecetaRepository.save(coleccionReceta);

        log.info("Receta {} guardada en la colección '{}' del usuario {}", recetaId, nombre, usuarioId);
        return aResponseDto(coleccion);
    }

    private ColeccionResponseDto aResponseDto(Coleccion coleccion) {
        List<ColeccionReceta> recetasDeLaColeccion = coleccionRecetaRepository.findByColeccionId(coleccion.getId());

        List<RecetaDetalleResponseDto> recetas = new ArrayList<>();
        for (ColeccionReceta coleccionReceta : recetasDeLaColeccion) {
            recetas.add(recetaMapper.aDetalleDto(coleccionReceta.getReceta()));
        }

        return new ColeccionResponseDto(
                coleccion.getId(),
                coleccion.getNombre(),
                coleccion.getUsuario().getId(),
                recetas);
    }
}
