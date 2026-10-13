package com.example.smartchef.repositorios;

import com.example.smartchef.modelos.UsuarioReceta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UsuarioRecetaRepository extends JpaRepository<UsuarioReceta, Integer> {

    // 15. GET /api/v1/registros/semana?fecha=: Semana por días con calorías totales
    @Query("SELECT ur FROM UsuarioReceta ur WHERE ur.usuario.id = :idUsuario " +
            "AND ur.fecha >= :inicioSemana AND ur.fecha <= :finSemana")
    List<UsuarioReceta> findRegistrosSemana(@Param("idUsuario") Integer idUsuario,
                                            @Param("inicioSemana") LocalDateTime inicioSemana,
                                            @Param("finSemana") LocalDateTime finSemana);
}