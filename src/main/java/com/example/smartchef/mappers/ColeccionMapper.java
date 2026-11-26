package com.example.smartchef.mappers;

import com.example.smartchef.dtos.response.ColeccionResponseDto;
import com.example.smartchef.modelos.Coleccion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RecetaMapper.class})
public interface ColeccionMapper {

    @Mapping(target = "usuarioId", source = "usuario.id")
    ColeccionResponseDto aResponseDto(Coleccion coleccion);

    List<ColeccionResponseDto> aResponseDtoList(List<Coleccion> colecciones);
}