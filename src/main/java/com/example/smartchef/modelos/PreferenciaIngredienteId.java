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
public class PreferenciaIngredienteId implements Serializable {

    @Column(name = "id_ingrediente")
    private Integer idIngrediente;

    @Column(name = "id_preferencia")
    private Integer idPreferencia;
}