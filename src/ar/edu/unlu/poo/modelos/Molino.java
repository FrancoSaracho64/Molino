package ar.edu.unlu.poo.modelos;

import ar.edu.unlu.poo.enumerados.EstadoCasilla;
import ar.edu.unlu.poo.interfaces.IMolino;
import ar.edu.unlu.poo.reglas.ReglasDelJuego;
import ar.edu.unlu.rmimvc.observer.IObservadorRemoto;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Molino extends ObservableRemoto implements IMolino {
    public static final int CANTIDAD_FICHAS = 9;
    private static final String CASILLA_DISPONIBLE = "#";
    private static final String CASILLA_INVALIDA = "";
    private static final String JUGADOR_1 = "X";
    private static final String JUGADOR_2 = "O";

    private ArrayList<Jugador> jugadores;
    private Tablero tablero;
    private ReglasDelJuego reglas;

    /*
    public static void main(String[] args) {
        /**
         * Ejecuto esta clase, la cual se encarga de levantar una vista en caso de que se quiera correr
         * de forma local.
         * Acá se le podría preguntar al usuario.

        //new MenuPrincipal();

    }*/

    public Molino() {
        this.jugadores = new ArrayList<>();
        this.tablero = new Tablero();
        this.reglas = new ReglasDelJuego(tablero);
    }

    public String contenidoCasilla(int fila, int columna) {
        String contenido;
        Ficha ficha = tablero.obtenerFicha(fila, columna);
        if (ficha == null) {
            if (tablero.obtenerEstadoCasilla(fila, columna) != EstadoCasilla.INVALIDA) {
                contenido = CASILLA_DISPONIBLE;
            } else {
                contenido = CASILLA_INVALIDA;
            }
        } else {
            if (ficha.getJugador() == jugadores.get(0)) {
                contenido = JUGADOR_1;
            } else {
                contenido = JUGADOR_2;
            }
        }
        return contenido;
    }

    @Override
    public void colocarFicha(Coordenada coordenada, Jugador jugadorActual, Jugador jugadorOponente, Ficha ficha) {
        tablero.colocarFicha(coordenada, ficha);
        jugadorActual.incFichasEnTablero();
        jugadorActual.incFichasColocadas();
    }

    @Override
    public void quitarFicha(Coordenada coordenada) {
        tablero.quitarFicha(true, coordenada.getFila(), coordenada.getColumna());
    }

    @Override
    public void moverFicha(Coordenada antCoord, Coordenada nueCoord) {
        tablero.moverFicha(antCoord, nueCoord);
    }

    public static ArrayList<Ficha> generarFichas(Jugador jugador) {
        ArrayList<Ficha> fichas = new ArrayList<>();
        Ficha f1 = new Ficha(jugador);
        Ficha f2 = new Ficha(jugador);
        Ficha f3 = new Ficha(jugador);
        Ficha f4 = new Ficha(jugador);
        Ficha f5 = new Ficha(jugador);
        Ficha f6 = new Ficha(jugador);
        Ficha f7 = new Ficha(jugador);
        Ficha f8 = new Ficha(jugador);
        Ficha f9 = new Ficha(jugador);
        fichas.add(f1);
        fichas.add(f2);
        fichas.add(f3);
        fichas.add(f4);
        fichas.add(f5);
        fichas.add(f6);
        fichas.add(f7);
        fichas.add(f8);
        fichas.add(f9);
        return fichas;
    }

    @Override
    public void conectarJugador(Jugador jugador) throws RemoteException {
        jugadores.add(jugador);
    }

    @Override
    public void desconectarJugador(Jugador jugador) throws RemoteException {
        jugadores.remove(jugador);
    }

    @Override
    public ArrayList<Jugador> getJugadores() throws RemoteException {
        return jugadores;
    }

    @Override
    public Jugador obtenerJ1() throws RemoteException {
        return jugadores.get(0);
    }

    @Override
    public Jugador obtenerJ2() throws RemoteException {
        return jugadores.get(1);
    }

    public Tablero getTablero() {
        return tablero;
    }

    @Override
    public void enviarMovimiento(Jugador jugador, Coordenada coordenada) throws RemoteException {

    }

    @Override
    public void cerrar(IObservadorRemoto controlador, Jugador jugador) throws RemoteException {

    }
}