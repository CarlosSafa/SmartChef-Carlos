package com.example.smartchef.modelos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "coleccion_receta", schema = "smartchef")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ColeccionReceta {

    @EmbeddedId
    private ColeccionRecetaId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idColeccion")
    @JoinColumn(name = "id_coleccion")
    private Coleccion coleccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idReceta")
    @JoinColumn(name = "id_receta")
    private Receta receta;
}