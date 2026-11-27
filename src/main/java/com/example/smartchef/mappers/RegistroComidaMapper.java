package com.example.smartchef.mappers;

import com.example.smartchef.dtos.response.RegistroComidaResponseDto;
import com.example.smartchef.modelos.RegistroComida;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegistroComidaMapper {

    @Mapping(target = "usuarioId", source = "usuario.id")
    @Mapping(target = "recetaId", source = "receta.id")
    @Mapping(target = "recetaNombre", source = "receta.nombre")
    RegistroComidaResponseDto aResponseDto(RegistroComida registroComida);
}
