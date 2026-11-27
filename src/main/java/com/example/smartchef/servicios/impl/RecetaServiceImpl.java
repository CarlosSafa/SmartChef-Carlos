package com.example.smartchef.servicios.impl;

import com.example.smartchef.dtos.request.RecetaCreacionDto;
import com.example.smartchef.dtos.request.RecetaIngredienteRequestDto;
import com.example.smartchef.dtos.response.RecetaDetalleResponseDto;
import com.example.smartchef.dtos.response.RecetaIngredienteResponseDto;
import com.example.smartchef.excepciones.RecursoNoEncontradoException;
import com.example.smartchef.excepciones.ReglaNegocioException;
import com.example.smartchef.mappers.RecetaIngredienteMapper;
import com.example.smartchef.mappers.RecetaMapper;
import com.example.smartchef.modelos.Ingrediente;
import com.example.smartchef.modelos.Paso;
import com.example.smartchef.modelos.Receta;
import com.example.smartchef.modelos.RecetaIngrediente;
import com.example.smartchef.modelos.RecetaIngredienteId;
import com.example.smartchef.repositorios.IngredienteRepository;
import com.example.smartchef.repositorios.RecetaRepository;
import com.example.smartchef.repositorios.RecetaSpecifications;
import com.example.smartchef.servicios.RecetaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    private final RecetaMapper recetaMapper;
    private final RecetaIngredienteMapper recetaIngredienteMapper;

    @Override
    public Page<RecetaDetalleResponseDto> listar(String nombre, Integer dificultad, Integer tiempoMax, Pageable pageable) {
        // Q7: se empieza con "todas" y se añade cada filtro solo si viene en la petición
        Specification<Receta> filtro = RecetaSpecifications.todas();
        if (nombre != null && !nombre.isBlank()) {
            filtro = filtro.and(RecetaSpecifications.nombreContiene(nombre));
        }
        if (dificultad != null) {
            filtro = filtro.and(RecetaSpecifications.dificultadIgual(dificultad));
        }
        if (tiempoMax != null) {
            filtro = filtro.and(RecetaSpecifications.tiempoMaximo(tiempoMax));
        }

        Page<Receta> pagina = recetaRepository.findAll(filtro, pageable);
        return pagina.map(recetaMapper::aDetalleDto);
    }

    @Override
    public RecetaDetalleResponseDto buscarPorId(Integer id) {
        Receta receta = recetaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la receta con id " + id));

        List<RecetaIngredienteResponseDto> ingredientes = new ArrayList<>();
        BigDecimal caloriasTotales = BigDecimal.ZERO;
        BigDecimal costeTotal = BigDecimal.ZERO;

        // RN-02: calorías y coste salen de los ingredientes (cantidad x valor por unidad)
        for (RecetaIngrediente linea : receta.getIngredientes()) {
            Ingrediente ingrediente = linea.getIngrediente();

            BigDecimal calorias = BigDecimal.ZERO;
            if (ingrediente.getCalorias() != null) {
                calorias = linea.getCantidad().multiply(ingrediente.getCalorias()).setScale(2, RoundingMode.HALF_UP);
            }

            BigDecimal coste = BigDecimal.ZERO;
            if (ingrediente.getPrecio() != null) {
                coste = linea.getCantidad().multiply(ingrediente.getPrecio()).setScale(2, RoundingMode.HALF_UP);
            }

            caloriasTotales = caloriasTotales.add(calorias);
            costeTotal = costeTotal.add(coste);
            ingredientes.add(recetaIngredienteMapper.aResponseDto(linea, calorias, coste));
        }

        BigDecimal raciones = BigDecimal.ONE;
        if (receta.getRaciones() != null && receta.getRaciones() > 0) {
            raciones = BigDecimal.valueOf(receta.getRaciones());
        }

        BigDecimal caloriasPorRacion = caloriasTotales.divide(raciones, 2, RoundingMode.HALF_UP);
        BigDecimal costePorRacion = costeTotal.divide(raciones, 2, RoundingMode.HALF_UP);

        return recetaMapper.aDetalleCompleto(receta, ingredientes, costePorRacion, caloriasPorRacion);
    }
    @Override
    @Transactional
    public RecetaDetalleResponseDto crear(RecetaCreacionDto peticion) {
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

        // Crear la receta con sus pasos
        Receta receta = recetaMapper.aEntidad(peticion);
        for (Paso paso : receta.getPasos()) {
            paso.setReceta(receta);
        }
        Receta guardada = recetaRepository.save(receta);

        // Añadir los ingredientes con su cantidad
        for (RecetaIngredienteRequestDto linea : peticion.ingredientes()) {
            Ingrediente ingrediente = ingredienteRepository.findById(linea.ingredienteId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("No existe el ingrediente con id " + linea.ingredienteId()));

            RecetaIngrediente recetaIngrediente = new RecetaIngrediente();
            recetaIngrediente.setId(new RecetaIngredienteId(guardada.getId(), ingrediente.getId()));
            recetaIngrediente.setReceta(guardada);
            recetaIngrediente.setIngrediente(ingrediente);
            recetaIngrediente.setCantidad(linea.cantidad());
            guardada.getIngredientes().add(recetaIngrediente);
        }

        log.info("Receta creada: id={}, nombre={}", guardada.getId(), guardada.getNombre());
        return buscarPorId(guardada.getId());
    }
}
