package com.example.smartchef.mappers;

import com.example.smartchef.dtos.request.RecetaCreacionDto;
import com.example.smartchef.dtos.response.RecetaDetalleResponseDto;
import com.example.smartchef.dtos.response.RecetaIngredienteResponseDto;
import com.example.smartchef.modelos.Receta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring", uses = {PasoMapper.class, FotoMapper.class})
public interface RecetaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fotos", ignore = true)
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
    @Mapping(target = "fotosUrl", ignore = true)
    RecetaDetalleResponseDto aDetalleDto(Receta receta);

    // Versión completa (endpoint 2): los ingredientes y los totales los calcula el servicio
    @Mapping(target = "ingredientes", source = "ingredientes")
    @Mapping(target = "costePorRacion", source = "costePorRacion")
    @Mapping(target = "caloriasPorRacion", source = "caloriasPorRacion")
    @Mapping(target = "fotosUrl", ignore = true)
    RecetaDetalleResponseDto aDetalleCompleto(Receta receta,
                                              List<RecetaIngredienteResponseDto> ingredientes,
                                              BigDecimal costePorRacion,
                                              BigDecimal caloriasPorRacion);
}
