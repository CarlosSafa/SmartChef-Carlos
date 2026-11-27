package com.example.smartchef.servicios;

import com.example.smartchef.dtos.response.InformeRecetasPopularesDto;

import java.time.LocalDate;

public interface InformeService {

    InformeRecetasPopularesDto recetasPopulares(LocalDate desde, LocalDate hasta);
}
