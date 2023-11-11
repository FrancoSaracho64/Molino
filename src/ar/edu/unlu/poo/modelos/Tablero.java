package ar.edu.unlu.poo.modelos;

import ar.edu.unlu.poo.enumerados.EstadoCasilla;

public class Tablero {
    private Casilla[][] casillas;
    private final int columnas;
    private final int filas;

    public Tablero(int filas, int columnas) {
        //Creo una matriz 13*13 para representar todas las fichas
        this.casillas = new Casilla[filas][columnas];
        this.filas = filas;
        this.columnas = columnas;

        //Inicializo todas las casillas en INVÁLIDA
        for (int i = 0; i < columnas; i++){
            for (int j = 0; j < filas; j++){
                casillas[i][j] = new Casilla(EstadoCasilla.INVALIDA);
            }
        }
        ocuparCasillasValidas();
    }

    /**
     * Se marca como "LIBRE" a todas aquellas casillas que se podrá acceder durante el juego.
     */
    private void ocuparCasillasValidas(){
        casillas[0][0].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[0][6].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[0][12].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[2][2].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[2][6].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[2][10].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[4][4].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[4][6].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[4][8].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[6][0].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[6][2].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[6][4].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[6][8].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[6][10].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[6][12].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[8][4].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[8][6].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[8][8].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[10][2].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[10][6].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[10][10].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[12][0].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[12][6].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[12][12].setEstadoCasilla(EstadoCasilla.LIBRE);
    }

    public Casilla obtenerCasilla(int fila, int columna){
        return casillas[fila][columna];
    }

    /**
     * Colocar una ficha en una casilla dentro del tablero.
     * @param fila Fila
     * @param columna Columna
     * @param ficha Ficha a colocar en el tablero.
     */
    public void colocarFicha(int fila, int columna, Ficha ficha){
        casillas[fila][columna].colocarFicha(ficha);
    }

    public Ficha obtenerFicha(int fila, int columna){
        return casillas[fila][columna].getFicha();
    }

    public int getCountFilas() {
        return filas;
    }

    public int getCountColumnas() {
        return columnas;
    }
}

