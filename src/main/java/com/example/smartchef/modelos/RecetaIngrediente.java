package com.example.smartchef.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

// N:M receta–ingrediente CON atributo (cantidad): por eso es una entidad.
// Tiene su propio id, así que no necesita una clase Id aparte.
@Entity
@Table(name = "recetas_ingredientes", schema = "smartchef")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecetaIngrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_receta", nullable = false)
    private Receta receta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ingrediente", nullable = false)
    private Ingrediente ingrediente;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidad;
}
