package ar.edu.unlu.poo.reglas;

import ar.edu.unlu.poo.enumerados.EstadoCasilla;
import ar.edu.unlu.poo.modelos.*;

import java.util.ArrayList;
import java.util.List;

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
        return !tablero.obtenerEstadoCasilla(fila, columna).equals(EstadoCasilla.INVALIDA);
        // Si no se cumple ninguna de las condiciones anteriores, la casilla se considera válida.
    }

    public boolean jugadorTieneMovimientos(Jugador jugador) {
        // Obtener las posiciones ocupadas por las fichas del jugador
        List<Casilla> posicionesOcupadas = tablero.obtenerCasillasOcupadasPorJugador(jugador);
        // Verificar movimientos posibles para cada posición ocupada
        for (Casilla casilla : posicionesOcupadas) {
            if (hayMovimientosPosibles(casilla)) {
                return true;  // El jugador tiene al menos un movimiento posible
            }
        }
        // Si no se encontraron movimientos posibles
        return false;
    }

    /**
     * Con este metodo se debe obtener todas las sus casillas adyacentes.
     * Luego, determinar si son válidas o no.
     * Si son válidas, se debe analizar por si es posible o no moverse ahí.
     * @param casilla La posicion que viene por parametro corresponde a una casilla ocupara por el usuario.
     * @return Se retorna true si hay movimientos. False si no hay.
     */
    private boolean hayMovimientosPosibles(Casilla casilla) {
        // Verifica las posiciones adyacentes y verifica si se pueden mover a ellas
        ArrayList<Coordenada> coordAdyacentes = casilla.getCoordenadasAdyacentes();
        for (Coordenada coordenada : coordAdyacentes) {
            if (esCasillaValida(coordenada)) {
                if (tablero.obtenerEstadoCasilla(coordenada.getFila(), coordenada.getColumna()).
                    equals(EstadoCasilla.LIBRE)) {
                    return true;  // Hay al menos un movimiento posible
                }
            }
        }
        // Si no se encontraron movimientos posibles
        return false;
    }










    public boolean hayMolinoEnPosicion(Coordenada coordenada, Jugador jugador) {
        ArrayList<Coordenada> casillasAdyacentes = tablero.getCasilla(coordenada).getCoordenadasAdyacentes();

        // Verificar en cada dirección si se forma un molino
        for (Coordenada adyacente : casillasAdyacentes) {
            if (esFichaDelJugador(adyacente, jugador)) {
                Coordenada siguienteAdyacente = obtenerSiguienteCasillaEnDireccion(coordenada, adyacente);
                if (siguienteAdyacente != null && esFichaDelJugador(siguienteAdyacente, jugador)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean esFichaDelJugador(Coordenada coordenada, Jugador jugador) {
        Ficha ficha = tablero.getCasilla(coordenada).getFicha();
        if (ficha == null){
            return false;
        } else {
            return ficha.getJugador() == jugador;
        }
    }

    private Coordenada obtenerSiguienteCasillaEnDireccion(Coordenada origen, Coordenada adyacente) {
        int filOri = origen.getFila();
        int filAdy = adyacente.getFila();
        int columna = origen.getColumna();
        int filaComun = -1;
        if (filOri == filAdy) {
            // Las filas son iguales, entonces tomas el valor de la fila
            filaComun = filOri;
        }  // Las columnas son iguales, entonces tomas el valor de la columna

        ArrayList<Coordenada> casillasAdyacentes = tablero.getCasilla(adyacente).getCoordenadasAdyacentes();
        if (filaComun != -1){
            for (Coordenada co: casillasAdyacentes){
                if (co.getFila() == filaComun){
                    return co;
                }
            }
        } else {
            for (Coordenada co: casillasAdyacentes){
                if (co.getColumna() == columna){
                    return co;
                }
            }
        }

        // Calcula la siguiente casilla en la misma dirección formada por origen y adyacente
        // Retorna la coordenada de esa casilla o null si no es válida (fuera del tablero o no existe)
        return null;
    }












    /*public boolean hayMolinoEnPosicion(int fila, int columna, Jugador jugador) {
        // Verifica si hay un molino en la fila
        switch (fila) {
            case 0, 12 -> {
                if(tablero.obtenerEstadoCasilla(fila, 0) == EstadoCasilla.OCUPADA &&
                        tablero.obtenerEstadoCasilla(fila, 6) == EstadoCasilla.OCUPADA &&
                        tablero.obtenerEstadoCasilla(fila, 12) == EstadoCasilla.OCUPADA) {
                    if (tablero.obtenerFicha(fila, 0).getJugador() == jugador &&
                            tablero.obtenerFicha(fila, 6).getJugador() == jugador &&
                            tablero.obtenerFicha(fila, 12).getJugador() == jugador) {
                        return true;
                    }
                }
            }
            case 2, 10 -> {
                if(tablero.obtenerEstadoCasilla(fila, 2) == EstadoCasilla.OCUPADA &&
                        tablero.obtenerEstadoCasilla(fila, 6) == EstadoCasilla.OCUPADA &&
                        tablero.obtenerEstadoCasilla(fila, 10) == EstadoCasilla.OCUPADA) {
                    if (tablero.obtenerFicha(fila, 2).getJugador() == jugador &&
                            tablero.obtenerFicha(fila, 6).getJugador() == jugador &&
                            tablero.obtenerFicha(fila, 10).getJugador() == jugador) {
                        return true;
                    }
                }
            }
            case 4, 8 -> {
                if(tablero.obtenerEstadoCasilla(fila, 4) == EstadoCasilla.OCUPADA &&
                        tablero.obtenerEstadoCasilla(fila, 6) == EstadoCasilla.OCUPADA &&
                        tablero.obtenerEstadoCasilla(fila, 8) == EstadoCasilla.OCUPADA) {
                    if (tablero.obtenerFicha(fila, 4).getJugador() == jugador &&
                            tablero.obtenerFicha(fila, 6).getJugador() == jugador &&
                            tablero.obtenerFicha(fila, 8).getJugador() == jugador) {
                        return true;
                    }
                }
            }
            case 6 -> {
                if (columna == 0 || columna == 2 || columna == 4) {
                    if (tablero.obtenerEstadoCasilla(fila, 0) == EstadoCasilla.OCUPADA &&
                            tablero.obtenerEstadoCasilla(fila, 2) == EstadoCasilla.OCUPADA &&
                            tablero.obtenerEstadoCasilla(fila, 4) == EstadoCasilla.OCUPADA) {
                        if (tablero.obtenerFicha(fila, 0).getJugador() == jugador &&
                                tablero.obtenerFicha(fila, 2).getJugador() == jugador &&
                                tablero.obtenerFicha(fila, 4).getJugador() == jugador) {
                            return true;
                        }
                    }
                }
                else if (columna == 8 || columna == 10 || columna == 12) {
                    if (tablero.obtenerEstadoCasilla(fila, 8) == EstadoCasilla.OCUPADA &&
                            tablero.obtenerEstadoCasilla(fila, 10) == EstadoCasilla.OCUPADA &&
                            tablero.obtenerEstadoCasilla(fila, 12) == EstadoCasilla.OCUPADA) {
                        if (tablero.obtenerFicha(fila, 8).getJugador() == jugador &&
                                tablero.obtenerFicha(fila, 10).getJugador() == jugador &&
                                tablero.obtenerFicha(fila, 12).getJugador() == jugador) {
                            return true;
                        }
                    }
                }
            }
        }
        // Verifica si hay un molino en la columna
        switch (columna) {
            case 0, 12 -> {
                if(tablero.obtenerEstadoCasilla(0, columna) == EstadoCasilla.OCUPADA &&
                        tablero.obtenerEstadoCasilla(6, columna) == EstadoCasilla.OCUPADA &&
                        tablero.obtenerEstadoCasilla(12, columna) == EstadoCasilla.OCUPADA) {
                    if (tablero.obtenerFicha(0, columna).getJugador() == jugador &&
                            tablero.obtenerFicha(6, columna).getJugador() == jugador &&
                            tablero.obtenerFicha(12, columna).getJugador() == jugador) {
                        return true;
                    }
                }
            }
            case 2, 10 -> {
                if(tablero.obtenerEstadoCasilla(2, columna) == EstadoCasilla.OCUPADA &&
                        tablero.obtenerEstadoCasilla(6, columna) == EstadoCasilla.OCUPADA &&
                        tablero.obtenerEstadoCasilla(10, columna) == EstadoCasilla.OCUPADA) {
                    if (tablero.obtenerFicha(2, columna).getJugador() == jugador &&
                            tablero.obtenerFicha(6, columna).getJugador() == jugador &&
                            tablero.obtenerFicha(10, columna).getJugador() == jugador) {
                        return true;
                    }
                }
            }
            case 4, 8 -> {
                if(tablero.obtenerEstadoCasilla(4, columna) == EstadoCasilla.OCUPADA &&
                        tablero.obtenerEstadoCasilla(6, columna) == EstadoCasilla.OCUPADA &&
                        tablero.obtenerEstadoCasilla(8, columna) == EstadoCasilla.OCUPADA) {
                    if (tablero.obtenerFicha(4, columna).getJugador() == jugador &&
                            tablero.obtenerFicha(6, columna).getJugador() == jugador &&
                            tablero.obtenerFicha(8, columna).getJugador() == jugador) {
                        return true;
                    }
                }
            }
            case 6 -> {
                if (fila == 0 || fila == 2 || fila == 4) {
                    if (tablero.obtenerEstadoCasilla(0, columna) == EstadoCasilla.OCUPADA &&
                            tablero.obtenerEstadoCasilla(2, columna) == EstadoCasilla.OCUPADA &&
                            tablero.obtenerEstadoCasilla(4, columna) == EstadoCasilla.OCUPADA) {
                        if (tablero.obtenerFicha(0, columna).getJugador() == jugador &&
                                tablero.obtenerFicha(2, columna).getJugador() == jugador &&
                                tablero.obtenerFicha(4, columna).getJugador() == jugador) {
                            return true;
                        }
                    }
                }
                else if (fila == 8 || fila == 10 || fila == 12) {
                    if (tablero.obtenerEstadoCasilla(8, columna) == EstadoCasilla.OCUPADA &&
                            tablero.obtenerEstadoCasilla(10, columna) == EstadoCasilla.OCUPADA &&
                            tablero.obtenerEstadoCasilla(12, columna) == EstadoCasilla.OCUPADA) {
                        if (tablero.obtenerFicha(8, columna).getJugador() == jugador &&
                                tablero.obtenerFicha(10, columna).getJugador() == jugador &&
                                tablero.obtenerFicha(12, columna).getJugador() == jugador) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }*/

    public Jugador obtenerGanador(Jugador j1, Jugador j2) {
        if (j1.getFichasEnTablero() == 2)
            return j2;
        if (j2.getFichasEnTablero() == 2)
            return j1;
        if (!jugadorTieneMovimientos(j1))
            return j2;
        if (!jugadorTieneMovimientos(j2))
            return j1;
        // Si no se cumplen otras condiciones, se podría considerar un empate
        return null; // No hay ganador definido
    }

    public boolean sonCasillasAdyacentes(Coordenada posFichaSelec, Coordenada nuevaPosFichaSelec) {
        Casilla casillaSelec = tablero.getCasilla(posFichaSelec);
        ArrayList<Coordenada> coordenadas = casillaSelec.getCoordenadasAdyacentes();
        return coordenadas.contains(nuevaPosFichaSelec);
    }

    public boolean fichaTieneMovimiento(Coordenada posFichaSelec) {
        return hayMovimientosPosibles(tablero.getCasilla(posFichaSelec));
    }

    public boolean hayFichasParaEliminar(Jugador jugadorOponente) {
        for (int fila = 0; fila < tablero.getCountFilas(); fila++) {
            for (int columna = 0; columna < tablero.getCountColumnas(); columna++) {
                if (tablero.obtenerEstadoCasilla(fila, columna) == EstadoCasilla.OCUPADA) {
                    Ficha ficha = tablero.obtenerFicha(fila, columna);
                    if (ficha.getJugador() == jugadorOponente) {
                        if (!hayMolinoEnPosicion(new Coordenada(fila, columna), jugadorOponente))
                            return true;
                    }
                }
            }
        }
        return false;
    }
}
