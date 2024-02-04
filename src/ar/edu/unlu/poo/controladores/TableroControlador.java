package ar.edu.unlu.poo.controladores;

import ar.edu.unlu.poo.enumerados.EstadoCasilla;
import ar.edu.unlu.poo.interfaces.ControladorImpl;
import ar.edu.unlu.poo.interfaces.Observer;
import ar.edu.unlu.poo.interfaces.VistaTableroI;
import ar.edu.unlu.poo.modelos.*;
import ar.edu.unlu.poo.persistencia.Persistencia;
import ar.edu.unlu.poo.reglas.ReglasDelJuego;

import java.util.ArrayList;

public class TableroControlador implements ControladorImpl {
    private static final int CANTIDAD_FICHAS = 9;
    private static final String CASILLA_DISPONIBLE = "#";
    private static final String CASILLA_INVALIDA = "";
    private static final String JUGADOR_1 = "X";
    private static final String JUGADOR_2 = "O";
    private ArrayList<Jugador> jugadores;
    private Tablero tablero;
    private VistaTableroI vista;
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

        // Comienza el juego
        boolean juegoActivo = true;     // estado de la partida
        boolean turno = true;           // true: jugador1 --- false: jugador2
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
            vista.mostrarEmpate(j1.getNombre(), j2.getNombre());
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
            ArrayList<Jugador> jugadores_historicos = Persistencia.cargarJugadoresHistorico();

            for (int i = 0; i < jugadores_historicos.size(); i++){
                if (jugadores_historicos.get(i).getId() == j1.getId()){
                    jugadores_historicos.set(i, j1);
                } else if (jugadores_historicos.get(i).getId() == j2.getId()){
                    jugadores_historicos.set(i, j2);
                }
            }
            Persistencia.guardarJugadores(jugadores_historicos);
        }
    }

    @Override
    public String contenidoCasilla(int fila, int columna) {
        String contenido;
        Ficha ficha = tablero.obtenerFicha(fila, columna);
        if (ficha == null) {
            if (tablero.obtenerEstadoCasilla(fila, columna) != EstadoCasilla.INVALIDA){
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
    public Tablero obtenerTablero() {
        return tablero;
    }

    @Override
    public void agregarObserver(VistaTableroI vista) {
        tablero.agregarObservador((Observer) vista);
    }

    private boolean turnoJugador(Jugador jugadorActual, Jugador oponente, ArrayList<Ficha> fichas){
        boolean juego_activo = true;
        vista.mostrarTurno(jugadorActual.getNombre());
        //  Si el jugador todavía no colocó 9 fichas, procede a colocar fichas.
        if (jugadorActual.getFichasColocadas() < CANTIDAD_FICHAS) {
            colocarFicha(jugadorActual, oponente, fichas.get(jugadorActual.getFichasColocadas()));
        } else {
            //  Ya se han colocado las 9 fichas. Empiezan a mover las fichas.
            juego_activo = jugadorActual.getFichasEnTablero() > 2 && reglas.jugadorTieneMovimientos(jugadorActual);
            if (juego_activo){
                moverFicha(jugadorActual, oponente);
            }
        }
        //Finaliza el turno
        if (!juego_activo){
            if (jugadorActual.getFichasEnTablero() <= 2){
                vista.jugadorSinFichas();
            }
            else if (!reglas.jugadorTieneMovimientos(jugadorActual)){
                vista.jugadorSinMovimientos();
            }
        }
        return juego_activo;
    }

    private void colocarFicha(Jugador jugadorActual, Jugador jugadorOponente, Ficha ficha){
        Coordenada coord = pedirCoordenadaLibre();
        tablero.colocarFicha(coord, ficha);
        jugadorActual.incFichasEnTablero();
        jugadorActual.incFichasColocadas();

        if (jugadorActual.getFichasEnTablero() >= 3) {
            if (reglas.hayMolinoEnPosicion(coord, jugadorActual)) {
                vista.avisoDeMolino(jugadorActual.getNombre());
                if (reglas.hayFichasParaEliminar(jugadorOponente)){
                    eliminarFichaOponente(jugadorOponente);
                } else {
                    vista.avisoNoHayFichasParaEliminarDelOponente();
                }
            }
        }
    }

    private void moverFicha(Jugador jugadorActual, Jugador jugadorOponente){
        boolean sonAdyacentes;
        boolean tieneMovimiento;
        Coordenada posFichaSelec;
        Coordenada nuevaPosFichaSelec;
        if (jugadorActual.getFichasEnTablero() == 3){
            //  Movimiento de pieza a cualquier posición libre
            posFichaSelec = pedirCoordenadaOcupadaPorJugador(jugadorActual);
            nuevaPosFichaSelec = pedirCoordenadaLibre();
        } else {
            //  Movimiento de pieza a una posición adyacente
            do {
                do {
                    posFichaSelec = pedirCoordenadaOcupadaPorJugador(jugadorActual);
                    tieneMovimiento = reglas.fichaTieneMovimiento(posFichaSelec);
                    if (!tieneMovimiento) {
                        vista.fichaSinMovimiento();
                    }
                } while (!tieneMovimiento);

                nuevaPosFichaSelec = pedirCoordenadaLibreAdyacente();
                sonAdyacentes = reglas.sonCasillasAdyacentes(posFichaSelec, nuevaPosFichaSelec);
                if (!sonAdyacentes) {
                    vista.casillaNoAdyacente();
                }
            } while (!sonAdyacentes);
        }
        tablero.moverFicha(posFichaSelec, nuevaPosFichaSelec);
        if (reglas.hayMolinoEnPosicion(nuevaPosFichaSelec, jugadorActual)) {
            vista.avisoDeMolino(jugadorActual.getNombre());
            if (reglas.hayFichasParaEliminar(jugadorOponente)){
                eliminarFichaOponente(jugadorOponente);
            } else {
                vista.avisoNoHayFichasParaEliminarDelOponente();
            }
        }
    }

    private Coordenada pedirCoordenadaLibre(){
        boolean valida;
        boolean ocupada;
        Coordenada coord;
        do {
            do {
                vista.mensajePedirNuevaCasillaLibre();
                coord = solicitarCasillaVista();
                valida = reglas.esCasillaValida(coord);
                if (!valida) {
                    vista.mostrarMensajeErrorCasilla();
                }
            } while (!valida);
            ocupada = tablero.obtenerEstadoCasilla(coord.getFila(), coord.getColumna())
                    == EstadoCasilla.OCUPADA;
            if (ocupada){
                vista.mostrarMensajeCasillaOcupada();
            }
        } while (ocupada);
        return coord;
    }

    private Coordenada pedirCoordenadaLibreAdyacente(){
        boolean valida;
        boolean ocupada;
        Coordenada coord;
        do {
            do {
                vista.mensajePedirNuevaCasillaLibreAdyacente();
                coord = solicitarCasillaVista();
                valida = reglas.esCasillaValida(coord);
                if (!valida) {
                    vista.mostrarMensajeErrorCasilla();
                }
            } while (!valida);
            ocupada = tablero.obtenerEstadoCasilla(coord.getFila(), coord.getColumna())
                    == EstadoCasilla.OCUPADA;
            if (ocupada){
                vista.mostrarMensajeCasillaOcupada();
            }
        } while (ocupada);
        return coord;
    }

    private Coordenada pedirCoordenadaOcupadaPorJugador(Jugador jugador){
        boolean valida;
        boolean ocupada;
        boolean esDeJugador;
        Coordenada coord;
        do { // verifico que la ficha corresponda al jugador pasado por parámetro.
            do { // verifico que la casilla esté ocupada.
                do { // verifico que sea una casilla válida.
                    vista.mensajeCasillaFichaAMover();
                    coord = solicitarCasillaVista();
                    valida = reglas.esCasillaValida(coord);
                    if (!valida) {
                        vista.mostrarMensajeErrorCasilla();
                    }
                } while (!valida);
                ocupada = tablero.obtenerEstadoCasilla(coord.getFila(), coord.getColumna())
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
        boolean formaMolino;
        Coordenada coord;
        Ficha ficha;
        do { //La ficha no tiene que estar en molino
            do { // La casilla está ocupada por el oponente
                do { // Casilla valida
                    vista.fichaAEliminar();
                    coord = solicitarCasillaVista();
                    valida = reglas.esCasillaValida(coord);
                    if (!valida) {
                        vista.mostrarMensajeErrorCasilla();
                    }
                } while (!valida);
                ficha = tablero.obtenerFicha(coord.getFila(), coord.getColumna());
                ocupada = tablero.obtenerEstadoCasilla(coord.getFila(), coord.getColumna()) ==
                        EstadoCasilla.OCUPADA &&
                        ficha.getJugador() == jugadorOponente;
                if (!ocupada) {
                    vista.mostrarMensajeErrorCasilla();
                }
            } while (!ocupada);
            formaMolino = reglas.hayMolinoEnPosicion(coord, jugadorOponente);
            if (formaMolino){
                vista.mensajeFichaFormaMolino();
            }
        } while (formaMolino);
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

    public void agregarVista(VistaTableroI vista) {
        this.vista = vista;
    }

    public Coordenada solicitarCasillaVista(){
        Object[] coordenada = vista.pedirCasilla();
        int fila = (int) coordenada[0];
        char columna = (char) coordenada[1];
        switch (fila){
            case 1 -> fila = 0;
            case 2 -> fila = 1;
            case 3 -> fila = 2;
            case 4 -> fila = 3;
            case 5 -> fila = 4;
            case 6 -> fila = 5;
            case 7 -> fila = 6;
        }
        int columnaResultado;
        switch (columna){
            case 'A' -> columnaResultado = 0;
            case 'B' -> columnaResultado = 1;
            case 'C' -> columnaResultado = 2;
            case 'D' -> columnaResultado = 3;
            case 'E' -> columnaResultado = 4;
            case 'F' -> columnaResultado = 5;
            case 'G' -> columnaResultado = 6;
            default -> columnaResultado = -1;
        }
        return new Coordenada(fila, columnaResultado);
    }
}
