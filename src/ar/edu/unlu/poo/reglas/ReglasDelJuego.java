package ar.edu.unlu.poo.reglas;

import ar.edu.unlu.poo.modelos.Tablero;

public class ReglasDelJuego {

    public ReglasDelJuego() {
    }

    public boolean esCasillaValida(int fila, int columna, Tablero tablero) {
        // Verifica si la casilla está dentro de los límites del tablero.
        if (fila < 0 || fila >= tablero.getCountFilas() || columna < 0 || columna >= tablero.getCountColumnas()) {
            return false;
        }

        // Verifica si la casilla está ocupada por una ficha.
        if (tablero.obtenerFicha(fila, columna) != null) {
            return false;
        }

        // Agrega más reglas específicas del juego aquí, según las necesidades de tu implementación.

        // Si no se cumple ninguna de las condiciones anteriores, la casilla se considera válida.
        return true;
    }
}
