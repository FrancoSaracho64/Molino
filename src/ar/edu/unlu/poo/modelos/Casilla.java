package ar.edu.unlu.poo.modelos;

import ar.edu.unlu.poo.enumerados.EstadoCasilla;

public class Casilla {
    private final int fila;
    private final int columna;
    private Ficha ficha;
    private EstadoCasilla estadoCasilla;

    public Casilla(int fila, int columna, EstadoCasilla estadoCasilla) {
        this.fila = fila;
        this.columna = columna;
        this.estadoCasilla = estadoCasilla;
        this.ficha = null;
    }

    public void setEstadoCasilla(EstadoCasilla estadoCasilla) {
        this.estadoCasilla = estadoCasilla;
    }

    public EstadoCasilla getEstadoCasilla() {
        return estadoCasilla;
    }

    public Ficha getFicha() {
        return ficha;
    }

    public void setFicha(Ficha ficha) {
        this.ficha = ficha;
    }
}
