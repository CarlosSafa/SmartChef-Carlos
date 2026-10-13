package com.example.smartchef.modelos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "preferencia_ingrediente", schema = "smartchef")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreferenciaIngrediente {

    @EmbeddedId
    private PreferenciaIngredienteId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idIngrediente")
    @JoinColumn(name = "id_ingrediente")
    private Ingrediente ingrediente;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idPreferencia")
    @JoinColumn(name = "id_preferencia")
    private Preferencia preferencia;
}