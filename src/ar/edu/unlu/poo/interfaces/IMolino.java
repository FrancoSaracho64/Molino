package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.modelos.Coordenada;
import ar.edu.unlu.poo.modelos.Ficha;
import ar.edu.unlu.poo.modelos.Jugador;
import ar.edu.unlu.poo.modelos.Tablero;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import ar.edu.unlu.rmimvc.observer.IObservadorRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IMolino extends IObservableRemoto {
    void conectarJugador(Jugador jugador) throws RemoteException;
    void desconectarJugador(Jugador jugador) throws RemoteException;
    ArrayList<Jugador> getJugadores() throws RemoteException;
    Jugador obtenerJ1() throws RemoteException;
    Jugador obtenerJ2() throws RemoteException;
    Tablero getTablero() throws RemoteException;
    String contenidoCasilla(Coordenada coordenada) throws RemoteException;
    void colocarFicha(Coordenada coordenada, Jugador jugadorActual, Jugador jugadorOponente, Ficha ficha) throws RemoteException;
    void quitarFicha(Coordenada coordenada) throws RemoteException;
    void moverFicha(Coordenada antCoord, Coordenada nueCoord) throws RemoteException;
    void enviarMovimiento(Jugador jugador, Coordenada coordenada) throws RemoteException;
    void cerrar(IObservadorRemoto controlador, Jugador jugador) throws RemoteException;
}
