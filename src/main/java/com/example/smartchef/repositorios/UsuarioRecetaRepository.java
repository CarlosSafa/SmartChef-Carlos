package com.example.smartchef.repositorios;

import com.example.smartchef.modelos.UsuarioReceta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UsuarioRecetaRepository extends JpaRepository<UsuarioReceta, Integer> {

    // 14. RN-07: ¿ya hay un registro de este usuario en este momento del día (1, 2 o 3) ese día?
    boolean existsByUsuarioIdAndTipoAndFechaBetween(Integer usuarioId, Integer tipo, LocalDateTime desde, LocalDateTime hasta);

    // 15. GET /api/v1/registros/semana?fecha=: Semana por días con calorías totales
    @Query("SELECT ur FROM UsuarioReceta ur WHERE ur.usuario.id = :idUsuario " +
            "AND ur.fecha >= :inicioSemana AND ur.fecha <= :finSemana")
    List<UsuarioReceta> findRegistrosSemana(@Param("idUsuario") Integer idUsuario,
                                            @Param("inicioSemana") LocalDateTime inicioSemana,
                                            @Param("finSemana") LocalDateTime finSemana);
}