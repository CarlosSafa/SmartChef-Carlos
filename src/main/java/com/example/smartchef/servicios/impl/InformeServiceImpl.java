package com.example.smartchef.servicios.impl;

import com.example.smartchef.dtos.response.InformeRecetasPopularesDto;
import com.example.smartchef.dtos.response.RecetaPopularDto;
import com.example.smartchef.excepciones.ReglaNegocioException;
import com.example.smartchef.repositorios.ColeccionRepository;
import com.example.smartchef.repositorios.RecetaRepository;
import com.example.smartchef.servicios.InformeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class InformeServiceImpl implements InformeService {

    private final RecetaRepository recetaRepository;
    private final ColeccionRepository coleccionRepository;

    @Override
    public InformeRecetasPopularesDto recetasPopulares(LocalDate desde, LocalDate hasta) {
        if (desde.isAfter(hasta)) {
            throw new ReglaNegocioException("La fecha 'desde' no puede ser posterior a 'hasta'");
        }

        List<RecetaPopularDto> masCocinadas = recetaRepository.findRecetasMasCocinadas(desde, hasta);
        List<RecetaPopularDto> masGuardadas = coleccionRepository.findRecetasMasGuardadas();

        log.info("Informe de recetas populares del {} al {}", desde, hasta);
        return new InformeRecetasPopularesDto(masCocinadas, masGuardadas);
    }
}
