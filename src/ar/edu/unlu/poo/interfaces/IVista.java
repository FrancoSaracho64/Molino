package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.enumerados.EstadoJuego;

import java.rmi.RemoteException;

public interface IVista {
    void iniciarVista();
    void mostrarTablero() throws RemoteException;
    void mostrarMensajeErrorCasilla();
    void avisoDeMolino(String nombreJugador);
    void mostrarGanador(String nombreJugador);
    void juegoTerminado();
    void mostrarMensajeFichaSinMovimiento();
    void avisoCasillaNoAdyacente();
    void mostrarEmpate();
    void mostrarMensajeCasillaOcupada();
    void mostrarMensajeFichaFormaMolino();
    void avisoNoHayFichasParaEliminarDelOponente();
    void avisoJugadorSinMovimientos();
    void avisoJugadorSinFichas();
    void mostrarJugadorConectado() throws RemoteException;
    void mostrarTurnoDelOponente();
    void actualizarTablero() throws RemoteException;
    void mostrarMensajeAlGanador();
    void mostrarMensajeAlPerdedor();
    void actualizarVistaParaAccion(EstadoJuego estadoActual) throws RemoteException;
    void mostrarMensajeNoCorrespondeAlJugador();
    void mostrarMensajeNoCorrespondeAlOponente();
    void avisoJugadorHizoMolino();
    void mostrarMensajeCasillaLibre();
    void avisoEmpatePorMovimientosSinComerFichas();
    void informarOponenteHaAbandonado();
    void mostrarMensajeEsTuTurno();
}
