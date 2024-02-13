package ar.edu.unlu.poo.modelos;

import ar.edu.unlu.poo.enumerados.Accion;
import ar.edu.unlu.poo.enumerados.EstadoCasilla;
import ar.edu.unlu.poo.enumerados.EventosTablero;
import ar.edu.unlu.poo.interfaces.IMolino;
import ar.edu.unlu.poo.persistencia.Persistencia;
import ar.edu.unlu.poo.reglas.ReglasDelJuego;
import ar.edu.unlu.rmimvc.observer.IObservadorRemoto;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Molino extends ObservableRemoto implements IMolino/*, IObservable*/ {
    public static final int CANTIDAD_FICHAS = 9;
    private static final String CASILLA_DISPONIBLE = "#";
    private static final String CASILLA_INVALIDA = "";
    private static final String JUGADOR_1 = "X";
    private static final String JUGADOR_2 = "O";
    private Jugador jugadorActual;
    private Jugador jugador1;
    private Jugador jugador2;
    private ArrayList<Jugador> jugadores;
    private Tablero tablero;
    private ReglasDelJuego reglas;

    public Molino() {
        this.jugadores = new ArrayList<>();
        this.tablero = new Tablero();
        this.reglas = new ReglasDelJuego(tablero);
    }

    @Override
    public void comenzarJuego() throws RemoteException {
        jugador1 = obtenerJ1();
        jugador2 = obtenerJ2();
        prepararFichas();
        notificarObservadores(EventosTablero.INICIO_PARTIDA);
        //partidaMolino();
    }

    @Override
    public Jugador getOponente(Jugador jugadorLocal) {
        if (jugadorLocal.equals(jugador1)) {
            return jugador2;
        } else {
            return jugador1;
        }
    }

    private void partidaMolino() throws RemoteException {
        boolean juegoActivo = true;     // estado de la partida
        boolean turno = true;           // true: jugador1 --- false: jugador2
        while (juegoActivo) {
            if (turno) {
                turno = false;
                alternarTurno();
            } else {
                turno = true;
                alternarTurno();
            }
        }
        finPartida();
    }

    private void prepararFichas(){
        jugador1.resetearFichasColocadas();
        jugador1.resetearFichasEnTablero();
        jugador1.setFichas(Molino.generarFichas(jugador1));
        jugador2.resetearFichasColocadas();
        jugador2.resetearFichasEnTablero();
        jugador2.setFichas(Molino.generarFichas(jugador2));
    }

    private boolean turnoJugador(Jugador jugadorActual, Jugador oponente) throws RemoteException {
        boolean juego_activo = true;
        //Finaliza el turno
        alternarTurno();

        return juego_activo;
    }

    public void finPartida() {
        // Informar que el juego ha terminado.
        //todo: vista.juegoTerminado();
        Jugador ganador = reglas.obtenerGanador(jugador1, jugador2);
        if (ganador == null) {
            //todo: vista.mostrarEmpate(j1.getNombre(), j2.getNombre());
            jugador1.empataPartida();
            jugador2.empataPartida();
        } else {
            ganador.ganaPartida();
            if (jugador1 == ganador)
                jugador2.pierdePartida();
            else
                jugador1.pierdePartida();
            // Mostramos el ganador.
            //todo: vista.mostrarGanador(ganador.getNombre());
            ArrayList<Jugador> jugadores_historicos = Persistencia.cargarJugadoresHistorico();

            for (int i = 0; i < jugadores_historicos.size(); i++) {
                if (jugadores_historicos.get(i).getId() == jugador1.getId()) {
                    jugadores_historicos.set(i, jugador1);
                } else if (jugadores_historicos.get(i).getId() == jugador2.getId()) {
                    jugadores_historicos.set(i, jugador2);
                }
            }
            Persistencia.guardarJugadores(jugadores_historicos);
        }
    }

    @Override
    public String contenidoCasilla(Coordenada coordenada) {
        String contenido;
        Ficha ficha = tablero.obtenerFicha(coordenada);
        if (ficha == null) {
            if (tablero.obtenerEstadoCasilla(coordenada) != EstadoCasilla.INVALIDA) {
                contenido = CASILLA_DISPONIBLE;
            } else {
                contenido = CASILLA_INVALIDA;
            }
        } else {
            if (ficha.getJugador().equals(jugador1)) {
                contenido = JUGADOR_1;
            } else {
                contenido = JUGADOR_2;
            }
        }
        return contenido;
    }

    @Override
    public void colocarFicha(Coordenada coordenada, Jugador jugador) throws RemoteException {
        if (jugador.equals(jugadorActual)) {
            tablero.colocarFicha(coordenada, jugadorActual.getFichaParaColocar());
            jugadorActual.incFichasEnTablero();
            jugadorActual.incFichasColocadas();
            notificarObservadores(EventosTablero.CAMBIO_EN_EL_TABLERO); // Mostramos el cambio de la nueva ficha ingresada
        } else {
            throw new RemoteException("No es tu turno.");
        }
    }

    @Override
    public void quitarFicha(Coordenada coordenada) throws RemoteException {
        tablero.quitarFicha(coordenada);
        notificarObservadores(EventosTablero.CAMBIO_EN_EL_TABLERO);
    }

    @Override
    public void moverFicha(Coordenada antCoord, Coordenada nueCoord) throws RemoteException {
        tablero.moverFicha(antCoord, nueCoord);
        if (reglas.hayMolinoEnPosicion(nueCoord, jugadorActual)) {
            //todo: vista.avisoDeMolino(jugadorActual.getNombre());

            /*
            todo
                if (reglas.hayFichasParaEliminar(jugadorOponente)) {
                    eliminarFichaOponente(jugadorOponente);
                } else {
                    vista.avisoNoHayFichasParaEliminarDelOponente();
                }*/
        }
    }

    private static ArrayList<Ficha> generarFichas(Jugador jugador) {
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
        //TODO: El primer jugador que se conecta comienza.
        if (jugadores.isEmpty()) {
            jugadorActual = jugador;
        }
        jugadores.add(jugador);
        notificarObservadores(EventosTablero.JUGADOR_CONECTADO);
        if (jugadores.size() == 2) {
            comenzarJuego();
        }
    }

    public Tablero getTablero() {
        return tablero;
    }

    @Override
    public void cerrar(IObservadorRemoto controlador, Jugador jugador) throws RemoteException {

    }

    @Override
    public Jugador obtenerJugadorActual() throws RemoteException {
        return jugadorActual;
    }

    @Override
    public Jugador obtenerJugadorOponente() throws RemoteException {
        if (jugadorActual.equals(jugador1)) {
            return jugador2;
        } else {
            return jugador1;
        }
    }

    @Override
    public void establecerTurnoInicial(Jugador jugador) {
        jugadorActual = jugador;
    }

    @Override
    public boolean esTurnoDe(Jugador jugador) throws RemoteException {
        return obtenerJugadorActual().equals(jugador);
    }

    @Override
    public void alternarTurno() throws RemoteException {
        if (jugadorActual.equals(jugador1)) {
            jugadorActual = jugador2;
        } else {
            jugadorActual = jugador1;
        }
    }

    @Override
    public boolean esCasillaValida(Coordenada coordenada) throws RemoteException {
        return reglas.esCasillaValida(coordenada);
    }

    @Override
    public boolean hayFichasParaEliminar(Jugador oponente) throws RemoteException {
        return reglas.hayFichasParaEliminar(oponente);
    }

    @Override
    public boolean hayMolinoEnPosicion(Coordenada coord, Jugador jugadorOponente) {
        return reglas.hayMolinoEnPosicion(coord, jugadorOponente);
    }

    public Accion determinarAccionJugador(Jugador jugador) {
        // Aquí puedes añadir más lógica, por ejemplo, si el jugador puede mover una ficha o si debe quitar una ficha del oponente
        if (jugador.getFichasColocadas() < CANTIDAD_FICHAS) {
            return Accion.COLOCAR_FICHA;
        } else {
            if (jugador.getFichasEnTablero() > 3) {
                return Accion.MOVER_FICHA;
            } else {
                return Accion.MOVER_FICHA_SUPER;
            }
        }
    }

    @Override
    public boolean fichaTieneMovimientos(Coordenada coordenada) {
        return reglas.fichaTieneMovimiento(coordenada);
    }

    @Override
    public boolean sonCasillasAdyacentes(Coordenada cOrigen, Coordenada cDestino) {
        return reglas.sonCasillasAdyacentes(cOrigen, cDestino);
    }

    public Jugador obtenerJ1() throws RemoteException {
        return jugadores.getFirst();
    }

    public Jugador obtenerJ2() throws RemoteException {
        return jugadores.get(1);
    }
}