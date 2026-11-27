package com.example.smartchef.servicios;

import com.example.smartchef.dtos.request.UsuarioRecetaRequestDto;
import com.example.smartchef.excepciones.ConflictoException;
import com.example.smartchef.mappers.UsuarioRecetaMapper;
import com.example.smartchef.modelos.Receta;
import com.example.smartchef.modelos.Usuario;
import com.example.smartchef.repositorios.RecetaRepository;
import com.example.smartchef.repositorios.UsuarioRecetaRepository;
import com.example.smartchef.repositorios.UsuarioRepository;
import com.example.smartchef.servicios.impl.RegistroServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

// Regla RN-07 del registro de comidas, sin base de datos
@ExtendWith(MockitoExtension.class)
class RegistroServiceTest {

    @Mock private UsuarioRecetaRepository usuarioRecetaRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private RecetaRepository recetaRepository;
    @Mock private UsuarioRecetaMapper usuarioRecetaMapper;

    @InjectMocks private RegistroServiceImpl registroService;

    @Test
    @DisplayName("RN-07 · No registra dos cenas el mismo día (409) y no guarda nada")
    void noRegistraDosVecesElMismoMomento() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        Receta receta = new Receta();
        receta.setId(3);

        given(usuarioRepository.findById(1)).willReturn(Optional.of(usuario));
        given(recetaRepository.findById(3)).willReturn(Optional.of(receta));
        given(usuarioRecetaRepository.existsByUsuarioIdAndTipoAndFechaBetween(eq(1), eq(3), any(), any()))
                .willReturn(true);

        assertThatThrownBy(() -> registroService.registrar(1, new UsuarioRecetaRequestDto(3, 3)))
                .isInstanceOf(ConflictoException.class);

        then(usuarioRecetaRepository).should(never()).save(any());
    }
}
