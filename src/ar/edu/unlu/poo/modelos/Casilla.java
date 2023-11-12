package ar.edu.unlu.poo.modelos;

import ar.edu.unlu.poo.enumerados.EstadoCasilla;

public class Casilla {
    private Ficha ficha;
    private EstadoCasilla estado;

    public Casilla(EstadoCasilla estado) {
        this.ficha = null;
        this.estado = estado;
    }

    public void setEstadoCasilla(EstadoCasilla estadoCasilla) {
        this.estado = estadoCasilla;
    }

    public EstadoCasilla getEstadoCasilla() {
        return estado;
    }

    public Ficha getFicha() {
        return ficha;
    }

    public void colocarFicha(Ficha ficha) {
        this.ficha = ficha;
        this.estado = EstadoCasilla.OCUPADA;
    }

    public boolean esValida(){
        return estado != EstadoCasilla.INVALIDA;
    }

    public void quitarFicha() {
        ficha = null;
    }
}
