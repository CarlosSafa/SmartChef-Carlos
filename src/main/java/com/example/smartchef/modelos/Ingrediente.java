package com.example.smartchef.modelos;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ingrediente", schema = "smartchef")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "unidad_medida", nullable = false)
    private Integer unidadMedida;

    @Column(name = "calorias", precision = 8, scale = 2)
    private BigDecimal calorias;

    @Column(name = "precio", precision = 8, scale = 2)
    private BigDecimal precio;
}