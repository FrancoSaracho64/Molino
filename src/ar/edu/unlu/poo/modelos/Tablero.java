package ar.edu.unlu.poo.modelos;

import ar.edu.unlu.poo.enumerados.EstadoCasilla;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tablero implements Serializable {
    private static final int CANT_COLUMNAS = 7;
    private static final int CANT_FILAS = 7;
    private final Casilla[][] casillas;

    public Tablero() {
        this.casillas = new Casilla[CANT_FILAS][CANT_COLUMNAS];
        for (int fila = 0; fila < CANT_FILAS; fila++) {
            for (int columna = 0; columna < CANT_COLUMNAS; columna++) {
                casillas[fila][columna] = new Casilla(new Coordenada(fila, columna));
            }
        }
        inicializarCasillasValidas();
        inicializarCasillasAdyacentes();
    }

    /**
     * Se marca como "LIBRE" a todas aquellas casillas que se podrá acceder durante el juego.
     */
    private void inicializarCasillasValidas() {
        casillas[0][0].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[0][3].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[0][6].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[1][1].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[1][3].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[1][5].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[2][2].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[2][3].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[2][4].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[3][0].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[3][1].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[3][2].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[3][4].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[3][5].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[3][6].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[4][2].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[4][3].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[4][4].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[5][1].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[5][3].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[5][5].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[6][0].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[6][3].setEstadoCasilla(EstadoCasilla.LIBRE);
        casillas[6][6].setEstadoCasilla(EstadoCasilla.LIBRE);
    }

    private void inicializarCasillasAdyacentes() {
        casillas[0][0].agregarAdyacentes(Arrays.asList(casillas[0][3], casillas[3][0]));
        casillas[0][3].agregarAdyacentes(Arrays.asList(casillas[0][0], casillas[1][3], casillas[0][6]));
        casillas[0][6].agregarAdyacentes(Arrays.asList(casillas[0][3], casillas[3][6]));
        casillas[1][1].agregarAdyacentes(Arrays.asList(casillas[1][3], casillas[3][1]));
        casillas[1][3].agregarAdyacentes(Arrays.asList(casillas[1][1], casillas[0][3], casillas[2][3], casillas[1][5]));
        casillas[1][5].agregarAdyacentes(Arrays.asList(casillas[1][3], casillas[3][5]));
        casillas[2][2].agregarAdyacentes(Arrays.asList(casillas[2][3], casillas[3][2]));
        casillas[2][3].agregarAdyacentes(Arrays.asList(casillas[2][2], casillas[1][3], casillas[2][4]));
        casillas[2][4].agregarAdyacentes(Arrays.asList(casillas[2][3], casillas[3][4]));
        casillas[3][0].agregarAdyacentes(Arrays.asList(casillas[0][0], casillas[3][1], casillas[6][0]));
        casillas[3][1].agregarAdyacentes(Arrays.asList(casillas[3][0], casillas[1][1], casillas[5][1], casillas[3][2]));
        casillas[3][2].agregarAdyacentes(Arrays.asList(casillas[3][1], casillas[2][2], casillas[4][2]));
        casillas[3][4].agregarAdyacentes(Arrays.asList(casillas[2][4], casillas[4][4], casillas[3][5]));
        casillas[3][5].agregarAdyacentes(Arrays.asList(casillas[3][4], casillas[1][5], casillas[5][5], casillas[3][6]));
        casillas[3][6].agregarAdyacentes(Arrays.asList(casillas[3][5], casillas[0][6], casillas[6][6]));
        casillas[4][2].agregarAdyacentes(Arrays.asList(casillas[3][2], casillas[4][3]));
        casillas[4][3].agregarAdyacentes(Arrays.asList(casillas[4][2], casillas[4][4], casillas[5][3]));
        casillas[4][4].agregarAdyacentes(Arrays.asList(casillas[4][3], casillas[3][4]));
        casillas[5][1].agregarAdyacentes(Arrays.asList(casillas[3][1], casillas[5][3]));
        casillas[5][3].agregarAdyacentes(Arrays.asList(casillas[5][1], casillas[4][3], casillas[6][3], casillas[5][5]));
        casillas[5][5].agregarAdyacentes(Arrays.asList(casillas[5][3], casillas[3][5]));
        casillas[6][0].agregarAdyacentes(Arrays.asList(casillas[3][0], casillas[6][3]));
        casillas[6][3].agregarAdyacentes(Arrays.asList(casillas[6][0], casillas[5][3], casillas[6][6]));
        casillas[6][6].agregarAdyacentes(Arrays.asList(casillas[6][3], casillas[3][6]));
    }

    public EstadoCasilla obtenerEstadoCasilla(Coordenada coordenada) {
        return casillas[coordenada.getFila()][coordenada.getColumna()].getEstadoCasilla();
    }

    public Casilla getCasilla(Coordenada coordenada) {
        return casillas[coordenada.getFila()][coordenada.getColumna()];
    }

    /**
     * Colocar una ficha en una casilla dentro del tablero.
     *
     * @param coordenada Coordenada de la nueva ubicación.
     * @param ficha      Ficha a colocar en el tablero.
     */
    public void colocarFicha(Coordenada coordenada, Ficha ficha) {
        casillas[coordenada.getFila()][coordenada.getColumna()].colocarFicha(ficha);
    }

    /**
     * Se quita una ficha del tablero
     *
     * @param coordenada de la ficha
     */
    public void quitarFicha(Coordenada coordenada) {
        casillas[coordenada.getFila()][coordenada.getColumna()].quitarFicha();
    }

    public void moverFicha(Coordenada antiguaCoord, Coordenada nuevaCoord) {
        Ficha ficha = obtenerFicha(antiguaCoord);
        quitarFicha(antiguaCoord);
        colocarFicha(nuevaCoord, ficha);
    }

    public Ficha obtenerFicha(Coordenada coordenada) {
        return casillas[coordenada.getFila()][coordenada.getColumna()].getFicha();
    }

    public List<Casilla> obtenerCasillasOcupadasPorJugador(Jugador jugador) {
        List<Casilla> casillasOcupadas = new ArrayList<>();
        for (int i = 0; i < CANT_FILAS; i++) {
            for (int j = 0; j < CANT_COLUMNAS; j++) {
                Casilla casillaActual = casillas[i][j];
                if (casillaActual.getEstadoCasilla().equals(EstadoCasilla.OCUPADA)) {
                    if (casillaActual.getFicha().getJugador().equals(jugador)) {
                        casillasOcupadas.add(casillaActual);
                    }
                }
            }
        }
        return casillasOcupadas;
    }

    public int getCountFilas() {
        return CANT_FILAS;
    }

    public int getCountColumnas() {
        return CANT_COLUMNAS;
    }
}
