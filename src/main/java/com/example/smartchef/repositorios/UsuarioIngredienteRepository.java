package com.example.smartchef.repositorios;

import com.example.smartchef.modelos.UsuarioIngrediente;
import com.example.smartchef.modelos.UsuarioIngredienteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioIngredienteRepository extends JpaRepository<UsuarioIngrediente, UsuarioIngredienteId> {

    List<UsuarioIngrediente> findByUsuarioId(Integer idUsuario);
}