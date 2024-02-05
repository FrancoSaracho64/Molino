package ar.edu.unlu.poo.controladores;

import java.rmi.RemoteException;
import java.util.ArrayList;

import ar.edu.unlu.poo.modelos.Molino;
import ar.edu.unlu.poo.enumerados.EstadoCasilla;
import ar.edu.unlu.poo.enumerados.Evento;
import ar.edu.unlu.poo.interfaces.IControlador;
import ar.edu.unlu.poo.interfaces.IMolino;
import ar.edu.unlu.poo.interfaces.IVistaTablero;
import ar.edu.unlu.poo.modelos.Coordenada;
import ar.edu.unlu.poo.modelos.Ficha;
import ar.edu.unlu.poo.modelos.Jugador;
import ar.edu.unlu.poo.modelos.Tablero;
import ar.edu.unlu.poo.persistencia.Persistencia;
import ar.edu.unlu.poo.reglas.ReglasDelJuego;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

public class Controlador implements IControlador, IControladorRemoto {
    private IMolino modelo;
    private IVistaTablero vista;

    private ReglasDelJuego reglas;
    private Jugador jugador;

    public Controlador() {
    }

    @Override
    public void comenzarJuego() throws RemoteException {
        // Inicializaciones para comenzar el juego
        Jugador j1 = modelo.obtenerJ1();
        Jugador j2 = modelo.obtenerJ2();
        j1.resetearFichasColocadas();
        j1.resetearFichasEnTablero();
        j2.resetearFichasColocadas();
        j2.resetearFichasEnTablero();

        ArrayList<Ficha> fichasJ1 = Molino.generarFichas(j1);
        ArrayList<Ficha> fichasJ2 = Molino.generarFichas(j2);

        // Comienza el juego
        boolean juegoActivo = true;     // estado de la partida
        boolean turno = true;           // true: jugador1 --- false: jugador2
        vista.mostrarTablero(modelo.getTablero());
        while (juegoActivo) {
            if (turno) {
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
        if (ganador == null) {
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

            for (int i = 0; i < jugadores_historicos.size(); i++) {
                if (jugadores_historicos.get(i).getId() == j1.getId()) {
                    jugadores_historicos.set(i, j1);
                } else if (jugadores_historicos.get(i).getId() == j2.getId()) {
                    jugadores_historicos.set(i, j2);
                }
            }
            Persistencia.guardarJugadores(jugadores_historicos);
        }
    }

    private boolean turnoJugador(Jugador jugadorActual, Jugador oponente, ArrayList<Ficha> fichas) throws RemoteException {
        boolean juego_activo = true;
        vista.mostrarTurno(jugadorActual.getNombre());
        //  Si el jugador todavía no colocó 9 fichas, procede a colocar fichas.
        if (jugadorActual.getFichasColocadas() < Molino.CANTIDAD_FICHAS) {
            Coordenada coordenada = pedirCoordenadaLibre();
            modelo.colocarFicha(coordenada, jugadorActual, oponente, fichas.get(jugadorActual.getFichasColocadas()));
            if (jugadorActual.getFichasEnTablero() >= 3) {
                if (reglas.hayMolinoEnPosicion(coordenada, jugadorActual)) {
                    vista.avisoDeMolino(jugadorActual.getNombre());
                    if (reglas.hayFichasParaEliminar(oponente)) {
                        eliminarFichaOponente(oponente);
                    } else {
                        vista.avisoNoHayFichasParaEliminarDelOponente();
                    }
                }
            }
        } else {
            //  Ya se han colocado las 9 fichas. Empiezan a mover las fichas.
            juego_activo = jugadorActual.getFichasEnTablero() > 2 && reglas.jugadorTieneMovimientos(jugadorActual);
            if (juego_activo) {
                moverFicha(jugadorActual, oponente);
            }
        }
        //Finaliza el turno
        if (!juego_activo) {
            if (jugadorActual.getFichasEnTablero() <= 2) {
                vista.jugadorSinFichas();
            } else if (!reglas.jugadorTieneMovimientos(jugadorActual)) {
                vista.jugadorSinMovimientos();
            }
        }
        return juego_activo;
    }

    private Coordenada pedirCoordenadaLibre() {
        try {
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
                ocupada = modelo.getTablero().obtenerEstadoCasilla(coord.getFila(), coord.getColumna())
                        == EstadoCasilla.OCUPADA;
                if (ocupada) {
                    vista.mostrarMensajeCasillaOcupada();
                }
            } while (ocupada);
            return coord;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Coordenada solicitarCasillaVista() {
        Object[] coordenada = vista.pedirCasilla();
        int fila = (int) coordenada[0];
        char columna = (char) coordenada[1];
        switch (fila) {
            case 1 -> fila = 0;
            case 2 -> fila = 1;
            case 3 -> fila = 2;
            case 4 -> fila = 3;
            case 5 -> fila = 4;
            case 6 -> fila = 5;
            case 7 -> fila = 6;
        }
        int columnaResultado;
        switch (columna) {
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

    @Override
    public String contenidoCasilla(int fila, int columna) {
        return null;
    }

    @Override
    public Tablero obtenerTablero() {
        return null;
    }

    @Override
    public void agregarObserver(IVistaTablero vista) {

    }

    @Override
    public void enviarMovimiento() {

    }

    @Override
    public void actualizarVista() {
        try {
            vista.mostrarTablero(modelo.getTablero());
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setVista(IVistaTablero vista) {
        this.vista = vista;
    }

    @Override
    public void agregarJugador(Jugador jugador) throws RemoteException {
        this.jugador = jugador;
        this.modelo.conectarJugador(jugador.getNombre());
        this.vista.mostrarJugadorConectado(jugador);
    }

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T t) throws RemoteException {
        this.modelo = (IMolino) t;
    }

    @Override
    public void actualizar(IObservableRemoto iObservableRemoto, Object o) throws RemoteException {
        //actualizarVista();
        if (o instanceof Evento) {
            switch ((Evento) o) {
                case INSERCION_DE_FICHA -> {
                    //this.vista.insertarFicha();
                    this.modelo.getTablero();
                    break;
                }
                case MOVIMIENTO_DE_FICHA -> {
                    //this.vista.movimientoDeFicha
                    break;
                }
                case ELIMINACION_DE_FICHA -> {
                    break;
                }
                default -> {
                    break;
                }
            }
        }
    }

    private Coordenada pedirCoordenadaLibreAdyacente() {
        try {
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
                ocupada = modelo.getTablero().obtenerEstadoCasilla(coord.getFila(), coord.getColumna())
                        == EstadoCasilla.OCUPADA;
                if (ocupada) {
                    vista.mostrarMensajeCasillaOcupada();
                }
            } while (ocupada);
            return coord;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Coordenada pedirCoordenadaOcupadaPorJugador(Jugador jugador) {
        try {
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
                    ocupada = modelo.getTablero().obtenerEstadoCasilla(coord.getFila(), coord.getColumna())
                            == EstadoCasilla.OCUPADA;
                    if (!ocupada) {
                        vista.mostrarMensajeErrorCasilla();
                    }
                } while (!ocupada);
                esDeJugador = modelo.getTablero().obtenerFicha(coord.getFila(), coord.getColumna()).getJugador() == jugador;
                if (!esDeJugador) {
                    vista.mostrarMensajeErrorCasilla();
                }
            } while (!esDeJugador);
            return coord;
        } catch (RemoteException e){
            e.printStackTrace();
            return null;
        }
    }

    private void moverFicha(Jugador jugadorActual, Jugador jugadorOponente) {
        try {
            boolean sonAdyacentes;
            boolean tieneMovimiento;
            Coordenada posFichaSelec;
            Coordenada nuevaPosFichaSelec;
            if (jugadorActual.getFichasEnTablero() == 3) {
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
            modelo.moverFicha(posFichaSelec, nuevaPosFichaSelec);
            if (reglas.hayMolinoEnPosicion(nuevaPosFichaSelec, jugadorActual)) {
                vista.avisoDeMolino(jugadorActual.getNombre());
                if (reglas.hayFichasParaEliminar(jugadorOponente)) {
                    eliminarFichaOponente(jugadorOponente);
                } else {
                    vista.avisoNoHayFichasParaEliminarDelOponente();
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void eliminarFichaOponente(Jugador jugadorOponente) {
        try {
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
                    ficha = modelo.getTablero().obtenerFicha(coord.getFila(), coord.getColumna());
                    ocupada = modelo.getTablero().obtenerEstadoCasilla(coord.getFila(), coord.getColumna()) ==
                            EstadoCasilla.OCUPADA &&
                            ficha.getJugador() == jugadorOponente;
                    if (!ocupada) {
                        vista.mostrarMensajeErrorCasilla();
                    }
                } while (!ocupada);
                formaMolino = reglas.hayMolinoEnPosicion(coord, jugadorOponente);
                if (formaMolino) {
                    vista.mensajeFichaFormaMolino();
                }
            } while (formaMolino);
            modelo.quitarFicha(coord);
            jugadorOponente.decFichasEnTablero();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void closeApp() throws RemoteException {
        try {
            this.modelo.cerrar(this, this.jugador);
            System.exit(0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
