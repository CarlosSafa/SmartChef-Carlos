package com.example.smartchef.repositorios;

import com.example.smartchef.dtos.response.RecetaPopularDto;
import com.example.smartchef.modelos.Receta;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// JpaSpecificationExecutor permite buscar con filtros opcionales combinables (Q7, endpoint 1)
public interface RecetaRepository extends JpaRepository<Receta, Integer>, JpaSpecificationExecutor<Receta> {

    // 2. GET /api/v1/recetas/{id}: carga la receta con sus ingredientes de una vez (Q8, evita N+1)
    @EntityGraph(attributePaths = {"ingredientes", "ingredientes.ingrediente"})
    Optional<Receta> findById(Integer id);

    // 16. Informe: recetas más cocinadas entre dos fechas (Q2 JOIN + COUNT + GROUP BY, Q5 SELECT new DTO)
    // Las comidas libres (sin receta) no cuentan: el JOIN las deja fuera
    @Query("SELECT new com.example.smartchef.dtos.response.RecetaPopularDto(r.id, r.nombre, COUNT(rc)) " +
            "FROM RegistroComida rc JOIN rc.receta r " +
            "WHERE rc.fecha BETWEEN :desde AND :hasta " +
            "GROUP BY r.id, r.nombre " +
            "ORDER BY COUNT(rc) DESC")
    List<RecetaPopularDto> findRecetasMasCocinadas(@Param("desde") LocalDate desde,
                                                   @Param("hasta") LocalDate hasta);
}
