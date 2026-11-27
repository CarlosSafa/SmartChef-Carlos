package com.example.smartchef.servicios.impl;

import com.example.smartchef.dtos.request.DespensaRequestDto;
import com.example.smartchef.dtos.response.DespensaResponseDto;
import com.example.smartchef.excepciones.RecursoNoEncontradoException;
import com.example.smartchef.mappers.DespensaMapper;
import com.example.smartchef.modelos.Despensa;
import com.example.smartchef.modelos.Ingrediente;
import com.example.smartchef.modelos.Usuario;
import com.example.smartchef.repositorios.DespensaRepository;
import com.example.smartchef.repositorios.IngredienteRepository;
import com.example.smartchef.repositorios.UsuarioRepository;
import com.example.smartchef.servicios.DespensaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DespensaServiceImpl implements DespensaService {

    private final DespensaRepository despensaRepository;
    private final UsuarioRepository usuarioRepository;
    private final IngredienteRepository ingredienteRepository;
    private final DespensaMapper despensaMapper;

    @Override
    public List<DespensaResponseDto> listar(Integer usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RecursoNoEncontradoException("No existe el usuario con id " + usuarioId);
        }
        List<Despensa> despensa = despensaRepository.findByUsuarioIdOrderByCaducidadAsc(usuarioId);
        return despensaMapper.aResponseDtoList(despensa);
    }

    @Override
    @Transactional
    public DespensaResponseDto guardar(Integer usuarioId, Integer ingredienteId, DespensaRequestDto peticion) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el usuario con id " + usuarioId));
        Ingrediente ingrediente = ingredienteRepository.findById(ingredienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el ingrediente con id " + ingredienteId));

        // ¿Ya tenía este ingrediente en su despensa?
        Despensa despensa = despensaRepository.findByUsuarioIdAndIngredienteId(usuarioId, ingredienteId)
                .orElse(null);

        // Cantidad 0: se quita de la despensa
        if (peticion.cantidad().compareTo(BigDecimal.ZERO) == 0) {
            if (despensa != null) {
                despensaRepository.delete(despensa);
            }
            log.info("Ingrediente {} quitado de la despensa del usuario {}", ingredienteId, usuarioId);
            return null;
        }

        if (despensa == null) {
            despensa = new Despensa();
            despensa.setUsuario(usuario);
            despensa.setIngrediente(ingrediente);
        }

        despensa.setCantidad(peticion.cantidad());
        despensa.setCaducidad(peticion.caducidad());
        Despensa guardada = despensaRepository.save(despensa);

        log.info("Despensa del usuario {} actualizada: ingrediente {}, cantidad {}",
                usuarioId, ingredienteId, peticion.cantidad());
        return despensaMapper.aResponseDto(guardada);
    }
}
