package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.modelos.Tablero;

public interface ControladorImpl {
    void comenzarJuego();
    String contenidoCasilla(int fila, int columna);
    Tablero obtenerTablero();
    void agregarObserver(TableroImpl vista);
}
