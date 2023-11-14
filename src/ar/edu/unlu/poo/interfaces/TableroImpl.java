package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.modelos.Coordenada;
import ar.edu.unlu.poo.modelos.Tablero;

public interface TableroImpl {
    void mostrarTablero(Tablero tablero);
    void mostrarTurno(String mensaje);
    void mostrarMensajeErrorCasilla();
    void avisoDeMolino(String nombreJugador);
    Object[] pedirCasilla();
    void fichaAEliminar();
    void iniciarJuego();
    void mostrarGanador(String nombreJugador);
    void juegoTerminado();
    void fichaSinMovimiento();
    void casillaNoAdyacente();
}
