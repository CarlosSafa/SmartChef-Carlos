package com.example.smartchef.modelos;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ColeccionRecetaId implements Serializable {

    @Column(name = "id_coleccion")
    private Integer idColeccion;

    @Column(name = "id_receta")
    private Integer idReceta;
}