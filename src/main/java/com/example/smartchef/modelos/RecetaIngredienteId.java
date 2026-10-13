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
public class RecetaIngredienteId implements Serializable {

    @Column(name = "id_receta")
    private Integer idReceta;

    @Column(name = "id_ingrediente")
    private Integer idIngrediente;
}