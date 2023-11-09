package ar.edu.unlu.poo.modelos;

import ar.edu.unlu.poo.enumerados.EstadoCasilla;

public class Tablero {
    private Casilla[][] tablero;
    private final int columnas;
    private final int filas;

    public Tablero(int columnas, int filas) {
        //Creo una matriz 13*13 para representar todas las fichas
        this.tablero = new Casilla[columnas][filas];
        this.columnas = columnas;
        this.filas = filas;

        //Inicializo todas las casillas en INVÁLIDA
        for (int i = 0; i < filas; i++){
            for (int j = 0; j < columnas; j++){
                tablero[i][j] = new Casilla(i, j, EstadoCasilla.INVALIDA);
            }
        }
        ocuparCasillasValidas();
    }

    /**
     * Se marca como "LIBRE" a todas aquellas casillas que se podrá acceder durante el juego.
     */
    private void ocuparCasillasValidas(){
        tablero[0][0].setEstadoCasilla(EstadoCasilla.LIBRE);
        tablero[0][6].setEstadoCasilla(EstadoCasilla.LIBRE);
        tablero[0][12].setEstadoCasilla(EstadoCasilla.LIBRE);
        tablero[2][2].setEstadoCasilla(EstadoCasilla.LIBRE);
        tablero[2][6].setEstadoCasilla(EstadoCasilla.LIBRE);
        tablero[2][10].setEstadoCasilla(EstadoCasilla.LIBRE);
        tablero[4][4].setEstadoCasilla(EstadoCasilla.LIBRE);
        tablero[4][6].setEstadoCasilla(EstadoCasilla.LIBRE);
        tablero[4][8].setEstadoCasilla(EstadoCasilla.LIBRE);
        tablero[6][0].setEstadoCasilla(EstadoCasilla.LIBRE);
        tablero[6][2].setEstadoCasilla(EstadoCasilla.LIBRE);
        tablero[6][4].setEstadoCasilla(EstadoCasilla.LIBRE);
        tablero[6][8].setEstadoCasilla(EstadoCasilla.LIBRE);
        tablero[6][10].setEstadoCasilla(EstadoCasilla.LIBRE);
        tablero[6][12].setEstadoCasilla(EstadoCasilla.LIBRE);
        tablero[8][4].setEstadoCasilla(EstadoCasilla.LIBRE);
        tablero[8][6].setEstadoCasilla(EstadoCasilla.LIBRE);
        tablero[8][8].setEstadoCasilla(EstadoCasilla.LIBRE);
        tablero[10][2].setEstadoCasilla(EstadoCasilla.LIBRE);
        tablero[10][6].setEstadoCasilla(EstadoCasilla.LIBRE);
        tablero[10][10].setEstadoCasilla(EstadoCasilla.LIBRE);
        tablero[12][0].setEstadoCasilla(EstadoCasilla.LIBRE);
        tablero[12][6].setEstadoCasilla(EstadoCasilla.LIBRE);
        tablero[12][12].setEstadoCasilla(EstadoCasilla.LIBRE);
    }

    public Casilla obtenerCasilla(int x, int y){
        return tablero[x][y];
    }

    public void colocarFicha(int x, int y, Ficha ficha){
        tablero[x][y].setFicha(ficha);
    }

    public Ficha obtenerFicha(int x, int y){
        return tablero[x][y].getFicha();
    }

    public int getCountFilas() {
        return filas;
    }

    public int getCountColumnas() {
        return columnas;
    }
}

