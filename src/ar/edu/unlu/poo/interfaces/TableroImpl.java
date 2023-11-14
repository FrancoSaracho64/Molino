package ar.edu.unlu.poo.interfaces;

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
    void mensajeCasillaFichaAMover();
    void mensajePedirCasillaParaColocar();
    void mensajePedirNuevaCasillaLibre();
    void mostrarEmpate(String nombreJ1, String nombreJ2);
    void mostrarMensajeCasillaOcupada();
    void mensajeFichaFormaMolino();
}
