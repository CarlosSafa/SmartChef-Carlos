package com.example.smartchef.mappers;

import com.example.smartchef.dtos.response.UsuarioRecetaResponseDto;
import com.example.smartchef.modelos.UsuarioReceta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioRecetaMapper {

    @Mapping(target = "usuarioId", source = "usuario.id")
    @Mapping(target = "recetaId", source = "receta.id")
    @Mapping(target = "recetaNombre", source = "receta.nombre")
    UsuarioRecetaResponseDto aResponseDto(UsuarioReceta usuarioReceta);
}
