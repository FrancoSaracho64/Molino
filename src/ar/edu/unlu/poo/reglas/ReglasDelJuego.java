package ar.edu.unlu.poo.reglas;

import ar.edu.unlu.poo.enumerados.EstadoCasilla;
import ar.edu.unlu.poo.modelos.Coordenada;
import ar.edu.unlu.poo.modelos.Ficha;
import ar.edu.unlu.poo.modelos.Jugador;
import ar.edu.unlu.poo.modelos.Tablero;

import java.util.ArrayList;

public class ReglasDelJuego {
    private Tablero tablero;

    public ReglasDelJuego(Tablero tablero) {
        this.tablero = tablero;
    }

    public boolean esCasillaValida(Coordenada coordenada) {
        // Verifica si la casilla está dentro de los límites del tablero.
        int fila = coordenada.getFila();
        int columna = coordenada.getColumna();
        if (fila < 0 || fila >= tablero.getCountFilas() || columna < 0 || columna >= tablero.getCountColumnas()) {
            return false;
        }
        if (tablero.obtenerCasilla(fila, columna).getEstadoCasilla().equals(EstadoCasilla.INVALIDA)
                || tablero.obtenerCasilla(fila, columna).getEstadoCasilla().equals(EstadoCasilla.OCUPADA))
            return false;
        // Si no se cumple ninguna de las condiciones anteriores, la casilla se considera válida.
        return true;
    }

    /**
     * Se verifica si el jugador tiene 3 o más fichas.
     * @param ju jugador
     * @return true si tiene fichas // false si no tiene fichas
     */
    public boolean tienePiezasSuficientes(Jugador ju){
        //    --- Un jugador tiene 2 fichas vivas
        int tamanio = tablero.getCountColumnas();
        int cantJ = 0;
        for (int fila = 0; fila < tamanio; fila++){
            for (int columna = 0; columna < tamanio; columna++){
                if (tablero.obtenerCasilla(fila, columna).getEstadoCasilla() == EstadoCasilla.OCUPADA) {
                    if (tablero.obtenerFicha(fila, columna).getJugador() == ju)
                        cantJ++;
                }
            }
        }
        return cantJ > 2;
    }

    public boolean jugadorTieneMovimientos(Jugador jugador) {
        // Obtener las posiciones ocupadas por las fichas del jugador
        ArrayList<Coordenada> posicionesOcupadas = obtenerPosicionesOcupadasPorJugador(jugador);

        // Verificar movimientos posibles para cada posición ocupada
        for (Coordenada posicion : posicionesOcupadas) {
            if (hayMovimientosPosibles(posicion)) {
                return true;  // El jugador tiene al menos un movimiento posible
            }
        }

        // Si no se encontraron movimientos posibles
        return false;
    }

    private ArrayList<Coordenada> obtenerPosicionesOcupadasPorJugador(Jugador jugador) {
        ArrayList<Coordenada> posicionesOcupadas = new ArrayList<>();
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

    private boolean hayMovimientosPosibles(Coordenada posicion) {
        // Implementa la lógica para verificar si hay movimientos posibles desde la posición dada
        // Puedes revisar las posiciones adyacentes, o saltos, según las reglas del juego
        // Devuelve true si hay al menos un movimiento posible, false si no hay movimientos
        // ...
        return false;
    }
    //Asegúrate de adaptar la lógica de hayMovimientosPosibles según las reglas específicas de tu implementación del juego del molino. Puedes considerar las posiciones adyacentes o cualquier otra regla específica que aplique. Además, puedes expandir esta lógica para tener en cuenta diferentes fases del juego, como la fase de colocación de fichas y la fase de movimiento.


    //    --- Un jugador NO tiene movimientos posibles.

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
                //Lado izquierdo arriba
                if(tablero.obtenerCasilla(0, 0).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                        tablero.obtenerCasilla(2, 2).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                        tablero.obtenerCasilla(4, 4).getEstadoCasilla() == EstadoCasilla.OCUPADA){
                    if (tablero.obtenerFicha(0, 0).getJugador() == jugador &&
                            tablero.obtenerFicha(2, 2).getJugador() == jugador &&
                            tablero.obtenerFicha(4, 4).getJugador() == jugador) {
                        return true;
                    }
                }
                //Lado derecho arriba
                if(tablero.obtenerCasilla(0, 12).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                        tablero.obtenerCasilla(2, 10).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                        tablero.obtenerCasilla(4, 8).getEstadoCasilla() == EstadoCasilla.OCUPADA){
                    if (tablero.obtenerFicha(0, 12).getJugador() == jugador &&
                            tablero.obtenerFicha(2, 10).getJugador() == jugador &&
                            tablero.obtenerFicha(4, 8).getJugador() == jugador) {
                        return true;
                    }
                }
            }
            case 12, 10, 8 ->{
                //Lado izquierdo abajo
                if(tablero.obtenerCasilla(12, 0).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                        tablero.obtenerCasilla(10, 2).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                        tablero.obtenerCasilla(8, 4).getEstadoCasilla() == EstadoCasilla.OCUPADA){
                    if (tablero.obtenerFicha(12, 0).getJugador() == jugador &&
                            tablero.obtenerFicha(10, 2).getJugador() == jugador &&
                            tablero.obtenerFicha(8, 4).getJugador() == jugador) {
                        return true;
                    }
                }
                //Lado derecho abajo
                if(tablero.obtenerCasilla(12, 12).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                        tablero.obtenerCasilla(10, 10).getEstadoCasilla() == EstadoCasilla.OCUPADA &&
                        tablero.obtenerCasilla(8, 8).getEstadoCasilla() == EstadoCasilla.OCUPADA){
                    if (tablero.obtenerFicha(12, 12).getJugador() == jugador &&
                            tablero.obtenerFicha(10, 10).getJugador() == jugador &&
                            tablero.obtenerFicha(8, 8).getJugador() == jugador) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Jugador obtenerGanador(Jugador j1, Jugador j2) {

        return j1; // j1 o j2
    }
}
