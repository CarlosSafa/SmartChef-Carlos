package com.example.smartchef.servicios;

import com.example.smartchef.dtos.request.RegistroComidaRequestDto;
import com.example.smartchef.dtos.response.SemanaResponseDto;
import com.example.smartchef.excepciones.ConflictoException;
import com.example.smartchef.excepciones.ReglaNegocioException;
import com.example.smartchef.mappers.RegistroComidaMapper;
import com.example.smartchef.modelos.Momento;
import com.example.smartchef.modelos.Receta;
import com.example.smartchef.modelos.Usuario;
import com.example.smartchef.repositorios.CaloriasDiaProyeccion;
import com.example.smartchef.repositorios.RecetaRepository;
import com.example.smartchef.repositorios.RegistroComidaRepository;
import com.example.smartchef.repositorios.UsuarioRepository;
import com.example.smartchef.servicios.impl.RegistroServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

// Regla RN-07 del registro de comidas, sin base de datos
@ExtendWith(MockitoExtension.class)
class RegistroServiceTest {

    @Mock private RegistroComidaRepository registroComidaRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private RecetaRepository recetaRepository;
    @Mock private RegistroComidaMapper registroComidaMapper;

    @InjectMocks private RegistroServiceImpl registroService;

    @Test
    @DisplayName("RN-07 · No registra dos cenas el mismo día (409) y no guarda nada")
    void noRegistraDosVecesElMismoMomento() {
        Usuario usuario = new Usuario();
        usuario.setId(5);
        Receta receta = new Receta();
        receta.setId(3);
        LocalDate hoy = LocalDate.now();

        given(usuarioRepository.findById(5)).willReturn(Optional.of(usuario));
        given(recetaRepository.findById(3)).willReturn(Optional.of(receta));
        given(registroComidaRepository.existsByUsuarioIdAndFechaAndMomento(5, hoy, Momento.CENA)).willReturn(true);

        assertThatThrownBy(() -> registroService.registrar(5, new RegistroComidaRequestDto(3, null, hoy, Momento.CENA)))
                .isInstanceOf(ConflictoException.class);

        then(registroComidaRepository).should(never()).save(any());
    }

    @Test
    @DisplayName("Sin receta y sin descripción → 422 y no guarda nada")
    void noRegistraSinRecetaNiDescripcion() {
        Usuario usuario = new Usuario();
        usuario.setId(5);
        given(usuarioRepository.findById(5)).willReturn(Optional.of(usuario));

        assertThatThrownBy(() -> registroService.registrar(5, new RegistroComidaRequestDto(null, " ", LocalDate.now(), Momento.COMIDA)))
                .isInstanceOf(ReglaNegocioException.class);

        then(registroComidaRepository).should(never()).save(any());
    }

    @Test
    @DisplayName("Endpoint 15 · La semana tiene siempre 7 días; los días sin registros salen con 0")
    void semanaConDiasVacios() {
        LocalDate jueves = LocalDate.of(2026, 11, 26);
        CaloriasDiaProyeccion miercoles = mock(CaloriasDiaProyeccion.class);
        given(miercoles.getDia()).willReturn(3);
        given(miercoles.getCalorias()).willReturn(new BigDecimal("500.00"));

        given(usuarioRepository.existsById(5)).willReturn(true);
        given(registroComidaRepository.findCaloriasPorDiaDeLaSemana(5, jueves)).willReturn(List.of(miercoles));

        SemanaResponseDto semana = registroService.semana(5, jueves);

        assertThat(semana.desde()).isEqualTo(LocalDate.of(2026, 11, 23));       // lunes
        assertThat(semana.hasta()).isEqualTo(LocalDate.of(2026, 11, 29));       // domingo
        assertThat(semana.dias()).hasSize(7);
        assertThat(semana.dias().get(2).calorias()).isEqualByComparingTo("500"); // miércoles
        assertThat(semana.dias().get(0).calorias()).isEqualByComparingTo("0");   // lunes vacío
        assertThat(semana.caloriasSemana()).isEqualByComparingTo("500");
    }
}
