package com.example.smartchef.repositorios;

import com.example.smartchef.dtos.response.RecetaPopularDto;
import com.example.smartchef.modelos.Coleccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ColeccionRepository extends JpaRepository<Coleccion, Integer> {

    // 12. GET /api/v1/colecciones: Mis colecciones pertenecientes a un usuario
    List<Coleccion> findByUsuarioId(Integer idUsuario);

    // 13. Buscar una colección de un usuario por su nombre (sin distinguir mayúsculas)
    Optional<Coleccion> findByUsuarioIdAndNombreIgnoreCase(Integer idUsuario, String nombre);

    // 16. Informe: recetas guardadas en más colecciones (Q2 JOIN + COUNT + GROUP BY, Q5 SELECT new DTO)
    @Query("SELECT new com.example.smartchef.dtos.response.RecetaPopularDto(r.id, r.nombre, COUNT(c)) " +
            "FROM Coleccion c JOIN c.recetas r " +
            "GROUP BY r.id, r.nombre " +
            "ORDER BY COUNT(c) DESC")
    List<RecetaPopularDto> findRecetasMasGuardadas();
}
