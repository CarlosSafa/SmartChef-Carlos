package com.example.smartchef.modelos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario_ingrediente", schema = "smartchef")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioIngrediente {

    @EmbeddedId
    private UsuarioIngredienteId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idUsuario")
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idIngrediente")
    @JoinColumn(name = "id_ingrediente")
    private Ingrediente ingrediente;
}