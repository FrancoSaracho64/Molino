package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.modelos.Jugador;
import ar.edu.unlu.poo.modelos.Tablero;

import java.rmi.RemoteException;

public interface IControlador {
    void comenzarJuego() throws RemoteException;
    String contenidoCasilla(int fila, int columna);
    Tablero obtenerTablero();
    void agregarObserver(IVistaTablero vista);
    void enviarMovimiento();
    void actualizarVista();
    void setVista(IVistaTablero vista);
    void agregarJugador(Jugador jugador) throws RemoteException;
}
