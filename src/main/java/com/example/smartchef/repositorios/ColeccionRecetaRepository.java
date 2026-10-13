package com.example.smartchef.repositorios;

import com.example.smartchef.modelos.ColeccionReceta;
import com.example.smartchef.modelos.ColeccionRecetaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColeccionRecetaRepository extends JpaRepository<ColeccionReceta, ColeccionRecetaId> {

    // 12. Obtener las recetas asociadas a una colección
    List<ColeccionReceta> findByColeccionId(Integer idColeccion);
}