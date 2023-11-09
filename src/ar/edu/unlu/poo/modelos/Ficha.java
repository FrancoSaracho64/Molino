package ar.edu.unlu.poo.modelos;

import ar.edu.unlu.poo.enumerados.Simbolo;
import ar.edu.unlu.poo.enumerados.EstadoFicha;

public class Ficha {
    private Simbolo simbolo;
    private EstadoFicha estadoFicha;
    private int posX;
    private int posY;


    public Simbolo getSimbolo() {
        return simbolo;
    }

    public EstadoFicha getEstadoFicha() {
        return estadoFicha;
    }
}
