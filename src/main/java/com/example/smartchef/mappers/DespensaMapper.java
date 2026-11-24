package com.example.smartchef.mappers;

import com.example.smartchef.dtos.response.DespensaResponseDto;
import com.example.smartchef.modelos.Despensa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DespensaMapper {

    @Mapping(target = "usuarioId", source = "id.idUsuario")
    @Mapping(target = "ingredienteId", source = "id.idIngrediente")
    @Mapping(target = "ingredienteNombre", source = "ingrediente.nombre")
    DespensaResponseDto aResponseDto(Despensa despensa);

    List<DespensaResponseDto> aResponseDtoList(List<Despensa> despensaItems);
}