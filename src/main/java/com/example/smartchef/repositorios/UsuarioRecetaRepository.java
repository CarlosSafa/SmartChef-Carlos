package com.example.smartchef.repositorios;

import com.example.smartchef.modelos.UsuarioReceta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface UsuarioRecetaRepository extends JpaRepository<UsuarioReceta, Integer> {

    // 14. RN-07: ¿ya hay un registro de este usuario en este momento del día (1, 2 o 3) ese día?
    boolean existsByUsuarioIdAndTipoAndFechaBetween(Integer usuarioId, Integer tipo, LocalDateTime desde, LocalDateTime hasta);
}
