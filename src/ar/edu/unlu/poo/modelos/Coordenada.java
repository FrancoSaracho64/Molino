package ar.edu.unlu.poo.modelos;

public class Coordenada {
    private final int fila;
    private final char columna;

    public Coordenada(int fila, char columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public int getFila() {
        return transformarEntradaFila(fila);
    }
    public int getColumna() {
        return transformarEntradaColumna(columna);
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

    private int transformarEntradaFila(int fil){
        int fila;
        switch (fil){
            case 1 -> fila = 0;
            case 2 -> fila = 2;
            case 3 -> fila = 4;
            case 4 -> fila = 6;
            case 5 -> fila = 8;
            case 6 -> fila = 10;
            case 7 -> fila = 12;
            default -> fila = -1;
        }
        return fila;
    }
}
