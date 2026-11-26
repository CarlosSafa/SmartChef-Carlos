package com.example.smartchef.repositorios;

import com.example.smartchef.modelos.Paso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasoRepository extends JpaRepository<Paso, Integer> {

    // Obtener los pasos de una receta ordenados por su número de paso
    List<Paso> findByRecetaIdOrderByNumeroAsc(Integer idReceta);
}