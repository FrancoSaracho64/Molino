package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.modelos.Coordenada;
import ar.edu.unlu.poo.modelos.Jugador;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IControlador {
    void setVista(IVista vista);
    boolean laPartidaHaComenzado() throws RemoteException;
    boolean laPartidaSigueActiva() throws RemoteException;
    void guardarPartidaEstadoActual() throws RemoteException;
    void jugadorAbandona() throws RemoteException;
    void cerrarAplicacion() throws RemoteException;
    void casillaSeleccionadaDesdeLaVista(Coordenada coordenada) throws RemoteException;
    String contenidoCasilla(Coordenada coordenada) throws RemoteException;
    String nombreJugador() throws RemoteException;
    void agregarJugador(Jugador jugadorLocal) throws RemoteException;
    boolean existeElNombre(String nombre) throws RemoteException;
    boolean jugadorRegistradoEstaDisponible(int pos) throws RemoteException;
    ArrayList<Jugador> obtenerJugadoresRegistrados() throws RemoteException;
    boolean jugadorParaReanudarDisponible(int pos) throws RemoteException;
    ArrayList<Jugador> obtenerJugadoresParaReanudar() throws RemoteException;
    boolean esPartidaNueva() throws RemoteException;
    boolean hayJugadoresRegistrados() throws RemoteException;
}
