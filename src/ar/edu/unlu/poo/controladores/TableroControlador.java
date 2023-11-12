package ar.edu.unlu.poo.controladores;

import ar.edu.unlu.poo.enumerados.EstadoCasilla;
import ar.edu.unlu.poo.interfaces.ControladorImpl;
import ar.edu.unlu.poo.interfaces.Observer;
import ar.edu.unlu.poo.interfaces.TableroImpl;
import ar.edu.unlu.poo.modelos.*;
import ar.edu.unlu.poo.reglas.ReglasDelJuego;
import ar.edu.unlu.poo.vistas.utilidadesConsola.EntradaTeclado;

import java.util.ArrayList;

public class TableroControlador implements ControladorImpl {
    private static final String CASILLA_DISPONIBLE = "◉";
    private static final String CASILLA_INVALIDA = "";
    private ArrayList<Jugador> jugadores;
    private Tablero tablero;
    private TableroImpl vista;
    private ReglasDelJuego reglas;

    public TableroControlador(ArrayList<Jugador> jugadores, Tablero tablero){
        this.jugadores = jugadores;
        this.tablero = tablero;
        this.reglas = new ReglasDelJuego(tablero);
    }

    @Override
    public void comenzarJuego(){
        // Inicializaciones para comenzar el juego
        Jugador j1 = jugadores.get(0);
        Jugador j2 = jugadores.get(1);
        j1.resetearFichasColocadas();
        j2.resetearFichasColocadas();
        ArrayList<Ficha> fichasJ1 = generarFichas(j1);
        ArrayList<Ficha> fichasJ2 = generarFichas(j2);

        //Comienza el juego
        boolean juegoActivo = true; // estado de la partida
        boolean turno = true; // true: jugador1 --- false: jugador2
        vista.mostrarTablero(tablero);
        while (juegoActivo){
            if(turno){
                juegoActivo = turnoJugador(j1, fichasJ1);
                turno = false;
            } else {
                juegoActivo = turnoJugador(j2, fichasJ2);
                turno = true;
            }
        }
        // TODO: detectar ganador
        Jugador ganador = reglas.obtenerGanador(j1, j2);
        // TODO: modificar sus estadisticas
        /*
            ++ aumentar partidas ganadas
            ++ aumentar puntaje
         */

        // TODO: el ganador lo muestra la vista
        System.out.println("El juego ha terminado.");
        System.out.println("El ganador es: " + ganador.getNombre());

    }

    @Override
    public String contenidoCasilla(int fila, int columna) {
        String contenido;
        Ficha ficha = tablero.obtenerFicha(fila, columna);
        if (ficha == null) {
            if (tablero.obtenerCasilla(fila, columna).getEstadoCasilla() != EstadoCasilla.INVALIDA){
                contenido = CASILLA_DISPONIBLE;
            } else {
                contenido = CASILLA_INVALIDA;
            }
        } else {
            if (ficha.getJugador() == jugadores.get(0)) {
                contenido = "◯";
            } else {
                contenido = "x";
            }
        }
        return contenido;
    }

    @Override
    public Tablero obtenerTablero() {
        return tablero;
    }

    @Override
    public void agregarObserver(TableroImpl vista) {
        tablero.agregarObservador((Observer) vista);
    }

    private boolean turnoJugador(Jugador jugador, ArrayList<Ficha> fichas){
        boolean juego_activo = true;
        vista.mostrarTurno(jugador.getNombre());
        if (jugador.getFichasColocadas() < 9) {
            Coordenada c = colocarFicha(jugador, fichas);
            if (jugador.getFichasColocadas() >= 3) {
                if (reglas.hayMolinoEnPosicion(c.getFila(), c.getColumna(), jugador)) {
                    System.out.println("HAY MOLINO!");
                    EntradaTeclado.presionarEnterParaContinuar();
                }
            }
        } else {
            juego_activo = reglas.tienePiezasSuficientes(jugador) && reglas.jugadorTieneMovimientos(jugador);
            if (juego_activo){
                //TODO: hacer movimiento de una ficha

                //TODO: verificar si se hizo molino tras el movimiento realizado
            }
        }
        //Finaliza el turno
        return juego_activo;
    }

    private Coordenada colocarFicha(Jugador j, ArrayList<Ficha> fichasJ){
        boolean valida;
        Coordenada coord;
        do {
            coord = vista.pedirCasilla();
            valida = reglas.esCasillaValida(coord);
            if (!valida){
                vista.mostrarMensajeErrorCasilla();
            }
        } while (!valida);
        tablero.colocarFicha(coord.getFila(), coord.getColumna(), fichasJ.get(j.getFichasColocadas()));
        j.incFichasColocadas();
        return coord;
    }

    private ArrayList<Ficha> generarFichas(Jugador jugador){
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

    public void agregarVista(TableroImpl vista) {
        this.vista = vista;
    }
}
