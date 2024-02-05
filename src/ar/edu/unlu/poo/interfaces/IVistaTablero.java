package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.modelos.Jugador;
import ar.edu.unlu.poo.modelos.Tablero;

import java.rmi.RemoteException;

public interface IVistaTablero {
    void mostrarTablero(Tablero tablero) throws RemoteException;
    void mostrarTurno(String mensaje);
    void mostrarMensajeErrorCasilla();
    void avisoDeMolino(String nombreJugador);
    Object[] pedirCasilla();
    void fichaAEliminar();
    void iniciarJuego() throws RemoteException;
    void mostrarGanador(String nombreJugador);
    void juegoTerminado();
    void fichaSinMovimiento();
    void casillaNoAdyacente();
    void mensajeCasillaFichaAMover();
    void mensajePedirNuevaCasillaLibreAdyacente();
    void mostrarEmpate(String nombreJ1, String nombreJ2);
    void mostrarMensajeCasillaOcupada();
    void mensajeFichaFormaMolino();
    void mensajePedirNuevaCasillaLibre();
    void avisoNoHayFichasParaEliminarDelOponente();
    void jugadorSinMovimientos();
    void jugadorSinFichas();
    void mostrarJugadorConectado(Jugador jugador);
}
