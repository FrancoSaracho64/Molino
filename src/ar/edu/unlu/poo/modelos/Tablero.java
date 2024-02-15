package ar.edu.unlu.poo.modelos;

import ar.edu.unlu.poo.enumerados.EstadoCasilla;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tablero implements Serializable {
    private static final int CANT_COLUMNAS = 7;
    private static final int CANT_FILAS = 7;
    private final ArrayList<Casilla> casillas;

    public Tablero() {
        casillas = new ArrayList<>();
        inicializarCasillas();
        conectarCasillas();
    }

    /**
     * Se crean todas las casillas con su correspondiente coordenada.
     * A medida que se crean, se agregan a la lista contenedora de todas las casillas.
     */
    private void inicializarCasillas() {
        casillas.add(new Casilla(new Coordenada(0, 0)));
        casillas.add(new Casilla(new Coordenada(0, 3)));
        casillas.add(new Casilla(new Coordenada(0, 6)));
        casillas.add(new Casilla(new Coordenada(1, 1)));
        casillas.add(new Casilla(new Coordenada(1, 3)));
        casillas.add(new Casilla(new Coordenada(1, 5)));
        casillas.add(new Casilla(new Coordenada(2, 2)));
        casillas.add(new Casilla(new Coordenada(2, 3)));
        casillas.add(new Casilla(new Coordenada(2, 4)));
        casillas.add(new Casilla(new Coordenada(3, 0)));
        casillas.add(new Casilla(new Coordenada(3, 1)));
        casillas.add(new Casilla(new Coordenada(3, 2)));
        casillas.add(new Casilla(new Coordenada(3, 4)));
        casillas.add(new Casilla(new Coordenada(3, 5)));
        casillas.add(new Casilla(new Coordenada(3, 6)));
        casillas.add(new Casilla(new Coordenada(4, 2)));
        casillas.add(new Casilla(new Coordenada(4, 3)));
        casillas.add(new Casilla(new Coordenada(4, 4)));
        casillas.add(new Casilla(new Coordenada(5, 1)));
        casillas.add(new Casilla(new Coordenada(5, 3)));
        casillas.add(new Casilla(new Coordenada(5, 5)));
        casillas.add(new Casilla(new Coordenada(6, 0)));
        casillas.add(new Casilla(new Coordenada(6, 3)));
        casillas.add(new Casilla(new Coordenada(6, 6)));
    }

    /**
     * Se crean las conexiones entre las conexiones entre las casillas que forman el tablero..
     */
    private void conectarCasillas() {
        //Obtenemos todas las casillas
        Casilla c00 = getCasilla(new Coordenada(0, 0));
        Casilla c03 = getCasilla(new Coordenada(0, 3));
        Casilla c06 = getCasilla(new Coordenada(0, 6));
        Casilla c11 = getCasilla(new Coordenada(1, 1));
        Casilla c13 = getCasilla(new Coordenada(1, 3));
        Casilla c15 = getCasilla(new Coordenada(1, 5));
        Casilla c22 = getCasilla(new Coordenada(2, 2));
        Casilla c23 = getCasilla(new Coordenada(2, 3));
        Casilla c24 = getCasilla(new Coordenada(2, 4));
        Casilla c30 = getCasilla(new Coordenada(3, 0));
        Casilla c31 = getCasilla(new Coordenada(3, 1));
        Casilla c32 = getCasilla(new Coordenada(3, 2));
        Casilla c34 = getCasilla(new Coordenada(3, 4));
        Casilla c35 = getCasilla(new Coordenada(3, 5));
        Casilla c36 = getCasilla(new Coordenada(3, 6));
        Casilla c42 = getCasilla(new Coordenada(4, 2));
        Casilla c43 = getCasilla(new Coordenada(4, 3));
        Casilla c44 = getCasilla(new Coordenada(4, 4));
        Casilla c51 = getCasilla(new Coordenada(5, 1));
        Casilla c53 = getCasilla(new Coordenada(5, 3));
        Casilla c55 = getCasilla(new Coordenada(5, 5));
        Casilla c60 = getCasilla(new Coordenada(6, 0));
        Casilla c63 = getCasilla(new Coordenada(6, 3));
        Casilla c66 = getCasilla(new Coordenada(6, 6));
        // A cada casilla, le asignamos sus adyacentes.
        c00.agregarAdyacentes(Arrays.asList(c03, c30));
        c03.agregarAdyacentes(Arrays.asList(c00, c13, c06));
        c06.agregarAdyacentes(Arrays.asList(c03, c36));
        c11.agregarAdyacentes(Arrays.asList(c31, c13));
        c13.agregarAdyacentes(Arrays.asList(c11, c03, c23, c15));
        c15.agregarAdyacentes(Arrays.asList(c13, c35));
        c22.agregarAdyacentes(Arrays.asList(c32, c23));
        c23.agregarAdyacentes(Arrays.asList(c22, c13, c24));
        c24.agregarAdyacentes(Arrays.asList(c23, c34));
        c30.agregarAdyacentes(Arrays.asList(c00, c31, c60));
        c31.agregarAdyacentes(Arrays.asList(c30, c11, c51, c32));
        c32.agregarAdyacentes(Arrays.asList(c31, c22, c42));
        c34.agregarAdyacentes(Arrays.asList(c24, c44, c35));
        c35.agregarAdyacentes(Arrays.asList(c34, c15, c55, c36));
        c36.agregarAdyacentes(Arrays.asList(c35, c06, c66));
        c42.agregarAdyacentes(Arrays.asList(c32, c43));
        c43.agregarAdyacentes(Arrays.asList(c42, c53, c44));
        c44.agregarAdyacentes(Arrays.asList(c43, c34));
        c51.agregarAdyacentes(Arrays.asList(c31, c53));
        c53.agregarAdyacentes(Arrays.asList(c51, c43, c63, c55));
        c55.agregarAdyacentes(Arrays.asList(c53, c35));
        c60.agregarAdyacentes(Arrays.asList(c30, c63));
        c63.agregarAdyacentes(Arrays.asList(c60, c53, c55));
        c66.agregarAdyacentes(Arrays.asList(c63, c36));
    }

    public Casilla getCasilla(Coordenada coordenada) {
        for (Casilla casilla : casillas) {
            if (casilla.getCoordenada().equals(coordenada)) {
                return casilla;
            }
        }
        return new Casilla();
    }

    public EstadoCasilla obtenerEstadoCasilla(Coordenada coordenada) {
        return getCasilla(coordenada).getEstadoCasilla();
    }

    /**
     * Colocar una ficha en una casilla dentro del tablero.
     *
     * @param coordenada Coordenada de la ubicación.
     * @param ficha      Ficha a colocar en el tablero.
     */
    public void colocarFicha(Coordenada coordenada, Ficha ficha) {
        getCasilla(coordenada).colocarFicha(ficha);
    }

    /**
     * Quitar una ficha  de yna casilla dentro del tablero.
     *
     * @param coordenada Coordenada de la ficha.
     */
    public void quitarFicha(Coordenada coordenada) {
        getCasilla(coordenada).quitarFicha();
    }

    /**
     * Desplazamos la ficha que se ubica en la primer coordenada a la segunda coordenada.
     * @param cOrigen Coordenada de origen.
     * @param cDestino Coordenada de destino.
     */
    public void moverFicha(Coordenada cOrigen, Coordenada cDestino) {
        Ficha ficha = obtenerFicha(cOrigen);
        quitarFicha(cOrigen);
        colocarFicha(cDestino, ficha);
    }

    public Ficha obtenerFicha(Coordenada coordenada) {
        return getCasilla(coordenada).getFicha();
    }

    public List<Casilla> obtenerCasillasOcupadasPorJugador(Jugador jugador) {
        List<Casilla> casillasOcupadas = new ArrayList<>();
        for (Casilla casilla : casillas) {
            if (casilla.getEstadoCasilla().equals(EstadoCasilla.OCUPADA)) {
                if (casilla.getFicha().getJugador().equals(jugador)) {
                    casillasOcupadas.add(casilla);
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
