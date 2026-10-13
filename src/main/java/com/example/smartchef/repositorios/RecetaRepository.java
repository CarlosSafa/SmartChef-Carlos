package com.example.smartchef.repositorios;

import com.example.smartchef.modelos.Receta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, Integer> {

    // 1. GET /api/v1/recetas: Catálogo paginado con filtros (RF02)
    @Query("SELECT r FROM Receta r WHERE " +
            "(:nombre IS NULL OR LOWER(r.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
            "(:dificultad IS NULL OR r.dificultad = :dificultad) AND " +
            "(:tiempoMax IS NULL OR r.tiempo <= :tiempoMax)")
    Page<Receta> findConFiltros(@Param("nombre") String nombre,
                                @Param("dificultad") Integer dificultad,
                                @Param("tiempoMax") Integer tiempoMax,
                                Pageable pageable);

    // 2. GET /api/v1/recetas/{id}: Detalle con ingredientes, pasos y fotos
    @EntityGraph(attributePaths = {"pasos", "fotos", "ingredientes", "ingredientes.ingrediente"})
    Optional<Receta> findById(Integer id);

    // 16. GET /api/v1/informes/recetas-populares: Recetas más cocinadas/guardadas
    @Query("SELECT ur.receta, COUNT(ur) AS total " +
            "FROM UsuarioReceta ur " +
            "WHERE (:desde IS NULL OR ur.fecha >= :desde) " +
            "  AND (:hasta IS NULL OR ur.fecha <= :hasta) " +
            "  AND (:tipo IS NULL OR ur.tipo = :tipo) " +
            "GROUP BY ur.receta " +
            "ORDER BY total DESC")
    List<Object[]> findRecetasPopulares(@Param("desde") LocalDateTime desde,
                                        @Param("hasta") LocalDateTime hasta,
                                        @Param("tipo") Integer tipo,
                                        Pageable pageable);
}