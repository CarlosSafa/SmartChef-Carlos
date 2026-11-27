package com.example.smartchef.repositorios;

import com.example.smartchef.modelos.Receta;
import org.springframework.data.jpa.domain.Specification;

// Filtros del catálogo (endpoint 1). Cada método es un filtro que se puede combinar con los demás (Q7)
public class RecetaSpecifications {

    // Sin filtro: todas las recetas
    public static Specification<Receta> todas() {
        return (root, query, cb) -> cb.conjunction();
    }

    // El nombre contiene el texto (sin distinguir mayúsculas)
    public static Specification<Receta> nombreContiene(String texto) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("nombre")), "%" + texto.toLowerCase() + "%");
    }

    // La dificultad es exactamente la pedida
    public static Specification<Receta> dificultadIgual(Integer dificultad) {
        return (root, query, cb) -> cb.equal(root.get("dificultad"), dificultad);
    }

    // Se hace en ese tiempo o menos
    public static Specification<Receta> tiempoMaximo(Integer minutos) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("tiempo"), minutos);
    }
}
