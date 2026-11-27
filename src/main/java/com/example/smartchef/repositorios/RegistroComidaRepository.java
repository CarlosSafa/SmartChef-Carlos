package com.example.smartchef.repositorios;

import com.example.smartchef.modelos.Momento;
import com.example.smartchef.modelos.RegistroComida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface RegistroComidaRepository extends JpaRepository<RegistroComida, Integer> {

    // 14. RN-07: ¿ya hay un registro de este usuario ese día en ese momento? (Q1, sugerida por el enunciado)
    boolean existsByUsuarioIdAndFechaAndMomento(Integer usuarioId, LocalDate fecha, Momento momento);
}
