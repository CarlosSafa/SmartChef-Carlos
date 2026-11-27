package com.example.smartchef.mappers;

import com.example.smartchef.dtos.request.RecetaCreacionDto;
import com.example.smartchef.dtos.response.RecetaDetalleResponseDto;
import com.example.smartchef.modelos.Receta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", uses = {PasoMapper.class, RecetaIngredienteMapper.class})
public interface RecetaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ingredientes", ignore = true)
    @Mapping(target = "autor", ignore = true)
    @Mapping(target = "activa", ignore = true)
    @Mapping(target = "creadoEn", ignore = true)
    @Mapping(target = "actualizadoEn", ignore = true)
    Receta aEntidad(RecetaCreacionDto dto);

    // Versión corta (la usa ColeccionMapper para listar las recetas de una colección)
    @Mapping(target = "costePorRacion", ignore = true)
    @Mapping(target = "caloriasPorRacion", ignore = true)
    @Mapping(target = "ingredientes", ignore = true)
    RecetaDetalleResponseDto aDetalleDto(Receta receta);

    // Versión completa (endpoint 2): el coste y las calorías por ración los calcula el servicio.
    // Los ingredientes y los pasos los convierte MapStruct solo con los otros mappers (uses = ...)
    @Mapping(target = "costePorRacion", source = "costePorRacion")
    @Mapping(target = "caloriasPorRacion", source = "caloriasPorRacion")
    RecetaDetalleResponseDto aDetalleCompleto(Receta receta, BigDecimal costePorRacion, BigDecimal caloriasPorRacion);
}
