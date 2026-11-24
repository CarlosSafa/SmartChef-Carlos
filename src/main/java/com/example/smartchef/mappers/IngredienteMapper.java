package com.example.smartchef.mappers;

import com.example.smartchef.dtos.request.IngredienteRequestDto;
import com.example.smartchef.dtos.response.IngredienteResponseDto;
import com.example.smartchef.modelos.Ingrediente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IngredienteMapper {

    @Mapping(target = "id", ignore = true)
    Ingrediente aEntidad(IngredienteRequestDto dto);

    IngredienteResponseDto aResponseDto(Ingrediente ingrediente);

    List<IngredienteResponseDto> aResponseDtoList(List<Ingrediente> ingredientes);
}