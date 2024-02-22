package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.enumerados.Accion;
import ar.edu.unlu.poo.enumerados.EstadoJuego;
import ar.edu.unlu.poo.enumerados.MotivoFinPartida;
import ar.edu.unlu.poo.modelos.Coordenada;
import ar.edu.unlu.poo.modelos.Jugador;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IMolino extends IObservableRemoto {
    void conectarJugador(Jugador jugador) throws RemoteException;
    String getContenidoCasilla(Coordenada coordenada) throws RemoteException;
    void colocarFicha(Coordenada coordenada, Jugador jugadorActual) throws RemoteException;
    void quitarFicha(Coordenada coordenada, Jugador oponente) throws RemoteException;
    void moverFicha(Coordenada antCoord, Coordenada nueCoord) throws RemoteException;
    void jugadorHaAbandonado(Jugador jugador) throws RemoteException;
    boolean esTurnoDe(Jugador jugador) throws RemoteException;
    boolean esCasillaValida(Coordenada coordenada) throws RemoteException;
    boolean hayFichasParaEliminar(Jugador oponente) throws RemoteException;
    Accion determinarAccionJugador(Jugador jugador) throws RemoteException;
    boolean fichaTieneMovimientos(Coordenada coordenada) throws RemoteException;
    boolean sonCasillasAdyacentes(Coordenada cOrigen, Coordenada cDestino) throws RemoteException;
    void comenzarJuego() throws RemoteException;
    Jugador getOponente(Jugador jugadorLocal) throws RemoteException;
    String getNombreMolino() throws RemoteException;
    boolean verificarMolinoTrasMovimiento(Coordenada coordenada, Jugador jugador) throws RemoteException;
    void finalizarTurno() throws RemoteException;
    MotivoFinPartida obtenerMotivoFinPartida() throws RemoteException;
    Jugador obtenerGanador() throws RemoteException;
    boolean hayJugadoresRegistrados() throws RemoteException;
    ArrayList<Jugador> obtenerJugadoresRegistrados() throws RemoteException;
    boolean jugadorEstaDisponible(int pos) throws RemoteException;
    boolean existeNombreJugador(String nombre) throws RemoteException;
    boolean casillaOcupadaPorJugador(Coordenada coordenada, Jugador jugador) throws RemoteException;
    boolean hayPartidaActiva() throws RemoteException;
    void removerJugador(Jugador jugadorLocal) throws RemoteException;
    boolean esCasillaLibre(Coordenada coordenada) throws RemoteException;
    void guardarPartida() throws RemoteException;
    boolean esPartidaNueva() throws RemoteException;
    ArrayList<Jugador> obtenerJugadoresParaReanudar() throws RemoteException;
    boolean jugadorParaReanudarDisponible(int pos) throws RemoteException;
    boolean jugadorTieneFichasPendientes(Jugador jugadorLocal) throws RemoteException;
    boolean jugadorEstaEnVuelo(Jugador jugadorLocal) throws RemoteException;
    boolean juegoSigueActivo() throws RemoteException;
    boolean fichaSePuedeEliminar(Coordenada coordenada, Jugador oponente) throws RemoteException;
    boolean ultimoMovimientoFueMolino() throws RemoteException;
}
