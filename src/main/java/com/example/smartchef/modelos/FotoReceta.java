package com.example.smartchef.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// Solo los metadatos: el fichero está en disco con un nombre UUID (sección 3.7)
@Entity
@Table(name = "fotos_recetas", schema = "smartchef")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FotoReceta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_receta", nullable = false)
    private Receta receta;

    @Column(name = "nombre_original", nullable = false)
    private String nombreOriginal;

    // Nombre del fichero en disco (UUID)
    @Column(nullable = false, unique = true, length = 100)
    private String clave;

    @Column(name = "tipo_contenido", nullable = false, length = 20)
    private String tipoContenido;

    @Column(nullable = false)
    private Long tamano;

    @Column(nullable = false)
    @Builder.Default
    private Integer orden = 0;

    @Column(nullable = false)
    @Builder.Default
    private Boolean portada = false;

    @Column(name = "subida_en", nullable = false, updatable = false)
    private LocalDateTime subidaEn;

    @PrePersist
    void alCrear() {
        subidaEn = LocalDateTime.now();
    }
}
