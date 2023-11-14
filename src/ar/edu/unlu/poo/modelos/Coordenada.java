package ar.edu.unlu.poo.modelos;

public class Coordenada {
    private final int fila;
    private final int columna;

    public Coordenada(int fila, char columna) {
        this.fila = fila;
        this.columna = transformarEntradaColumna(columna);
    }

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

    private int transformarEntradaColumna(char col){
        int columna;
        switch (col){
            case 'A' -> columna = 0;
            case 'B' -> columna = 2;
            case 'C' -> columna = 4;
            case 'D' -> columna = 6;
            case 'E' -> columna = 8;
            case 'F' -> columna = 10;
            case 'G' -> columna = 12;
            default -> columna = -1;
        }
        return columna;
    }

    private char transformarEntradaColumna(int col) {
        char columna;
        switch (col) {
            case 0 -> columna = 'A';
            case 2 -> columna = 'B';
            case 4 -> columna = 'C';
            case 6 -> columna = 'D';
            case 8 -> columna = 'E';
            case 10 -> columna = 'F';
            case 12 -> columna = 'G';
            default -> columna = ' ';
        }
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
