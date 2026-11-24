package com.example.smartchef.mappers;

import com.example.smartchef.dtos.request.RecetaCreacionDto;
import com.example.smartchef.dtos.response.RecetaDetalleResponseDto;
import com.example.smartchef.modelos.Receta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PasoMapper.class, FotoMapper.class})
public interface RecetaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fotos", ignore = true)
    @Mapping(target = "ingredientes", ignore = true)
    Receta aEntidad(RecetaCreacionDto dto);

    @Mapping(target = "costePorRacion", ignore = true)
    @Mapping(target = "caloriasPorRacion", ignore = true)
    @Mapping(target = "ingredientes", ignore = true)
    @Mapping(target = "fotosUrl", ignore = true)
    RecetaDetalleResponseDto aDetalleDto(Receta receta);
}