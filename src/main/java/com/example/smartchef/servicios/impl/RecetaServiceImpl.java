package com.example.smartchef.servicios.impl;

import com.example.smartchef.dtos.request.RecetaCreacionDto;
import com.example.smartchef.dtos.request.RecetaIngredienteRequestDto;
import com.example.smartchef.dtos.response.RecetaDetalleResponseDto;
import com.example.smartchef.excepciones.RecursoNoEncontradoException;
import com.example.smartchef.excepciones.ReglaNegocioException;
import com.example.smartchef.mappers.RecetaMapper;
import com.example.smartchef.modelos.Dificultad;
import com.example.smartchef.modelos.Ingrediente;
import com.example.smartchef.modelos.Paso;
import com.example.smartchef.modelos.Receta;
import com.example.smartchef.modelos.RecetaIngrediente;
import com.example.smartchef.modelos.Usuario;
import com.example.smartchef.repositorios.IngredienteRepository;
import com.example.smartchef.repositorios.RecetaRepository;
import com.example.smartchef.repositorios.RecetaTarjetaView;
import com.example.smartchef.repositorios.UsuarioRepository;
import com.example.smartchef.servicios.RecetaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RecetaServiceImpl implements RecetaService {

    private final RecetaRepository recetaRepository;
    private final IngredienteRepository ingredienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final RecetaMapper recetaMapper;

    @Override
    public Page<RecetaTarjetaView> listar(String nombre, List<Dificultad> dificultades, Integer tiempoMax, Pageable pageable) {
        return recetaRepository.findByActivaTrueAndNombreContainingIgnoreCaseAndDificultadInAndTiempoLessThanEqual(
                nombre, dificultades, tiempoMax, pageable);
    }

    @Override
    public RecetaDetalleResponseDto buscarPorId(Integer id) {
        Receta receta = recetaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la receta con id " + id));

        // RN-02: calorías y coste por ración salen de los ingredientes (las sumas las hace la base de datos)
        BigDecimal raciones = BigDecimal.valueOf(receta.getRaciones());
        BigDecimal caloriasPorRacion = recetaRepository.sumarCalorias(id).divide(raciones, 2, RoundingMode.HALF_UP);
        BigDecimal costePorRacion = recetaRepository.sumarCoste(id).divide(raciones, 2, RoundingMode.HALF_UP);

        return recetaMapper.aDetalleCompleto(receta, costePorRacion, caloriasPorRacion);
    }
    @Override
    @Transactional
    public RecetaDetalleResponseDto crear(Integer autorId, RecetaCreacionDto peticion) {
        // RN-01: los pasos van numerados 1, 2, 3... sin huecos
        for (int i = 0; i < peticion.pasos().size(); i++) {
            int numeroEsperado = i + 1;
            if (peticion.pasos().get(i).numero() != numeroEsperado) {
                throw new ReglaNegocioException("Los pasos deben ir numerados 1, 2, 3... sin huecos");
            }
        }

        // No se puede repetir un ingrediente en la misma receta
        List<Integer> idsVistos = new ArrayList<>();
        for (RecetaIngredienteRequestDto linea : peticion.ingredientes()) {
            if (idsVistos.contains(linea.ingredienteId())) {
                throw new ReglaNegocioException("El ingrediente " + linea.ingredienteId() + " está repetido");
            }
            idsVistos.add(linea.ingredienteId());
        }

        Usuario autor = usuarioRepository.findById(autorId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el usuario con id " + autorId));

        // Crear la receta con sus pasos
        Receta receta = recetaMapper.aEntidad(peticion);
        receta.setAutor(autor);
        for (Paso paso : receta.getPasos()) {
            paso.setReceta(receta);
        }
        Receta guardada = recetaRepository.save(receta);

        // Añadir los ingredientes con su cantidad
        for (RecetaIngredienteRequestDto linea : peticion.ingredientes()) {
            Ingrediente ingrediente = ingredienteRepository.findById(linea.ingredienteId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("No existe el ingrediente con id " + linea.ingredienteId()));

            RecetaIngrediente recetaIngrediente = new RecetaIngrediente();
            recetaIngrediente.setReceta(guardada);
            recetaIngrediente.setIngrediente(ingrediente);
            recetaIngrediente.setCantidad(linea.cantidad());
            guardada.getIngredientes().add(recetaIngrediente);
        }

        log.info("Receta creada: id={}, nombre={}", guardada.getId(), guardada.getNombre());
        return buscarPorId(guardada.getId());
    }
}
