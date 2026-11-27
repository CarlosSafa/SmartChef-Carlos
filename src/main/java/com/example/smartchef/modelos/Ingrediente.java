package com.example.smartchef.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ingredientes", schema = "smartchef")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "unidad_medida", nullable = false, length = 10)
    private UnidadMedida unidadMedida;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private Categoria categoria;

    // Por unidad de medida (por gramo, por ml o por unidad)
    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal calorias;

    // Por unidad de medida: 4 decimales porque un gramo puede costar 0,0025 €
    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal precio;

    // N:M con preferencia (tabla ingredientes_preferencias, sin datos propios):
    // las preferencias que CUMPLE este ingrediente (RN-02)
    @ManyToMany
    @JoinTable(
            name = "ingredientes_preferencias", schema = "smartchef",
            joinColumns = @JoinColumn(name = "id_ingrediente"),
            inverseJoinColumns = @JoinColumn(name = "id_preferencia"))
    @Builder.Default
    private Set<Preferencia> preferencias = new HashSet<>();
}
