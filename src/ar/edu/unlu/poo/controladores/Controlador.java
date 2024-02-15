package ar.edu.unlu.poo.controladores;

import java.rmi.RemoteException;
import java.util.ArrayList;

import ar.edu.unlu.poo.enumerados.EstadoCasilla;
import ar.edu.unlu.poo.enumerados.EventosTablero;
import ar.edu.unlu.poo.interfaces.IMolino;
import ar.edu.unlu.poo.interfaces.IVista;
import ar.edu.unlu.poo.modelos.Coordenada;
import ar.edu.unlu.poo.modelos.Ficha;
import ar.edu.unlu.poo.modelos.Jugador;
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
        vista.actualizar();
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
                            if (modelo.hayFichasParaEliminar(oponente)) {
                                eliminarFichaOponente(oponente);
                            } else {
                                //Le informamos al usuario que no hay fichas para eliminar.
                                vista.avisoNoHayFichasParaEliminarDelOponente();
                            }
                        }
                        finalizarTurno();
                    }
                    case MOVER_FICHA -> {
                        Coordenada origen = pedirCoordenadaFichaAMover(jugadorLocal);
                        Coordenada destino = pedirCoordenadaLibreAdyacente(origen);
                        modelo.moverFicha(origen, destino);
                        if (modelo.verificarMolinoTrasMovimiento(destino, jugadorLocal)) {
                            if (modelo.hayFichasParaEliminar(oponente)) {
                                eliminarFichaOponente(oponente);
                            } else {
                                //Le informamos al usuario que no hay fichas para eliminar.
                                vista.avisoNoHayFichasParaEliminarDelOponente();
                            }
                        }
                        finalizarTurno();
                    }
                    case MOVER_FICHA_SUPER -> {
                        Coordenada origen = pedirCoordenadaFichaAMover(jugadorLocal);
                        Coordenada destino = pedirCoordenadaLibre();
                        modelo.moverFicha(origen, destino);
                        if (modelo.verificarMolinoTrasMovimiento(destino, jugadorLocal)) {
                            if (modelo.hayFichasParaEliminar(oponente)) {
                                eliminarFichaOponente(oponente);
                            } else {
                                //Le informamos al usuario que no hay fichas para eliminar.
                                vista.avisoNoHayFichasParaEliminarDelOponente();
                            }
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

    public void setVista(IVista vista) {
        this.vista = vista;
    }

    public void agregarJugador(Jugador jugador) throws RemoteException {
        jugadorLocal = jugador;
        modelo.conectarJugador(jugador);
    }

    private Coordenada solicitarCasillaVista() throws RemoteException {
        Object[] coordenada = vista.pedirCasilla();
        return modelo.generarCoordenada(coordenada);
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
            boolean esDeJugador;
            boolean movimientos;
            Coordenada coord;
            do { // verifico que la ficha tenga movimientos.
                do { // verifico que la casilla esté ocupada por el jugador.
                    do { // verifico que sea una casilla válida.
                        vista.mensajeCasillaFichaAMover();
                        coord = solicitarCasillaVista();
                        valida = modelo.esCasillaValida(coord);
                        if (!valida) {
                            vista.mostrarMensajeErrorCasilla();
                        }
                    } while (!valida);
                    esDeJugador = modelo.casillaOcupadaPorJugador(coord, jugador);
                    if (!esDeJugador) {
                        vista.mostrarMensajeErrorCasilla();
                    }
                } while (!esDeJugador);
                movimientos = modelo.fichaTieneMovimientos(coord);
                if (!movimientos) {
                    vista.fichaSinMovimiento();
                }
            } while (!movimientos);
            return coord;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void finDePartida() throws RemoteException {
        Jugador ganador = modelo.obtenerGanador();
        vista.juegoTerminado();
        if (ganador == null) {
            vista.mostrarEmpate();
        } else {
            vista.mostrarGanador(ganador.getNombre());
            if (ganador.equals(jugadorLocal)) {
                //Soy el ganador...
                vista.mensajeAlGanador();
            } else {
                vista.mensajeAlPerdedor();
                switch (modelo.obtenerMotivoFinPartida()) {
                    case JUGADOR_SIN_MOVIMIENTOS -> {
                        vista.jugadorSinMovimientos();
                    }
                    case JUGADOR_SIN_FICHAS -> {
                        vista.jugadorSinFichas();
                    }
                }
            }
        }
    }

    public void cerrarAplicacion() throws RemoteException {
        try {
            if (modelo != null) {
                modelo.removerObservador(this);
                modelo.removerJugador(jugadorLocal);
            }
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

    public boolean hayJugadoresRegistrados() throws RemoteException {
        return modelo.hayJugadoresRegistrados();
    }

    public ArrayList<Jugador> obtenerJugadoresRegistrados() throws RemoteException {
        return modelo.obtenerJugadoresRegistrados();
    }

    public boolean jugadorRegistradoEstaDisponible(int pos) throws RemoteException {
        return modelo.jugadorEstaDisponible(pos);
    }

    public boolean existeElNombre(String nombre) throws RemoteException {
        return modelo.existeNombreJugador(nombre);
    }

    public void jugadorAbandona() {
        try {
            modelo.jugadorHaAbandonado(jugadorLocal);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean hayPartidaActiva() throws RemoteException {
        return modelo.hayPartidaActiva();
    }
}
