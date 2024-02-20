package ar.edu.unlu.poo.modelos;

import java.io.Serializable;

public class Ficha implements Serializable {
    private final Jugador jugador;

    public Ficha(Jugador jugador) {
        this.jugador = jugador;
    }

    public Jugador getJugador() {
        return jugador;
    }
}
