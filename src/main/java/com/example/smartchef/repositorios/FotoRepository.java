package com.example.smartchef.repositorios;

import com.example.smartchef.modelos.Foto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FotoRepository extends JpaRepository<Foto, Integer> {

    // Obtener las fotos pertenecientes a una receta
    List<Foto> findByRecetaId(Integer idReceta);
}