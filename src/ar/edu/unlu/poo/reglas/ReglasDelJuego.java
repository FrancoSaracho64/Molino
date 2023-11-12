package ar.edu.unlu.poo.reglas;

import ar.edu.unlu.poo.enumerados.EstadoCasilla;
import ar.edu.unlu.poo.modelos.Casilla;
import ar.edu.unlu.poo.modelos.Coordenada;
import ar.edu.unlu.poo.modelos.Jugador;
import ar.edu.unlu.poo.modelos.Tablero;

public class ReglasDelJuego {

    public ReglasDelJuego() {
    }

    public boolean esCasillaValida(Coordenada coordenada, Tablero tablero) {
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
     * Se verifica si el jugador tiene 3 o mas fichas.
     * @param t tablero
     * @param ju jugador
     * @return true si tiene fichas // false si no tiene fichas
     */
    public boolean tienePiezasSuficientes(Tablero t, Jugador ju){
        //Casos de finalizacion de la partida:
        //    --- Un jugador tiene 2 fichas vivas
        int tamanio = t.getCountColumnas();
        int cantJ = 0;
        for (int i = 0; i < tamanio; i++){
            for (int j = 0; j < tamanio; j++){
                if (t.obtenerCasilla(i, j).getFicha().getJugador() == ju)
                    cantJ++;
            }
        }
        return cantJ != 2;
    }

    public boolean tieneMovimientosDisponibles(Tablero t, Jugador ju){
        //Casos de finalizacion de la partida:
        //    --- Un jugador tiene 2 fichas vivas
        int tamanio = t.getCountColumnas();
        int cantJ = 0;
        for (int i = 0; i < tamanio; i++){
            for (int j = 0; j < tamanio; j++){
                if (t.obtenerCasilla(i, j).getFicha().getJugador() == ju)
                    cantJ++;
            }
        }
        return cantJ != 2;
    }
    //    --- Un jugador NO tiene movimientos posibles.



    /*public boolean verificarMolino(Tablero tablero, int fila, int columna, Jugador jugador) {
        // Verificar si se ha formado un molino en la posición dada

        // Verificar fila
        if (verificarLinea(tablero.obtenerCasilla(fila), jugador)) {
            return true;
        }

        // Verificar columna
        Casilla[] columnaArray = new Casilla[filas];
        for (int i = 0; i < filas; i++) {
            columnaArray[i] = tablero[i][columna];
        }
        if (verificarLinea(columnaArray, jugador)) {
            return true;
        }

        // Verificar diagonales solo si la posición está en una esquina o en el centro
        if ((fila == 0 || fila == filas - 1 || fila == filas / 2) &&
                (columna == 0 || columna == columnas - 1 || columna == columnas / 2)) {
            // Verificar diagonales
            if (verificarDiagonalPrincipal(jugador) || verificarDiagonalSecundaria(jugador)) {
                return true;
            }
        }

        return false;
    }*/

    public boolean hayMolinoEnPosicion(int fila, int columna, Jugador jugador, Tablero tablero) {
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
        /*switch (columna) {
            case 0, 12 -> {
                if (tablero.obtenerFicha(0, columna).getJugador() == jugador &&
                        tablero.obtenerFicha(2, columna).getJugador() == jugador &&
                        tablero.obtenerFicha(4, columna).getJugador() == jugador) {
                    return true;
                }
            }
            case 2, 10 -> {
                if (tablero.obtenerFicha(2, columna).getJugador() == jugador &&
                        tablero.obtenerFicha(6, columna).getJugador() == jugador &&
                        tablero.obtenerFicha(10, columna).getJugador() == jugador) {
                    return true;
                }
            }
            case 4, 8 -> {
                if (tablero.obtenerFicha(4, columna).getJugador() == jugador &&
                        tablero.obtenerFicha(6, columna).getJugador() == jugador &&
                        tablero.obtenerFicha(8, columna).getJugador() == jugador) {
                    return true;
                }
            }
            case 6 -> {
                if ((tablero.obtenerFicha(0, columna).getJugador() == jugador &&
                        tablero.obtenerFicha(2, columna).getJugador() == jugador &&
                        tablero.obtenerFicha(4, columna).getJugador() == jugador) ||
                        (tablero.obtenerFicha(8, columna).getJugador() == jugador &&
                                tablero.obtenerFicha(10, columna).getJugador() == jugador &&
                                tablero.obtenerFicha(12, columna).getJugador() == jugador)) {
                    return true;
                }
            }
        }*/

        /*// Verifica si hay un molino en la diagonal principal
        if (fila == columna && tablero[0][0] == jugador && tablero[1][1] == jugador && tablero[2][2] == jugador) {
            return true;
        }

        // Verifica si hay un molino en la diagonal secundaria
        if (fila + columna == 2 && tablero[0][2] == jugador && tablero[1][1] == jugador && tablero[2][0] == jugador) {
            return true;
        }*/

        return false;
    }

    /*private boolean verificarLinea(Casilla[] linea, Jugador jugador) {
        for (Casilla casilla : linea) {
            if (casilla == null || casilla.getJugador() != jugador) {
                return false;
            }
        }
        return true;
    }*/

    private boolean verificarDiagonalPrincipal(Jugador jugador) {
        // Lógica para verificar la diagonal principal
        return false;
    }

    private boolean verificarDiagonalSecundaria(Jugador jugador) {
        // Lógica para verificar la diagonal secundaria
        return false;
    }
}
