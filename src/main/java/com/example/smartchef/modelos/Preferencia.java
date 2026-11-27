package com.example.smartchef.modelos;

import jakarta.persistence.*;
import lombok.*;

// Catálogo de preferencias (VEGETARIANO, SIN_GLUTEN, RAPIDO, ECONOMICO...).
// Es una tabla para poder añadir preferencias nuevas sin cambiar el código.
@Entity
@Table(name = "preferencias", schema = "smartchef")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Preferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 30)
    private String nombre;
}
