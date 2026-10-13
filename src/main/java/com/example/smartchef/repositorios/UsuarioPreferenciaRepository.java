package com.example.smartchef.repositorios;

import com.example.smartchef.modelos.UsuarioPreferencia;
import com.example.smartchef.modelos.UsuarioPreferenciaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioPreferenciaRepository extends JpaRepository<UsuarioPreferencia, UsuarioPreferenciaId> {

    List<UsuarioPreferencia> findByUsuarioId(Integer idUsuario);
}