package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.modelos.Coordenada;
import ar.edu.unlu.poo.modelos.Tablero;

public interface TableroImpl {
    void mostrarTablero(Tablero tablero);
    void mostrarMensaje(String mensaje);
    void mostrarMensajeErrorCasilla();
    Coordenada pedirCasilla();
    void iniciarJuego();
}
