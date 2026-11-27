package com.example.smartchef.mappers;

import com.example.smartchef.dtos.response.RecetaIngredienteResponseDto;
import com.example.smartchef.modelos.RecetaIngrediente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RecetaIngredienteMapper {

    @Mapping(target = "ingredienteId", source = "ingrediente.id")
    @Mapping(target = "nombreIngrediente", source = "ingrediente.nombre")
    @Mapping(target = "unidadMedida", source = "ingrediente.unidadMedida")
    RecetaIngredienteResponseDto aResponseDto(RecetaIngrediente recetaIngrediente);
}
