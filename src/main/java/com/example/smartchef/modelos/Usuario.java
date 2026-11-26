package com.example.smartchef.modelos;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuario", schema = "smartchef")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "contrasena", nullable = false, length = 255)
    private String contrasena;

    @Column(name = "rol", nullable = false)
    private Integer rol;

    // N:M con ingrediente (tabla usuario_ingrediente, sin datos propios)
    @ManyToMany
    @JoinTable(
            name = "usuario_ingrediente", schema = "smartchef",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_ingrediente"))
    @Builder.Default
    private Set<Ingrediente> ingredientes = new HashSet<>();

    // N:M con preferencia (tabla usuario_preferencia, sin datos propios)
    @ManyToMany
    @JoinTable(
            name = "usuario_preferencia", schema = "smartchef",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_preferencia"))
    @Builder.Default
    private Set<Preferencia> preferencias = new HashSet<>();
}