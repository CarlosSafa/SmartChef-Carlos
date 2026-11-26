package com.example.smartchef.mappers;

import com.example.smartchef.dtos.response.RecetaIngredienteResponseDto;
import com.example.smartchef.modelos.RecetaIngrediente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface RecetaIngredienteMapper {

    @Mapping(target = "ingredienteId", source = "recetaIngrediente.ingrediente.id")
    @Mapping(target = "nombreIngrediente", source = "recetaIngrediente.ingrediente.nombre")
    @Mapping(target = "unidadMedida", source = "recetaIngrediente.ingrediente.unidadMedida")
    @Mapping(target = "cantidad", source = "recetaIngrediente.cantidad")
    @Mapping(target = "caloriasCalculadas", source = "calorias")
    @Mapping(target = "costeCalculado", source = "coste")
    RecetaIngredienteResponseDto aResponseDto(RecetaIngrediente recetaIngrediente, BigDecimal calorias, BigDecimal coste);
}
