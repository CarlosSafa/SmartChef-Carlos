package com.example.smartchef.mappers;

import com.example.smartchef.dtos.request.RegistroRequestDto;
import com.example.smartchef.dtos.response.UsuarioResponseDto;
import com.example.smartchef.modelos.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rol", ignore = true)
    Usuario registroDtoToUsuario(RegistroRequestDto dto);

    @Mapping(target = "email", ignore = true)
    @Mapping(target = "preferencias", ignore = true)
    UsuarioResponseDto usuarioToUsuarioResponseDto(Usuario usuario);
}