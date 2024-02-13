package ar.edu.unlu.poo.controladores;

import java.rmi.RemoteException;

import ar.edu.unlu.poo.enumerados.EstadoCasilla;
import ar.edu.unlu.poo.enumerados.EventosTablero;
import ar.edu.unlu.poo.interfaces.IMolino;
import ar.edu.unlu.poo.interfaces.IVista;
import ar.edu.unlu.poo.modelos.Coordenada;
import ar.edu.unlu.poo.modelos.Ficha;
import ar.edu.unlu.poo.modelos.Jugador;
import ar.edu.unlu.poo.modelos.Tablero;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

public class Controlador implements IControladorRemoto {
    private IMolino modelo;
    private IVista vista;
    private Jugador jugadorLocal;
    private Jugador oponente;

    public Controlador() {
    }

    public void iniciarPartida() throws RemoteException {
        // Comenzar el juego
        setOponente();
        //modelo.comenzarJuego();
        vista.mostrarTablero(modelo.getTablero());
    }

    public void realizarAccion() {
        try {
            if (modelo.esTurnoDe(jugadorLocal)) {
                switch (modelo.determinarAccionJugador(jugadorLocal)) {
                    case COLOCAR_FICHA -> {
                        Coordenada coordenada = pedirCoordenadaLibre();
                        modelo.colocarFicha(coordenada, jugadorLocal);
                        if (modelo.verificarMolinoTrasMovimiento(coordenada, jugadorLocal)) {
                            // Si se da la condición, eliminamos una ficha.
                            eliminarFichaOponente(oponente);
                        }
                        finalizarTurno();
                    }
                    case MOVER_FICHA -> {
                        Coordenada origen = pedirCoordenadaFichaAMover(jugadorLocal);
                        if (modelo.fichaTieneMovimientos(origen)) {
                            Coordenada destino = pedirCoordenadaLibreAdyacente(origen);
                            modelo.moverFicha(origen, destino);
                            if (modelo.verificarMolinoTrasMovimiento(destino, jugadorLocal)) {
                                eliminarFichaOponente(oponente);
                            }
                        } else {
                            vista.fichaSinMovimiento();
                        }
                        finalizarTurno();
                    }
                    case MOVER_FICHA_SUPER -> {
                        Coordenada origen = pedirCoordenadaFichaAMover(jugadorLocal);
                        Coordenada destino = pedirCoordenadaLibre();
                        modelo.moverFicha(origen, destino);
                        if (modelo.verificarMolinoTrasMovimiento(destino, jugadorLocal)) {
                            eliminarFichaOponente(oponente);
                        }
                        finalizarTurno();
                    }
                }
            } else {
                vista.mostrarTurno("Es turno de: " + oponente.getNombre());
            }
        } catch (RemoteException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    private void finalizarTurno() {
        try {
            modelo.finalizarTurno();
            Jugador jugadorActual = modelo.obtenerJugadorActual();
            vista.mostrarTurnoActual(jugadorActual);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public String contenidoCasilla(Coordenada coordenada) throws RemoteException {
        return modelo.contenidoCasilla(coordenada);
    }

    public Tablero obtenerTablero() throws RemoteException {
        return modelo.getTablero();
    }

    public void setVista(IVista vista) {
        this.vista = vista;
    }

    public void agregarJugador(Jugador jugador) throws RemoteException {
        jugadorLocal = jugador;
        modelo.conectarJugador(jugador);
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

    private void eliminarFichaOponente(Jugador jugadorOponente) throws RemoteException {
        if (modelo.esTurnoDe(jugadorLocal)) {
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
                            valida = modelo.esCasillaValida(coord);
                            if (!valida) {
                                vista.mostrarMensajeErrorCasilla();
                            }
                        } while (!valida);
                        ficha = modelo.getTablero().obtenerFicha(coord);
                        ocupada = modelo.getTablero().obtenerEstadoCasilla(coord) ==
                                EstadoCasilla.OCUPADA &&
                                ficha.getJugador().equals(jugadorOponente);
                        if (!ocupada) {
                            vista.mostrarMensajeErrorCasilla();
                        }
                    } while (!ocupada);
                    formaMolino = modelo.hayMolinoEnPosicion(coord, jugadorOponente);
                    if (formaMolino) {
                        vista.mensajeFichaFormaMolino();
                    }
                } while (formaMolino);
                // Eliminamos la ficha del tablero
                modelo.quitarFicha(coord, jugadorOponente);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
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
                    valida = modelo.esCasillaValida(coord);
                    if (!valida) {
                        vista.mostrarMensajeErrorCasilla();
                    }
                } while (!valida);
                ocupada = modelo.getTablero().obtenerEstadoCasilla(coord)
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

    private Coordenada pedirCoordenadaLibreAdyacente(Coordenada origen) {
        try {
            boolean valida;
            boolean ocupada;
            boolean esAdyacente;
            Coordenada destino;
            do { // es adyacente
                do { // casilla libre
                    do { // casilla valida
                        vista.mensajePedirNuevaCasillaLibreAdyacente();
                        destino = solicitarCasillaVista();
                        valida = modelo.esCasillaValida(destino);
                        if (!valida) {
                            vista.mostrarMensajeErrorCasilla();
                        }
                    } while (!valida);
                    ocupada = modelo.getTablero().obtenerEstadoCasilla(destino)
                            == EstadoCasilla.OCUPADA;
                    if (ocupada) {
                        vista.mostrarMensajeCasillaOcupada();
                    }
                } while (ocupada);
                esAdyacente = modelo.sonCasillasAdyacentes(origen, destino);
                if (!esAdyacente) {
                    vista.casillaNoAdyacente();
                }
            } while (!esAdyacente);
            return destino;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Coordenada pedirCoordenadaFichaAMover(Jugador jugador) {
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
                        valida = modelo.esCasillaValida(coord);
                        if (!valida) {
                            vista.mostrarMensajeErrorCasilla();
                        }
                    } while (!valida);
                    ocupada = modelo.getTablero().obtenerEstadoCasilla(coord)
                            == EstadoCasilla.OCUPADA;
                    if (!ocupada) {
                        vista.mostrarMensajeErrorCasilla();
                    }
                } while (!ocupada);
                esDeJugador = modelo.getTablero().obtenerFicha(coord).getJugador().equals(jugador);
                if (!esDeJugador) {
                    vista.mostrarMensajeErrorCasilla();
                }
            } while (!esDeJugador);
            return coord;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void finDePartida() throws RemoteException {
        vista.juegoTerminado();
        switch (modelo.obtenerMotivoFinPartida()) {
            case JUGADOR_SIN_MOVIMIENTOS -> {
                vista.jugadorSinMovimientos();
            }
            case JUGADOR_SIN_FICHAS -> {
                vista.jugadorSinFichas();
            }
        }
    }

    public void closeApp() throws RemoteException {
        try {
            this.modelo.cerrar(this, this.jugadorLocal);
            System.exit(0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void setOponente() throws RemoteException {
        this.oponente = modelo.getOponente(jugadorLocal);
    }

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T t) throws RemoteException {
        this.modelo = (IMolino) t;
    }

    @Override
    public void actualizar(IObservableRemoto iObservableRemoto, Object o) throws RemoteException {
        if (o instanceof EventosTablero) {
            switch ((EventosTablero) o) {
                case JUGADOR_CONECTADO -> vista.mostrarJugadorConectado();
                case INICIO_PARTIDA -> iniciarPartida();
                case MOLINO -> vista.avisoDeMolino(modelo.getNombreMolino());
                case CAMBIO_EN_EL_TABLERO -> vista.actualizar();
                case TURNO_JUGADOR -> {
                }
                case JUGADOR_SIN_FICHAS -> vista.jugadorSinFichas();
                case JUGADOR_SIN_MOVIMIENTOS -> vista.jugadorSinMovimientos();
                case FIN_PARTIDA -> finDePartida();
                default -> System.out.println("MENSAJE DE PRUEBA");
            }
        }
    }
}
