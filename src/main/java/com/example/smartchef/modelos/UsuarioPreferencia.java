package com.example.smartchef.modelos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario_preferencia", schema = "smartchef")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioPreferencia {

    @EmbeddedId
    private UsuarioPreferenciaId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idUsuario")
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idPreferencia")
    @JoinColumn(name = "id_preferencia")
    private Preferencia preferencia;
}