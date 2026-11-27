package com.example.smartchef.servicios.impl;

import com.example.smartchef.dtos.request.RegistroComidaRequestDto;
import com.example.smartchef.dtos.response.RegistroComidaResponseDto;
import com.example.smartchef.dtos.response.ResumenDiaDto;
import com.example.smartchef.dtos.response.SemanaResponseDto;
import com.example.smartchef.excepciones.ConflictoException;
import com.example.smartchef.excepciones.RecursoNoEncontradoException;
import com.example.smartchef.excepciones.ReglaNegocioException;
import com.example.smartchef.mappers.RegistroComidaMapper;
import com.example.smartchef.modelos.Receta;
import com.example.smartchef.modelos.RegistroComida;
import com.example.smartchef.modelos.Usuario;
import com.example.smartchef.repositorios.CaloriasDiaProyeccion;
import com.example.smartchef.repositorios.RecetaRepository;
import com.example.smartchef.repositorios.RegistroComidaRepository;
import com.example.smartchef.repositorios.UsuarioRepository;
import com.example.smartchef.servicios.RegistroService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RegistroServiceImpl implements RegistroService {

    private final RegistroComidaRepository registroComidaRepository;
    private final UsuarioRepository usuarioRepository;
    private final RecetaRepository recetaRepository;
    private final RegistroComidaMapper registroComidaMapper;

    @Override
    @Transactional
    public RegistroComidaResponseDto registrar(Integer usuarioId, RegistroComidaRequestDto peticion) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el usuario con id " + usuarioId));

        // Con receta, o comida libre con descripción
        Receta receta = null;
        if (peticion.recetaId() != null) {
            receta = recetaRepository.findById(peticion.recetaId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("No existe la receta con id " + peticion.recetaId()));
        } else if (peticion.descripcion() == null || peticion.descripcion().isBlank()) {
            throw new ReglaNegocioException("Indica una receta o, si es una comida libre, una descripción");
        }

        // RN-07: un solo registro por día y momento
        if (registroComidaRepository.existsByUsuarioIdAndFechaAndMomento(usuarioId, peticion.fecha(), peticion.momento())) {
            throw new ConflictoException("Ya tienes un registro el " + peticion.fecha() + " en " + peticion.momento());
        }

        RegistroComida registro = new RegistroComida();
        registro.setUsuario(usuario);
        registro.setReceta(receta);
        registro.setDescripcion(peticion.descripcion());
        registro.setFecha(peticion.fecha());
        registro.setMomento(peticion.momento());
        RegistroComida guardado = registroComidaRepository.save(registro);

        log.info("Registro creado: usuario {}, {} {}", usuarioId, peticion.fecha(), peticion.momento());
        return registroComidaMapper.aResponseDto(guardado);
    }

    @Override
    public SemanaResponseDto semana(Integer usuarioId, LocalDate fecha) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RecursoNoEncontradoException("No existe el usuario con id " + usuarioId);
        }

        // Las calorías de los días que tienen algo registrado
        List<CaloriasDiaProyeccion> filas = registroComidaRepository.findCaloriasPorDiaDeLaSemana(usuarioId, fecha);

        // Se recorren los 7 días, de lunes a domingo; los días sin registros salen con 0
        LocalDate lunes = fecha.with(DayOfWeek.MONDAY);
        List<ResumenDiaDto> dias = new ArrayList<>();
        BigDecimal caloriasSemana = BigDecimal.ZERO;

        for (int i = 0; i < 7; i++) {
            LocalDate dia = lunes.plusDays(i);
            BigDecimal calorias = BigDecimal.ZERO;

            for (CaloriasDiaProyeccion fila : filas) {
                if (fila.getDia() == i + 1) {
                    calorias = fila.getCalorias();
                }
            }

            dias.add(new ResumenDiaDto(dia, calorias));
            caloriasSemana = caloriasSemana.add(calorias);
        }

        return new SemanaResponseDto(lunes, lunes.plusDays(6), caloriasSemana, dias);
    }
}
