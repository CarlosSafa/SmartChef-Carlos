package com.example.smartchef.repositorios;

import com.example.smartchef.modelos.Despensa;
import com.example.smartchef.modelos.DespensaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DespensaRepository extends JpaRepository<Despensa, DespensaId> {

    // 8. GET /api/v1/despensa: Mi despensa, con los próximos a caducar primero
    @Query("SELECT d FROM Despensa d WHERE d.usuario.id = :idUsuario ORDER BY d.caducidad ASC NULLS LAST")
    List<Despensa> findByUsuarioIdOrderByCaducidadAsc(@Param("idUsuario") Integer idUsuario);
}