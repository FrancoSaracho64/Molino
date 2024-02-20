package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.enumerados.EstadoJuego;

import java.rmi.RemoteException;

public interface IVista {
    void iniciar();
    void mostrarTablero() throws RemoteException;
    void mostrarMensajeErrorCasilla();
    void avisoDeMolino(String nombreJugador);
    void mostrarGanador(String nombreJugador);
    void juegoTerminado();
    void mostrarMensajeFichaSinMovimiento();
    void casillaNoAdyacente();
    void mostrarEmpate();
    void mostrarMensajeCasillaOcupada();
    void mensajeFichaFormaMolino();
    void avisoNoHayFichasParaEliminarDelOponente();
    void jugadorSinMovimientos();
    void jugadorSinFichas();
    void mostrarJugadorConectado() throws RemoteException;
    void mostrarTurnoActual();
    void mostrarMensajeError(String mensaje);
    void actualizarTablero() throws RemoteException;
    void mensajeAlGanador();
    void mensajeAlPerdedor();
    void actualizarParaAccion(EstadoJuego estadoActual);
    void mostrarMensajeNoCorrespondeAlJugador();
    void mostrarMensajeNoCorrespondeAlOponente();
    void avisoJugadorHizoMolino();
    void mostrarMensajeCasillaLibre();
    void empatePorMovimientosSinComerFichas();
    void informarOponenteHaAbandonado();
}
