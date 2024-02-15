package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.modelos.Jugador;

import java.rmi.RemoteException;

public interface IVista {
    void iniciar();
    void mostrarTablero() throws RemoteException;
    void mostrarTurno(String mensaje);
    void mostrarMensajeErrorCasilla();
    void avisoDeMolino(String nombreJugador);
    Object[] pedirCasilla();
    void fichaAEliminar();
    void mostrarGanador(String nombreJugador);
    void juegoTerminado();
    void fichaSinMovimiento();
    void casillaNoAdyacente();
    void mensajeCasillaFichaAMover();
    void mensajePedirNuevaCasillaLibreAdyacente();
    void mostrarEmpate();
    void mostrarMensajeCasillaOcupada();
    void mensajeFichaFormaMolino();
    void mensajePedirNuevaCasillaLibre();
    void avisoNoHayFichasParaEliminarDelOponente();
    void jugadorSinMovimientos();
    void jugadorSinFichas();
    void mostrarJugadorConectado();
    void mostrarTurnoActual(Jugador jugadorActual);
    void mostrarMensajeError(String mensaje);
    void actualizar() throws RemoteException;
}
