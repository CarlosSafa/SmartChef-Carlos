package com.example.smartchef;

import com.example.smartchef.dtos.request.IngredienteRequestDto;
import com.example.smartchef.dtos.request.RegistroRequestDto;
import com.example.smartchef.dtos.response.IngredienteResponseDto;
import com.example.smartchef.dtos.response.UsuarioResponseDto;
import com.example.smartchef.mappers.IngredienteMapper;
import com.example.smartchef.mappers.UsuarioMapper;
import com.example.smartchef.modelos.Categoria;
import com.example.smartchef.modelos.Ingrediente;
import com.example.smartchef.modelos.Rol;
import com.example.smartchef.modelos.UnidadMedida;
import com.example.smartchef.modelos.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MapperTests {

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Autowired
    private IngredienteMapper ingredienteMapper;

    @Test
    @DisplayName("Probar conversión de RegistroRequestDto a entidad Usuario")
    void testRegistroDtoToUsuario() {
        RegistroRequestDto dto = new RegistroRequestDto("Lucía Ruiz", "lucia@example.com", "miPassword123");

        Usuario usuario = usuarioMapper.registroDtoToUsuario(dto);

        assertNotNull(usuario);
        assertEquals("Lucía Ruiz", usuario.getNombre());
        assertEquals("lucia@example.com", usuario.getEmail());
        assertEquals("miPassword123", usuario.getContrasena());
    }

    @Test
    @DisplayName("Probar conversión de entidad Usuario a UsuarioResponseDto")
    void testUsuarioToUsuarioResponseDto() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Carlos Gómez");
        usuario.setContrasena("hash_secret");
        usuario.setEmail("carlos@example.com");
        usuario.setRol(Rol.ADMIN);

        UsuarioResponseDto responseDto = usuarioMapper.usuarioToUsuarioResponseDto(usuario);

        assertNotNull(responseDto);
        assertEquals(1, responseDto.id());
        assertEquals("Carlos Gómez", responseDto.nombre());
        assertEquals(Rol.ADMIN, responseDto.rol());
        assertEquals("carlos@example.com", responseDto.email());
    }

    @Test
    @DisplayName("Probar conversión bidireccional de Ingrediente")
    void testIngredienteMapper() {
        IngredienteRequestDto requestDto = new IngredienteRequestDto(
                "Aceite de Oliva",
                UnidadMedida.MILILITROS,
                Categoria.DESPENSA,
                new BigDecimal("884.00"),
                new BigDecimal("0.0120")
        );

        // RequestDTO -> Entidad
        Ingrediente ingrediente = ingredienteMapper.aEntidad(requestDto);
        assertNotNull(ingrediente);
        assertEquals("Aceite de Oliva", ingrediente.getNombre());
        assertEquals(UnidadMedida.MILILITROS, ingrediente.getUnidadMedida());

        // Entidad -> ResponseDTO
        ingrediente.setId(10);
        IngredienteResponseDto responseDto = ingredienteMapper.aResponseDto(ingrediente);
        assertNotNull(responseDto);
        assertEquals(10, responseDto.id());
        assertEquals("Aceite de Oliva", responseDto.nombre());
    }
}