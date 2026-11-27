package com.example.smartchef.mappers;

import com.example.smartchef.dtos.response.IngredienteResponseDto;
import com.example.smartchef.modelos.Ingrediente;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IngredienteMapper {

    IngredienteResponseDto aResponseDto(Ingrediente ingrediente);

    List<IngredienteResponseDto> aResponseDtoList(List<Ingrediente> ingredientes);
}
