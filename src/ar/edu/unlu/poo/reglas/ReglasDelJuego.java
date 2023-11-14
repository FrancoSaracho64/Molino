package ar.edu.unlu.poo.reglas;

import ar.edu.unlu.poo.enumerados.EstadoCasilla;
import ar.edu.unlu.poo.modelos.Coordenada;
import ar.edu.unlu.poo.modelos.Ficha;
import ar.edu.unlu.poo.modelos.Jugador;
import ar.edu.unlu.poo.modelos.Tablero;

import java.util.ArrayList;
import java.util.List;

public class ReglasDelJuego {
    private Tablero tablero;

    public ReglasDelJuego(Tablero tablero) {
        this.tablero = tablero;
    }

    /**
     *
     * @param coordenada
     * @return
     */
    public boolean esCasillaValida(Coordenada coordenada) {
        // Verifica si la casilla está dentro de los límites del tablero.
        int fila = coordenada.getFila();
        int columna = coordenada.getColumna();
        if (fila < 0 || fila >= tablero.getCountFilas() || columna < 0 || columna >= tablero.getCountColumnas()) {
            return false;
        }
        if (tablero.obtenerCasilla(fila, columna).getEstadoCasilla().equals(EstadoCasilla.INVALIDA))
            return false;
        // Si no se cumple ninguna de las condiciones anteriores, la casilla se considera válida.
        return true;
    }

    public boolean jugadorTieneMovimientos(Jugador jugador) {
        // Obtener las posiciones ocupadas por las fichas del jugador
        List<Coordenada> posicionesOcupadas = obtenerPosicionesOcupadasPorJugador(jugador);
        // Verificar movimientos posibles para cada posición ocupada
        for (Coordenada posicion : posicionesOcupadas) {
            if (hayMovimientosPosibles(posicion)) {
                return true;  // El jugador tiene al menos un movimiento posible
            }
        }
        // Si no se encontraron movimientos posibles
        return false;
    }

    private List<Coordenada> obtenerPosicionesOcupadasPorJugador(Jugador jugador) {
        List<Coordenada> posicionesOcupadas = new ArrayList<>();
        for (int fila = 0; fila < tablero.getCountFilas(); fila++) {
            for (int columna = 0; columna < tablero.getCountColumnas(); columna++) {
                if (tablero.obtenerCasilla(fila, columna).getEstadoCasilla() == EstadoCasilla.OCUPADA) {
                    Ficha ficha = tablero.obtenerFicha(fila, columna);
                    if (ficha.getJugador() == jugador) {
                        posicionesOcupadas.add(new Coordenada(fila, columna));
                    }
                }
            }
        }
        return posicionesOcupadas;
    }

    /**
     * Con este metodo se debe obtener todos las sus casillas adyacentes.
     * Luego, determinar si son validas o no.
     * Si son validas, se debe analizar por si es posible o no moverse ahí.
     * @param posicion La posicion que viene por parametro corresponde a una casilla ocupara por el usuario.
     * @return Se retorna true si hay movimientos. False si no hay.
     */
    private boolean hayMovimientosPosibles(Coordenada posicion) {
        // Verifica las posiciones adyacentes y verifica si se pueden mover a ellas
        for (Coordenada adyacente : obtenerPosicionesAdyacentes(posicion)) {
            if (esCasillaValida(adyacente)) {
                if (tablero.obtenerCasilla(adyacente.getFila(), adyacente.getColumna()).getEstadoCasilla().
                    equals(EstadoCasilla.LIBRE)) {
                    return true;  // Hay al menos un movimiento posible
                }
            }
        }
        // Si no se encontraron movimientos posibles
        return false;
    }

    /**
     * @param posicion
     * @return Se retornan las posiciones adyacentes: ARRIBA, ABAJO, IZQUIERDA, DERECHA
     */
    public static Coordenada[] obtenerPosicionesAdyacentes(Coordenada posicion) {
        int fila = posicion.getFila();
        int columna = posicion.getColumna();

        Coordenada arriba = null;
        Coordenada abajo = null;
        Coordenada izquierda = null;
        Coordenada derecha = null;

        // Definir las coordenadas de las posiciones adyacentes
        // TODO---> Hay que analizar cada situacion. Desde cada lugar hay que avanzar mas o menos pasos.
        switch (columna){
            case 0, 12 -> {
                arriba = new Coordenada(fila + 6, columna);
                abajo = new Coordenada(fila - 6, columna);
            }
            case 2, 10 -> {
                arriba = new Coordenada(fila + 4, columna);
                abajo = new Coordenada(fila - 4, columna);
            }
            case 4, 8, 6 -> {
                arriba = new Coordenada(fila + 2, columna);
                abajo = new Coordenada(fila - 2, columna);
            }
        }
        switch (fila){
            case 0, 12 -> {
                izquierda = new Coordenada(fila, columna + 6);
                derecha = new Coordenada(fila, columna + 6);
            }
            case 2, 10 -> {
                izquierda = new Coordenada(fila, columna + 4);
                derecha = new Coordenada(fila, columna + 4);
            }
            case 4, 8, 6 -> {
                izquierda = new Coordenada(fila, columna + 2);
                derecha = new Coordenada(fila, columna + 2);
            }
        }
        // Devolver un array con las coordenadas adyacentes
        return new Coordenada[]{arriba, abajo, izquierda, derecha};
    }

    public boolean esPosAdyacente(Coordenada original, Coordenada destino){
        boolean esAdyacente = false;

        //TODO: queda pendiente hacer este

        return esAdyacente;
    }

    public boolean hayMolinoEnPosicion(int fila, int columna, Jugador jugador) {
        // Verifica si hay un molino en la fila
        switch (fila) {
            case 0, 12 -> {
                if(tablero.obtenerCasilla(fila, 0).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                        tablero.obtenerCasilla(fila, 6).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                        tablero.obtenerCasilla(fila, 12).getEstadoCasilla() == EstadoCasilla.OCUPADA) {
                    if (tablero.obtenerFicha(fila, 0).getJugador() == jugador &&
                            tablero.obtenerFicha(fila, 6).getJugador() == jugador &&
                            tablero.obtenerFicha(fila, 12).getJugador() == jugador) {
                        return true;
                    }
                }
            }
            case 2, 10 -> {
                if(tablero.obtenerCasilla(fila, 2).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                        tablero.obtenerCasilla(fila, 6).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                        tablero.obtenerCasilla(fila, 10).getEstadoCasilla() == EstadoCasilla.OCUPADA) {
                    if (tablero.obtenerFicha(fila, 2).getJugador() == jugador &&
                            tablero.obtenerFicha(fila, 6).getJugador() == jugador &&
                            tablero.obtenerFicha(fila, 10).getJugador() == jugador) {
                        return true;
                    }
                }
            }
            case 4, 8 -> {
                if(tablero.obtenerCasilla(fila, 4).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                        tablero.obtenerCasilla(fila, 6).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                        tablero.obtenerCasilla(fila, 8).getEstadoCasilla() == EstadoCasilla.OCUPADA) {
                    if (tablero.obtenerFicha(fila, 4).getJugador() == jugador &&
                            tablero.obtenerFicha(fila, 6).getJugador() == jugador &&
                            tablero.obtenerFicha(fila, 8).getJugador() == jugador) {
                        return true;
                    }
                }
            }
            case 6 -> {
                if(tablero.obtenerCasilla(fila, 0).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                        tablero.obtenerCasilla(fila, 2).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                        tablero.obtenerCasilla(fila, 4).getEstadoCasilla() == EstadoCasilla.OCUPADA) {
                    if (tablero.obtenerFicha(fila, 0).getJugador() == jugador &&
                            tablero.obtenerFicha(fila, 2).getJugador() == jugador &&
                            tablero.obtenerFicha(fila, 4).getJugador() == jugador) {
                        return true;
                    }
                }
                if(tablero.obtenerCasilla(fila, 8).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                        tablero.obtenerCasilla(fila, 10).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                        tablero.obtenerCasilla(fila, 12).getEstadoCasilla() == EstadoCasilla.OCUPADA) {
                    if (tablero.obtenerFicha(fila, 8).getJugador() == jugador &&
                            tablero.obtenerFicha(fila, 10).getJugador() == jugador &&
                            tablero.obtenerFicha(fila, 12).getJugador() == jugador) {
                        return true;
                    }
                }
            }
        }
        // Verifica si hay un molino en la columna
        switch (columna) {
            case 0, 12 -> {
                if(tablero.obtenerCasilla(0, columna).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                        tablero.obtenerCasilla(6, columna).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                        tablero.obtenerCasilla(12, columna).getEstadoCasilla() == EstadoCasilla.OCUPADA) {
                    if (tablero.obtenerFicha(0, columna).getJugador() == jugador &&
                            tablero.obtenerFicha(6, columna).getJugador() == jugador &&
                            tablero.obtenerFicha(12, columna).getJugador() == jugador) {
                        return true;
                    }
                }
            }
            case 2, 10 -> {
                if(tablero.obtenerCasilla(2, columna).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                        tablero.obtenerCasilla(6, columna).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                        tablero.obtenerCasilla(10, columna).getEstadoCasilla() == EstadoCasilla.OCUPADA) {
                    if (tablero.obtenerFicha(2, columna).getJugador() == jugador &&
                            tablero.obtenerFicha(6, columna).getJugador() == jugador &&
                            tablero.obtenerFicha(10, columna).getJugador() == jugador) {
                        return true;
                    }
                }
            }
            case 4, 8 -> {
                if(tablero.obtenerCasilla(4, columna).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                        tablero.obtenerCasilla(6, columna).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                        tablero.obtenerCasilla(8, columna).getEstadoCasilla() == EstadoCasilla.OCUPADA) {
                    if (tablero.obtenerFicha(4, columna).getJugador() == jugador &&
                            tablero.obtenerFicha(6, columna).getJugador() == jugador &&
                            tablero.obtenerFicha(8, columna).getJugador() == jugador) {
                        return true;
                    }
                }
            }
            case 6 -> {
                if(tablero.obtenerCasilla(0, columna).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                        tablero.obtenerCasilla(2, columna).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                        tablero.obtenerCasilla(4, columna).getEstadoCasilla() == EstadoCasilla.OCUPADA) {
                    if (tablero.obtenerFicha(0, columna).getJugador() == jugador &&
                            tablero.obtenerFicha(2, columna).getJugador() == jugador &&
                            tablero.obtenerFicha(4, columna).getJugador() == jugador) {
                        return true;
                    }
                }
                if(tablero.obtenerCasilla(8, columna).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                        tablero.obtenerCasilla(10, columna).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                        tablero.obtenerCasilla(12, columna).getEstadoCasilla() == EstadoCasilla.OCUPADA) {
                    if (tablero.obtenerFicha(8, columna).getJugador() == jugador &&
                            tablero.obtenerFicha(10, columna).getJugador() == jugador &&
                            tablero.obtenerFicha(12, columna).getJugador() == jugador) {
                        return true;
                    }
                }
            }
        }
        // Verifica si hay molino en las diagonales
        switch (fila){
            case 0, 2, 4 ->{
                if(columna == 0 || columna == 2 || columna == 4) {
                    //Lado izquierdo arriba
                    if (tablero.obtenerCasilla(0, 0).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                            tablero.obtenerCasilla(2, 2).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                            tablero.obtenerCasilla(4, 4).getEstadoCasilla() == EstadoCasilla.OCUPADA) {
                        if (tablero.obtenerFicha(0, 0).getJugador() == jugador &&
                                tablero.obtenerFicha(2, 2).getJugador() == jugador &&
                                tablero.obtenerFicha(4, 4).getJugador() == jugador) {
                            return true;
                        }
                    }
                } else {
                    //Lado derecho arriba
                    if (tablero.obtenerCasilla(0, 12).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                            tablero.obtenerCasilla(2, 10).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                            tablero.obtenerCasilla(4, 8).getEstadoCasilla() == EstadoCasilla.OCUPADA) {
                        if (tablero.obtenerFicha(0, 12).getJugador() == jugador &&
                                tablero.obtenerFicha(2, 10).getJugador() == jugador &&
                                tablero.obtenerFicha(4, 8).getJugador() == jugador) {
                            return true;
                        }
                    }
                }
            }
            case 12, 10, 8 ->{
                //Lado izquierdo abajo
                if(columna == 0 || columna == 2 || columna == 4) {
                    if (tablero.obtenerCasilla(12, 0).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                            tablero.obtenerCasilla(10, 2).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                            tablero.obtenerCasilla(8, 4).getEstadoCasilla() == EstadoCasilla.OCUPADA) {
                        if (tablero.obtenerFicha(12, 0).getJugador() == jugador &&
                                tablero.obtenerFicha(10, 2).getJugador() == jugador &&
                                tablero.obtenerFicha(8, 4).getJugador() == jugador) {
                            return true;
                        }
                    }
                } else {
                    //Lado derecho abajo
                    if (tablero.obtenerCasilla(12, 12).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                            tablero.obtenerCasilla(10, 10).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                            tablero.obtenerCasilla(8, 8).getEstadoCasilla() == EstadoCasilla.OCUPADA) {
                        if (tablero.obtenerFicha(12, 12).getJugador() == jugador &&
                                tablero.obtenerFicha(10, 10).getJugador() == jugador &&
                                tablero.obtenerFicha(8, 8).getJugador() == jugador) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public Jugador obtenerGanador(Jugador j1, Jugador j2) {
        if (j1.getFichasColocadas() == 2)
            return j2;
        if (j2.getFichasColocadas() == 2)
            return j1;
        if (!jugadorTieneMovimientos(j1))
            return j2;
        if (!jugadorTieneMovimientos(j2))
            return j1;
        // Si no se cumplen otras condiciones, se podría considerar un empate
        return null; // No hay ganador definido
    }

    public boolean sonCasillasAdyacentes(Coordenada posFichaSelec, Coordenada nuevaPosFichaSelec) {
        for (Coordenada adyacente : obtenerPosicionesAdyacentes(posFichaSelec)) {
            if (adyacente.equals(nuevaPosFichaSelec))
                return true;
        }
        return false;
    }

    public boolean fichaTieneMovimiento(Coordenada posFichaSelec) {
        return hayMovimientosPosibles(posFichaSelec);
    }
}
