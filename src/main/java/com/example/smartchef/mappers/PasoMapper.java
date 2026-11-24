package com.example.smartchef.mappers;

import com.example.smartchef.dtos.request.PasoRequestDto;
import com.example.smartchef.dtos.response.PasoResponseDto;
import com.example.smartchef.modelos.Paso;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PasoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "receta", ignore = true)
    Paso aEntidad(PasoRequestDto dto);

    PasoResponseDto aResponseDto(Paso paso);

    List<PasoResponseDto> aResponseDtoList(List<Paso> pasos);
}