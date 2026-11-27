package com.example.smartchef.servicios.impl;

import com.example.smartchef.dtos.request.UsuarioRecetaRequestDto;
import com.example.smartchef.dtos.response.UsuarioRecetaResponseDto;
import com.example.smartchef.excepciones.ConflictoException;
import com.example.smartchef.excepciones.RecursoNoEncontradoException;
import com.example.smartchef.mappers.UsuarioRecetaMapper;
import com.example.smartchef.modelos.Receta;
import com.example.smartchef.modelos.Usuario;
import com.example.smartchef.modelos.UsuarioReceta;
import com.example.smartchef.repositorios.RecetaRepository;
import com.example.smartchef.repositorios.UsuarioRecetaRepository;
import com.example.smartchef.repositorios.UsuarioRepository;
import com.example.smartchef.servicios.RegistroService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RegistroServiceImpl implements RegistroService {

    private final UsuarioRecetaRepository usuarioRecetaRepository;
    private final UsuarioRepository usuarioRepository;
    private final RecetaRepository recetaRepository;
    private final UsuarioRecetaMapper usuarioRecetaMapper;

    @Override
    @Transactional
    public UsuarioRecetaResponseDto registrar(Integer usuarioId, UsuarioRecetaRequestDto peticion) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el usuario con id " + usuarioId));
        Receta receta = recetaRepository.findById(peticion.recetaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la receta con id " + peticion.recetaId()));

        // RN-07: un solo registro por día y momento
        LocalDate hoy = LocalDate.now();
        LocalDateTime inicioDelDia = hoy.atStartOfDay();
        LocalDateTime finDelDia = hoy.atTime(23, 59, 59);

        if (usuarioRecetaRepository.existsByUsuarioIdAndTipoAndFechaBetween(usuarioId, peticion.tipo(), inicioDelDia, finDelDia)) {
            throw new ConflictoException("Ya tienes un registro hoy para ese momento del día");
        }

        UsuarioReceta registro = new UsuarioReceta();
        registro.setUsuario(usuario);
        registro.setReceta(receta);
        registro.setTipo(peticion.tipo());
        registro.setFecha(LocalDateTime.now());
        UsuarioReceta guardado = usuarioRecetaRepository.save(registro);

        log.info("Registro creado: usuario {}, receta {}, momento {}", usuarioId, receta.getId(), peticion.tipo());
        return usuarioRecetaMapper.aResponseDto(guardado);
    }
}
