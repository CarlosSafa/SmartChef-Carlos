package com.example.smartchef.repositorios;

import com.example.smartchef.modelos.Despensa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DespensaRepository extends JpaRepository<Despensa, Integer> {

    // 8. GET /api/v1/despensa: Mi despensa, con los próximos a caducar primero
    // (en PostgreSQL, al ordenar ASC los que no tienen caducidad salen al final)
    List<Despensa> findByUsuarioIdOrderByCaducidadAsc(Integer idUsuario);

    // 9. PUT /api/v1/despensa/{ingredienteId}: ¿ya tiene este ingrediente en su despensa?
    Optional<Despensa> findByUsuarioIdAndIngredienteId(Integer idUsuario, Integer idIngrediente);
}
