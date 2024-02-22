package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.modelos.Coordenada;
import ar.edu.unlu.poo.modelos.Jugador;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IControlador {
    void colocarVista(IVista vista);
    boolean partidaHaComenzado() throws RemoteException;
    boolean partidaSigueActiva() throws RemoteException;
    void guardarPartida() throws RemoteException;
    void jugadorAbandona() throws RemoteException;
    void aplicacionCerrada() throws RemoteException;
    void casillaSeleccionadaDesdeLaVista(Coordenada coordenada) throws RemoteException;
    String obtenerContenidoCasilla(Coordenada coordenada) throws RemoteException;
    String obtenerNombreJugador() throws RemoteException;
    void agregarJugador(Jugador jugadorLocal) throws RemoteException;
    boolean esNombreYaRegistrado(String nombre) throws RemoteException;
    boolean jugadorRegistradoEstaDisponible(int pos) throws RemoteException;
    ArrayList<Jugador> obtenerJugadoresRegistrados() throws RemoteException;
    boolean jugadorParaReanudarDisponible(int pos) throws RemoteException;
    ArrayList<Jugador> obtenerJugadoresParaReanudar() throws RemoteException;
    boolean esPartidaNueva() throws RemoteException;
    boolean hayJugadoresRegistrados() throws RemoteException;
}
