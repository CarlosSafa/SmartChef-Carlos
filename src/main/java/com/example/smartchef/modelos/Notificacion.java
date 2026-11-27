package com.example.smartchef.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// Sección 3.8: destinatario, tipo, mensaje, recurso relacionado, leída y fecha
@Entity
@Table(name = "notificaciones", schema = "smartchef")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoNotificacion tipo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RecursoNotificacion recurso;

    @Column(name = "recurso_id")
    private Integer recursoId;

    @Column(nullable = false)
    @Builder.Default
    private Boolean leida = false;

    @Column(name = "creada_en", nullable = false, updatable = false)
    private LocalDateTime creadaEn;

    @PrePersist
    void alCrear() {
        creadaEn = LocalDateTime.now();
    }
}
