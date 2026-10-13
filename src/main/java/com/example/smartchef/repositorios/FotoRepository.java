package com.example.smartchef.repositorios;

import com.example.smartchef.modelos.Foto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FotoRepository extends JpaRepository<Foto, Integer> {

    // Obtener las fotos pertenecientes a una receta
    List<Foto> findByRecetaId(Integer idReceta);
}