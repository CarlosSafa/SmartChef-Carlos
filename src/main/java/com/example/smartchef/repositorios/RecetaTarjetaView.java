package com.example.smartchef.repositorios;

import com.example.smartchef.modelos.Dificultad;

// Proyección por interfaz (Q5): solo los datos de la "tarjeta" de una receta en el catálogo.
// Spring rellena estos métodos solo; no hace falta escribir ninguna clase.
public interface RecetaTarjetaView {

    Integer getId();

    String getNombre();

    Dificultad getDificultad();

    Integer getTiempo();

    Integer getRaciones();
}
