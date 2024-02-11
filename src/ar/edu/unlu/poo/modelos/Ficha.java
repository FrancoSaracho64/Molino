package ar.edu.unlu.poo.modelos;

import ar.edu.unlu.poo.interfaces.IFicha;

import java.io.Serializable;

public class Ficha implements Serializable, IFicha {
    private final Jugador jugador;

    public Ficha(Jugador jugador) {
        this.jugador = jugador;
    }

    @Override
    public Jugador getJugador() {
        return jugador;
    }
}
