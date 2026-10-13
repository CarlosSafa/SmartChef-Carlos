package com.example.smartchef.repositorios;

import com.example.smartchef.modelos.Preferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferenciaRepository extends JpaRepository<Preferencia, Integer> {
}