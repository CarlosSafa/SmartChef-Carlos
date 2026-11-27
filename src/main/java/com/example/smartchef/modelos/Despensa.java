package com.example.smartchef.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

// N:M usuario–ingrediente CON atributos (cantidad y caducidad): por eso es una entidad.
// Tiene su propio id, así que no necesita una clase Id aparte.
@Entity
@Table(name = "despensas", schema = "smartchef")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Despensa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ingrediente", nullable = false)
    private Ingrediente ingrediente;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidad;

    // Puede ser null: la sal o el aceite no caducan pronto
    private LocalDate caducidad;
}
