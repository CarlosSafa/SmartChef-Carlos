package com.example.smartchef.servicios.impl;

import com.example.smartchef.dtos.response.RecetaDetalleResponseDto;
import com.example.smartchef.dtos.response.RecetaIngredienteResponseDto;
import com.example.smartchef.excepciones.RecursoNoEncontradoException;
import com.example.smartchef.mappers.RecetaIngredienteMapper;
import com.example.smartchef.mappers.RecetaMapper;
import com.example.smartchef.modelos.Ingrediente;
import com.example.smartchef.modelos.Receta;
import com.example.smartchef.modelos.RecetaIngrediente;
import com.example.smartchef.repositorios.RecetaRepository;
import com.example.smartchef.servicios.RecetaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final RecetaMapper recetaMapper;
    private final RecetaIngredienteMapper recetaIngredienteMapper;

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
}
