package com.example.smartchef.repositorios;

import com.example.smartchef.modelos.Coleccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ColeccionRepository extends JpaRepository<Coleccion, Integer> {

    // 12. GET /api/v1/colecciones: Mis colecciones pertenecientes a un usuario
    List<Coleccion> findByUsuarioId(Integer idUsuario);

    // 13. Buscar una colección de un usuario por su nombre (sin distinguir mayúsculas)
    Optional<Coleccion> findByUsuarioIdAndNombreIgnoreCase(Integer idUsuario, String nombre);
}