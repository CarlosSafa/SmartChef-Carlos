package com.example.smartchef.repositorios;

import com.example.smartchef.modelos.Despensa;
import com.example.smartchef.modelos.DespensaId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DespensaRepository extends JpaRepository<Despensa, DespensaId> {

    // 8. GET /api/v1/despensa: Mi despensa, con los próximos a caducar primero
    // (en PostgreSQL, al ordenar ASC los que no tienen caducidad salen al final)
    List<Despensa> findByUsuarioIdOrderByCaducidadAsc(Integer idUsuario);
}
