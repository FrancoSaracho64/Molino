package ar.edu.unlu.poo.modelos;

import java.util.ArrayList;

public class Participante {
    private Jugador jugador;
    private ArrayList<Ficha> fichas;
    private int fichasColocadas;

    public Participante(Jugador jugador, ArrayList<Ficha> fichas) {
        this.jugador = jugador;
        this.fichas = fichas;
        this. fichasColocadas = 0;
    }

    public int getFichasColocadas() {
        return fichasColocadas;
    }

    public void setFichasColocadas(int fichasColocadas) {
        this.fichasColocadas = fichasColocadas;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public ArrayList<Ficha> getFichas() {
        return fichas;
    }
}
