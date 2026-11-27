package com.example.smartchef.mappers;

import com.example.smartchef.dtos.response.FotoResponseDto;
import com.example.smartchef.modelos.FotoReceta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FotoMapper {

    @Mapping(target = "recetaId", source = "receta.id")
    FotoResponseDto aResponseDto(FotoReceta foto);

    List<FotoResponseDto> aResponseDtoList(List<FotoReceta> fotos);
}