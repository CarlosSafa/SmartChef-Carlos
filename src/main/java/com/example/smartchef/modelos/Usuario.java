package com.example.smartchef.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios", schema = "smartchef")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String contrasena;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Rol rol;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    // N:M con preferencia (tabla usuarios_preferencias, sin datos propios): RN-03
    @ManyToMany
    @JoinTable(
            name = "usuarios_preferencias", schema = "smartchef",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_preferencia"))
    @Builder.Default
    private Set<Preferencia> preferencias = new HashSet<>();

    @PrePersist
    void alCrear() {
        creadoEn = LocalDateTime.now();
        if (rol == null) {
            rol = Rol.USUARIO;
        }
    }
}
