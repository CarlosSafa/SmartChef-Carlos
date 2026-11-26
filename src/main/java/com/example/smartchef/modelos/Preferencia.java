package com.example.smartchef.modelos;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "preferencia", schema = "smartchef")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Preferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "tipo", nullable = false)
    private Integer tipo;

    // N:M con ingrediente (tabla preferencia_ingrediente, sin datos propios)
    @ManyToMany
    @JoinTable(
            name = "preferencia_ingrediente", schema = "smartchef",
            joinColumns = @JoinColumn(name = "id_preferencia"),
            inverseJoinColumns = @JoinColumn(name = "id_ingrediente"))
    @Builder.Default
    private Set<Ingrediente> ingredientes = new HashSet<>();
}