package com.example.smartchef.repositorios;

import com.example.smartchef.modelos.Dificultad;
import com.example.smartchef.modelos.Receta;
import org.springframework.data.jpa.domain.Specification;

// Filtros del catálogo (endpoint 1). Cada método es un filtro que se puede combinar con los demás (Q7)
public class RecetaSpecifications {

    // RN-09: las recetas retiradas no salen en las búsquedas
    public static Specification<Receta> activas() {
        return (root, query, cb) -> cb.isTrue(root.get("activa"));
    }

    // El nombre contiene el texto (sin distinguir mayúsculas)
    public static Specification<Receta> nombreContiene(String texto) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("nombre")), "%" + texto.toLowerCase() + "%");
    }

    // La dificultad es exactamente la pedida
    public static Specification<Receta> dificultadIgual(Dificultad dificultad) {
        return (root, query, cb) -> cb.equal(root.get("dificultad"), dificultad);
    }

    // Se hace en ese tiempo o menos
    public static Specification<Receta> tiempoMaximo(Integer minutos) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("tiempo"), minutos);
    }
}
