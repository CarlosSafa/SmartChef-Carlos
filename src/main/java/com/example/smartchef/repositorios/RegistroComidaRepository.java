package com.example.smartchef.repositorios;

import com.example.smartchef.dtos.response.ResumenDiaDto;
import com.example.smartchef.modelos.Momento;
import com.example.smartchef.modelos.RegistroComida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RegistroComidaRepository extends JpaRepository<RegistroComida, Integer> {

    // 14. RN-07: ¿ya hay un registro de este usuario ese día en ese momento? (Q1)
    boolean existsByUsuarioIdAndFechaAndMomento(Integer usuarioId, LocalDate fecha, Momento momento);

    // 15. Calorías de cada día entre el lunes y el domingo (Q5 SELECT new DTO, sugerida por el enunciado)
    // Cada comida cuenta como UNA ración. Las comidas libres (sin receta) suman 0.
    @Query("SELECT new com.example.smartchef.dtos.response.ResumenDiaDto(" +
            "rc.fecha, COALESCE(ROUND(SUM(ri.cantidad * i.calorias / r.raciones), 2), 0)) " +
            "FROM RegistroComida rc " +
            "LEFT JOIN rc.receta r LEFT JOIN r.ingredientes ri LEFT JOIN ri.ingrediente i " +
            "WHERE rc.usuario.id = :usuarioId AND rc.fecha BETWEEN :lunes AND :domingo " +
            "GROUP BY rc.fecha " +
            "ORDER BY rc.fecha")
    List<ResumenDiaDto> findCaloriasPorDia(@Param("usuarioId") Integer usuarioId,
                                           @Param("lunes") LocalDate lunes,
                                           @Param("domingo") LocalDate domingo);
}
