package ar.edu.unlu.poo.controladores;

import ar.edu.unlu.poo.enumerados.EstadoCasilla;
import ar.edu.unlu.poo.interfaces.ControladorImpl;
import ar.edu.unlu.poo.interfaces.Observer;
import ar.edu.unlu.poo.interfaces.TableroImpl;
import ar.edu.unlu.poo.modelos.*;
import ar.edu.unlu.poo.reglas.ReglasDelJuego;

import java.util.ArrayList;

public class TableroControlador implements ControladorImpl {
    private static final String CASILLA_DISPONIBLE = "■";
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
        j1.resetearFichasEnTablero();
        j2.resetearFichasColocadas();
        j2.resetearFichasEnTablero();

        ArrayList<Ficha> fichasJ1 = generarFichas(j1);
        ArrayList<Ficha> fichasJ2 = generarFichas(j2);

        //Comienza el juego
        boolean juegoActivo = true; // estado de la partida
        boolean turno = true; // true: jugador1 --- false: jugador2
        vista.mostrarTablero(tablero);
        while (juegoActivo){
            if(turno){
                juegoActivo = turnoJugador(j1, j2, fichasJ1);
                turno = false;
            } else {
                juegoActivo = turnoJugador(j2, j1, fichasJ2);
                turno = true;
            }
        }

        // Informar que el juego ha terminado.
        vista.juegoTerminado();

        Jugador ganador = reglas.obtenerGanador(j1, j2);
        if (ganador == null){
            j1.empataPartida();
            j2.empataPartida();
        } else {
            ganador.ganaPartida();
            if (j1 == ganador)
                j2.pierdePartida();
            else
                j1.pierdePartida();
            // Mostramos el ganador.
            vista.mostrarGanador(ganador.getNombre());
        }
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
                contenido = "o";
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

    private boolean turnoJugador(Jugador jugadorActual, Jugador oponente, ArrayList<Ficha> fichas){
        boolean juego_activo = true;
        vista.mostrarTurno(jugadorActual.getNombre());
        if (jugadorActual.getFichasColocadas() < 3) { // TODO: no olvidar poner en 9
            Coordenada c = colocarFicha(jugadorActual, fichas.get(jugadorActual.getFichasColocadas()));
            if (jugadorActual.getFichasColocadas() >= 3) {
                if (reglas.hayMolinoEnPosicion(c.getFila(), c.getColumna(), jugadorActual)) {
                    vista.avisoDeMolino(jugadorActual.getNombre());
                    eliminarFichaOponente(oponente);
                }
            }
        } else {
            juego_activo = jugadorActual.getFichasEnTablero() > 2 && reglas.jugadorTieneMovimientos(jugadorActual);
            if (juego_activo){
                boolean sonAdyacentes;
                boolean tieneMovimiento;
                Coordenada posFichaSelec;
                Coordenada nuevaPosFichaSelec;
                do {
                    do {
                        posFichaSelec = pedir_coordenada_ocupada_por_jugador(jugadorActual);
                        tieneMovimiento = reglas.fichaTieneMovimiento(posFichaSelec);
                        if (!tieneMovimiento){
                            vista.fichaSinMovimiento();
                        }
                    } while (!tieneMovimiento);
                    nuevaPosFichaSelec = pedir_coordenada_libre();
                    sonAdyacentes = reglas.sonCasillasAdyacentes(posFichaSelec, nuevaPosFichaSelec);
                    if (!sonAdyacentes){
                        vista.casillaNoAdyacente();
                    }
                } while (!sonAdyacentes);
                tablero.moverFicha(posFichaSelec, nuevaPosFichaSelec);
                if (reglas.hayMolinoEnPosicion(nuevaPosFichaSelec.getFila(), nuevaPosFichaSelec.getColumna(), jugadorActual)) {
                    vista.avisoDeMolino(jugadorActual.getNombre());
                    eliminarFichaOponente(oponente);
                }
            }
        }
        //Finaliza el turno
        return juego_activo;
    }

    private Coordenada colocarFicha(Jugador j, Ficha ficha){
        Coordenada coord = pedir_coordenada_libre();
        tablero.colocarFicha(coord.getFila(), coord.getColumna(), ficha);
        j.incFichasEnTablero();
        j.incFichasColocadas();
        return coord;
    }

    private Coordenada pedir_coordenada_libre(){
        boolean valida;
        boolean ocupada;
        Coordenada coord;
        do {
            do {
                coord = solicitarCasillaVista();
                valida = reglas.esCasillaValida(coord);
                if (!valida) {
                    vista.mostrarMensajeErrorCasilla();
                }
            } while (!valida);
            ocupada = tablero.obtenerCasilla(coord.getFila(), coord.getColumna()).getEstadoCasilla()
                    == EstadoCasilla.OCUPADA;
            if (ocupada){
                vista.mostrarMensajeErrorCasilla();
            }
        } while (ocupada);
        return coord;
    }

    private Coordenada pedir_coordenada_ocupada_por_jugador(Jugador jugador){
        boolean valida;
        boolean ocupada;
        boolean esDeJugador;
        Coordenada coord;
        do { // verifico que la ficha corresponda al jugador pasado por parámetro.
            do { // verifico que la casilla esté ocupada.
                do { // verifico que sea una casilla válida.
                    coord = solicitarCasillaVista();
                    valida = reglas.esCasillaValida(coord);
                    if (!valida) {
                        vista.mostrarMensajeErrorCasilla();
                    }
                } while (!valida);
                ocupada = tablero.obtenerCasilla(coord.getFila(), coord.getColumna()).getEstadoCasilla()
                        == EstadoCasilla.OCUPADA;
                if (!ocupada) {
                    vista.mostrarMensajeErrorCasilla();
                }
            } while (!ocupada);
            esDeJugador = tablero.obtenerFicha(coord.getFila(), coord.getColumna()).getJugador() == jugador;
            if (!esDeJugador){
                vista.mostrarMensajeErrorCasilla();
            }
        } while (!esDeJugador);
        return coord;
    }

    private void eliminarFichaOponente(Jugador jugadorOponente){
        boolean valida;
        boolean ocupada;
        Coordenada coord;
        do {
            do {
                vista.fichaAEliminar();
                coord = solicitarCasillaVista();
                valida = reglas.esCasillaValida(coord);
                if (!valida) {
                    vista.mostrarMensajeErrorCasilla();
                }
            } while (!valida);
            ocupada = tablero.obtenerCasilla(coord.getFila(), coord.getColumna()).getEstadoCasilla() ==
                    EstadoCasilla.OCUPADA &&
                    tablero.obtenerFicha(coord.getFila(), coord.getColumna()).getJugador() == jugadorOponente;
            if (!ocupada){
                vista.mostrarMensajeErrorCasilla();
            }
        } while (!ocupada);
        tablero.quitarFicha(true, coord.getFila(), coord.getColumna());
        jugadorOponente.decFichasEnTablero();
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

    public Coordenada solicitarCasillaVista(){
        Object[] coordenada = vista.pedirCasilla();
        int fila = (int) coordenada[0];
        char columna = (char) coordenada[1];
        switch (fila){
            case 1 -> fila = 0;
            case 2 -> fila = 2;
            case 3 -> fila = 4;
            case 4 -> fila = 6;
            case 5 -> fila = 8;
            case 6 -> fila = 10;
            case 7 -> fila = 12;
        }
        int columnaResultado;
        switch (columna){
            case 'A' -> columnaResultado = 0;
            case 'B' -> columnaResultado = 2;
            case 'C' -> columnaResultado = 4;
            case 'D' -> columnaResultado = 6;
            case 'E' -> columnaResultado = 8;
            case 'F' -> columnaResultado = 10;
            case 'G' -> columnaResultado = 12;
            default -> columnaResultado = -1;
        }
        return new Coordenada(fila, columnaResultado);
    }
}
