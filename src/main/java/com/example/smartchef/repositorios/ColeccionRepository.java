package com.example.smartchef.repositorios;

import com.example.smartchef.modelos.Coleccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColeccionRepository extends JpaRepository<Coleccion, Integer> {

    // 12. GET /api/v1/colecciones: Mis colecciones pertenecientes a un usuario
    List<Coleccion> findByUsuarioId(Integer idUsuario);
}