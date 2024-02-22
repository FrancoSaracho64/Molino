package ar.edu.unlu.poo.reglas;

import ar.edu.unlu.poo.enumerados.EstadoCasilla;
import ar.edu.unlu.poo.modelos.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReglasDelJuego implements Serializable {
    private static final int CANT_COLUMNAS = 7;
    private static final int CANT_FILAS = 7;
    private final Tablero tablero;

    public ReglasDelJuego(Tablero tablero) {
        this.tablero = tablero;
    }

    public boolean esCasillaValida(Coordenada coordenada) {
        // Verifica si la casilla está dentro de los límites del tablero.
        int fila = coordenada.getFila();
        int columna = coordenada.getColumna();
        if (fila < 0 || fila >= CANT_FILAS || columna < 0 || columna >= CANT_COLUMNAS) {
            return false;
        }
        return !tablero.obtenerEstadoCasilla(new Coordenada(fila, columna)).equals(EstadoCasilla.INVALIDA);
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
     *
     * @param casilla La posicion que viene por parametro corresponde a una casilla ocupada por el usuario.
     * @return Se retorna true si hay movimientos. False si no hay.
     */
    private boolean hayMovimientosPosibles(Casilla casilla) {
        // Verifica las posiciones adyacentes y verifica si se pueden mover a ellas
        ArrayList<Coordenada> coordAdyacentes = casilla.getCoordenadasCasillasAdyacentes();
        for (Coordenada coordenada : coordAdyacentes) {
            if (esCasillaValida(coordenada)) {
                if (tablero.obtenerEstadoCasilla(coordenada).
                        equals(EstadoCasilla.LIBRE)) {
                    return true;  // Hay al menos un movimiento posible
                }
            }
        }
        // Si no se encontraron movimientos posibles
        return false;
    }

    private boolean esFichaDelJugador(Coordenada coordenada, Jugador jugador) {
        Ficha ficha = tablero.getCasilla(coordenada).getFicha();
        if (ficha == null) {
            return false;
        } else {
            return ficha.getJugador().equals(jugador);
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
        ArrayList<Coordenada> casillasAdyacentes = tablero.getCasilla(adyacente).getCoordenadasCasillasAdyacentes();
        if (filaComun != -1) {
            for (Coordenada co : casillasAdyacentes) {
                if (!co.equals(origen) && co.getFila() == filaComun) {
                    return co;
                }
            }
        } else {
            for (Coordenada co : casillasAdyacentes) {
                if (!co.equals(origen) && co.getColumna() == columna) {
                    return co;
                }
            }
        }
        // Retorna la coordenada de esa casilla o null si no es válida (fuera del tablero o no existe)
        return null;
    }

    public boolean hayMolinoEnPosicion(Coordenada coordenada, Jugador jugador) {
        ArrayList<Coordenada> casillasAdyacentes = tablero.getCasilla(coordenada).getCoordenadasCasillasAdyacentes();
        // Verificar en cada dirección si se forma un molino
        for (Coordenada adyacente : casillasAdyacentes) {
            if (esFichaDelJugador(adyacente, jugador)) {
                // Busca la siguiente casilla en la misma dirección formada por la coordenada y su adyacente
                Coordenada siguienteAdyacente = obtenerSiguienteCasillaEnDireccion(coordenada, adyacente);
                if (siguienteAdyacente != null && esFichaDelJugador(siguienteAdyacente, jugador)) {
                    return true; // Se encontró un molino en una dirección
                }
                // Buscar en la dirección opuesta
                Coordenada adyacenteOpuesta = obtenerCasillaOpuesta(coordenada, adyacente);
                if (adyacenteOpuesta != null && esFichaDelJugador(adyacenteOpuesta, jugador)) {
                    return true; // Se encontró un molino en la dirección opuesta
                }
            }
        }
        return false;
    }

    private Coordenada obtenerCasillaOpuesta(Coordenada origen, Coordenada adyacente) {
        // Calcula la dirección opuesta basándose en la diferencia entre origen y adyacente
        int deltaFil = origen.getFila() - adyacente.getFila();
        int deltaCol = origen.getColumna() - adyacente.getColumna();
        // Calcula la coordenada opuesta usando la dirección opuesta
        int filaOpuesta = origen.getFila() + deltaFil;
        int columnaOpuesta = origen.getColumna() + deltaCol;
        // Verifica si la coordenada opuesta es válida dentro del tablero
        if (filaOpuesta >= 0 && filaOpuesta < CANT_FILAS &&
                columnaOpuesta >= 0 && columnaOpuesta < CANT_COLUMNAS) {
            return new Coordenada(filaOpuesta, columnaOpuesta);
        }
        return null;
    }

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
        ArrayList<Coordenada> coordenadas = casillaSelec.getCoordenadasCasillasAdyacentes();
        return coordenadas.contains(nuevaPosFichaSelec);
    }

    public boolean fichaTieneMovimiento(Coordenada posFichaSelec) {
        return hayMovimientosPosibles(tablero.getCasilla(posFichaSelec));
    }

    public boolean hayFichasParaEliminar(Jugador jugadorOponente) {
        if (jugadorOponente.getFichasColocadas() == Molino.CANTIDAD_FICHAS && jugadorOponente.getFichasEnTablero() == 3) {
            return true;
        }
        List<Casilla> casillasOcupadas = tablero.obtenerCasillasOcupadasPorJugador(jugadorOponente);
        for (Casilla casilla : casillasOcupadas) {
            if (!hayMolinoEnPosicion(casilla.getCoordenada(), jugadorOponente)) {
                return true;
            }
        }
        return false;
    }
}
