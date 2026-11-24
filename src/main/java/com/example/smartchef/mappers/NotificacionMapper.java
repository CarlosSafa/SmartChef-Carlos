package com.example.smartchef.mappers;

import com.example.smartchef.dtos.response.NotificacionResponseDto;
import com.example.smartchef.modelos.Notificacion;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificacionMapper {

    NotificacionResponseDto aResponseDto(Notificacion notificacion);

    List<NotificacionResponseDto> aResponseDtoList(List<Notificacion> notificaciones);
}