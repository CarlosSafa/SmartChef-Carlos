package com.example.smartchef.repositorios;

import com.example.smartchef.modelos.Ingrediente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredienteRepository extends JpaRepository<Ingrediente, Integer> {

    // 6. GET /api/v1/ingredientes?texto=: Catálogo de ingredientes paginado
    Page<Ingrediente> findByNombreContainingIgnoreCase(String texto, Pageable pageable);
}
