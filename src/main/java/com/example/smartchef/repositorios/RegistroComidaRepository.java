package com.example.smartchef.repositorios;

import com.example.smartchef.modelos.Momento;
import com.example.smartchef.modelos.RegistroComida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RegistroComidaRepository extends JpaRepository<RegistroComida, Integer> {

    // 14. RN-07: ¿ya hay un registro de este usuario ese día en ese momento? (Q1, sugerida por el enunciado)
    boolean existsByUsuarioIdAndFechaAndMomento(Integer usuarioId, LocalDate fecha, Momento momento);

    // 15. Calorías de cada día de la semana de "fecha" (Q3 nativa con DATE_TRUNC, EXTRACT y COALESCE)
    // Cada comida cuenta como UNA ración. Las comidas libres (sin receta) suman 0.
    @Query(value = """
            SELECT CAST(EXTRACT(ISODOW FROM rc.fecha) AS INTEGER) AS dia,
                   COALESCE(ROUND(SUM(ri.cantidad * i.calorias / r.raciones), 2), 0) AS calorias
            FROM smartchef.registros_comida rc
            LEFT JOIN smartchef.recetas r ON r.id = rc.id_receta
            LEFT JOIN smartchef.recetas_ingredientes ri ON ri.id_receta = r.id
            LEFT JOIN smartchef.ingredientes i ON i.id = ri.id_ingrediente
            WHERE rc.id_usuario = :usuarioId
              AND DATE_TRUNC('week', rc.fecha) = DATE_TRUNC('week', CAST(:fecha AS DATE))
            GROUP BY rc.fecha
            ORDER BY rc.fecha
            """, nativeQuery = true)
    List<CaloriasDiaProyeccion> findCaloriasPorDiaDeLaSemana(@Param("usuarioId") Integer usuarioId,
                                                             @Param("fecha") LocalDate fecha);
}
