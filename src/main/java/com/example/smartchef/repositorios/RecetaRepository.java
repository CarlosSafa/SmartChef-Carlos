package com.example.smartchef.repositorios;

import com.example.smartchef.dtos.response.RecetaPopularDto;
import com.example.smartchef.modelos.Dificultad;
import com.example.smartchef.modelos.Receta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RecetaRepository extends JpaRepository<Receta, Integer> {

    // 1. Catálogo (Q1 derivada + Q5 proyección por interfaz + Q6 paginación):
    // recetas activas (RN-09) cuyo nombre contiene el texto, con una de esas dificultades
    // y que se hacen en ese tiempo o menos
    Page<RecetaTarjetaView> findByActivaTrueAndNombreContainingIgnoreCaseAndDificultadInAndTiempoLessThanEqual(
            String nombre, List<Dificultad> dificultades, Integer tiempoMax, Pageable pageable);

    // 2. Detalle: carga la receta con sus ingredientes de una vez (Q8, evita N+1)
    @EntityGraph(attributePaths = {"ingredientes", "ingredientes.ingrediente"})
    Optional<Receta> findById(Integer id);

    // 2. RN-02: calorías totales de la receta = suma de cantidad x calorías de cada ingrediente (Q2 JOIN + SUM)
    @Query("SELECT SUM(ri.cantidad * i.calorias) FROM RecetaIngrediente ri JOIN ri.ingrediente i " +
            "WHERE ri.receta.id = :recetaId")
    BigDecimal sumarCalorias(@Param("recetaId") Integer recetaId);

    // 2. RN-02: coste total de la receta = suma de cantidad x precio de cada ingrediente (Q2 JOIN + SUM)
    @Query("SELECT SUM(ri.cantidad * i.precio) FROM RecetaIngrediente ri JOIN ri.ingrediente i " +
            "WHERE ri.receta.id = :recetaId")
    BigDecimal sumarCoste(@Param("recetaId") Integer recetaId);

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
