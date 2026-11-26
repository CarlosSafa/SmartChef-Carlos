package com.example.smartchef.repositorios;

import com.example.smartchef.modelos.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
}