package com.example.smartchef.modelos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "preferencia", schema = "smartchef")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Preferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "tipo", nullable = false)
    private Integer tipo;
}