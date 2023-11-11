package ar.edu.unlu.poo.modelos;

import ar.edu.unlu.poo.enumerados.Simbolo;
import ar.edu.unlu.poo.enumerados.EstadoFicha;

public class Ficha {
    private Jugador jugador;
    private Simbolo simbolo;
    private EstadoFicha estadoFicha;

    public Ficha(Jugador jugador) {
        this.jugador = jugador;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public Simbolo getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(Simbolo simbolo) {
        this.simbolo = simbolo;
    }

    public EstadoFicha getEstadoFicha() {
        return estadoFicha;
    }

    public void setEstadoFicha(EstadoFicha estadoFicha) {
        this.estadoFicha = estadoFicha;
    }
}
