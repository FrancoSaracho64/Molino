package ar.edu.unlu.poo.modelos;

import java.io.Serializable;

public class Coordenada implements Serializable {
    private final int fila;
    private final int columna;

    public Coordenada(int fila, int columna){
        this.fila = fila;
        this.columna = columna;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordenada that = (Coordenada) o;
        return fila == that.fila && columna == that.columna;
    }
}
