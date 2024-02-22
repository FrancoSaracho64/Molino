package ar.edu.unlu.poo.controladores;

import java.rmi.RemoteException;
import java.util.ArrayList;

import ar.edu.unlu.poo.enumerados.EstadoJuego;
import ar.edu.unlu.poo.enumerados.EventosTablero;
import ar.edu.unlu.poo.enumerados.MotivoFinPartida;
import ar.edu.unlu.poo.interfaces.IControlador;
import ar.edu.unlu.poo.interfaces.IMolino;
import ar.edu.unlu.poo.interfaces.IVista;
import ar.edu.unlu.poo.modelos.Coordenada;
import ar.edu.unlu.poo.modelos.Jugador;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

public class Controlador implements IControladorRemoto, IControlador {
    private IMolino modelo;
    private IVista vista;
    private Jugador jugadorLocal;
    private EstadoJuego estadoActual;
    private Coordenada coordTemporalMovimiento;

    public Controlador() {
    }

    public void iniciarPartida() throws RemoteException {
        // Comenzar el juego
        if (modelo.esTurnoDe(jugadorLocal)) {
            // Validaciones necesarias por si se reanuda la partida
            if (modelo.ultimoMovimientoFueMolino()) {
                cambiarEstadoYActualizarVista(EstadoJuego.SELECCIONAR_FICHA_PARA_ELIMINAR);
            } else {
                if (modelo.jugadorTieneFichasPendientes(jugadorLocal)) {
                    cambiarEstadoYActualizarVista(EstadoJuego.COLOCAR_FICHA);
                } else {
                    if (modelo.jugadorEstaEnVuelo(jugadorLocal)) {
                        cambiarEstadoYActualizarVista(EstadoJuego.SELECCIONAR_ORIGEN_MOVER_SUPER);
                    } else {
                        cambiarEstadoYActualizarVista(EstadoJuego.SELECCIONAR_ORIGEN_MOVER);
                    }
                }
            }
        } else {
            cambiarEstadoYActualizarVista(EstadoJuego.ESPERANDO_TURNO);
        }
        vista.actualizarTablero();
    }

    public void actualizarVistaNuevoTurno() {
        try {
            if (modelo.esTurnoDe(jugadorLocal)) {
                switch (modelo.determinarAccionJugador(jugadorLocal)) {
                    case COLOCAR_FICHA -> cambiarEstadoYActualizarVista(EstadoJuego.COLOCAR_FICHA);
                    case MOVER_FICHA -> cambiarEstadoYActualizarVista(EstadoJuego.SELECCIONAR_ORIGEN_MOVER);
                    case MOVER_FICHA_SUPER -> cambiarEstadoYActualizarVista(EstadoJuego.SELECCIONAR_ORIGEN_MOVER_SUPER);
                }
            } else {
                cambiarEstadoYActualizarVista(EstadoJuego.ESPERANDO_TURNO);
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void casillaSeleccionadaDesdeLaVista(Coordenada coordenada) throws RemoteException {
        if (!modelo.esTurnoDe(jugadorLocal)) {
            vista.mostrarTurnoDelOponente();
            return;
        }
        switch (estadoActual) {
            case COLOCAR_FICHA -> {
                if (validarCasillaValida(coordenada) && validarCasillaLibre(coordenada)) {
                    modelo.colocarFicha(coordenada, jugadorLocal);
                    if (modelo.verificarMolinoTrasMovimiento(coordenada, jugadorLocal)) {
                        vista.avisoJugadorHizoMolino();
                        if (modelo.hayFichasParaEliminarDelOponente(jugadorLocal)) {
                            cambiarEstadoYActualizarVista(EstadoJuego.SELECCIONAR_FICHA_PARA_ELIMINAR);
                        } else {
                            //Le informamos al usuario que no hay fichas para eliminar.
                            vista.avisoNoHayFichasParaEliminarDelOponente();
                            finalizarTurno();
                        }
                    } else {
                        finalizarTurno();
                    }
                }
            }
            case SELECCIONAR_ORIGEN_MOVER -> {
                if (validarCasillaValida(coordenada) && !validarCasillaLibre(coordenada) && validarCasillaJugadorLocal(coordenada)) {
                    if (modelo.fichaTieneMovimientos(coordenada)) {
                        coordTemporalMovimiento = coordenada;
                        cambiarEstadoYActualizarVista(EstadoJuego.SELECCIONAR_DESTINO_MOVER);
                    } else {
                        vista.mostrarMensajeFichaSinMovimiento();
                    }
                }
            }
            case SELECCIONAR_DESTINO_MOVER -> {
                if (validarCasillaValida(coordenada) && validarCasillaLibre(coordenada)) {
                    if (modelo.sonCasillasAdyacentes(coordTemporalMovimiento, coordenada)) {
                        modelo.moverFicha(coordTemporalMovimiento, coordenada);
                        coordTemporalMovimiento = null;
                        if (modelo.verificarMolinoTrasMovimiento(coordenada, jugadorLocal)) {
                            vista.avisoJugadorHizoMolino();
                            if (modelo.hayFichasParaEliminarDelOponente(jugadorLocal)) {
                                cambiarEstadoYActualizarVista(EstadoJuego.SELECCIONAR_FICHA_PARA_ELIMINAR);
                            } else {
                                vista.avisoNoHayFichasParaEliminarDelOponente();
                                finalizarTurno();
                            }
                        } else {
                            finalizarTurno();
                        }
                    } else {
                        vista.avisoCasillaNoAdyacente();
                    }
                }
            }
            case SELECCIONAR_ORIGEN_MOVER_SUPER -> {
                if (validarCasillaValida(coordenada) && !validarCasillaLibre(coordenada) && validarCasillaJugadorLocal(coordenada)) {
                    coordTemporalMovimiento = coordenada;
                    cambiarEstadoYActualizarVista(EstadoJuego.SELECCIONAR_DESTINO_MOVER_SUPER);
                }
            }
            case SELECCIONAR_DESTINO_MOVER_SUPER -> {
                if (validarCasillaValida(coordenada) && validarCasillaLibre(coordenada)) {
                    modelo.moverFicha(coordTemporalMovimiento, coordenada);
                    coordTemporalMovimiento = null;
                    if (modelo.verificarMolinoTrasMovimiento(coordenada, jugadorLocal)) {
                        vista.avisoJugadorHizoMolino();
                        if (modelo.hayFichasParaEliminarDelOponente(jugadorLocal)) {
                            cambiarEstadoYActualizarVista(EstadoJuego.SELECCIONAR_FICHA_PARA_ELIMINAR);
                        } else {
                            vista.avisoNoHayFichasParaEliminarDelOponente();
                            finalizarTurno();
                        }
                    } else {
                        finalizarTurno();
                    }
                }
            }
            case SELECCIONAR_FICHA_PARA_ELIMINAR -> {
                if (validarCasillaValida(coordenada)) {
                    if (!modelo.esCasillaLibre(coordenada)) {
                        if (modelo.casillaOcupadaPorOponente(coordenada, jugadorLocal)) {
                            if (modelo.fichaSePuedeEliminar(coordenada, jugadorLocal)) {
                                modelo.quitarFicha(coordenada, jugadorLocal);
                                finalizarTurno();
                            } else {
                                vista.mostrarMensajeFichaFormaMolino();
                            }
                        } else {
                            vista.mostrarMensajeNoCorrespondeAlOponente();
                        }
                    } else {
                        vista.mostrarMensajeCasillaLibre();
                    }
                }
            }
        }
    }

    private void cambiarEstadoYActualizarVista(EstadoJuego nuevoEstado) throws RemoteException {
        estadoActual = nuevoEstado;
        vista.actualizarVistaParaAccion(nuevoEstado);
    }

    private void finalizarTurno() {
        try {
            modelo.finalizarTurno();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String obtenerContenidoCasilla(Coordenada coordenada) throws RemoteException {
        return modelo.obtenerContenidoCasilla(coordenada);
    }

    @Override
    public void colocarVista(IVista vista) {
        this.vista = vista;
    }

    @Override
    public void agregarJugador(Jugador jugador) throws RemoteException {
        jugadorLocal = jugador;
        modelo.conectarJugador(jugador);
    }

    private boolean validarCasillaValida(Coordenada coordenada) throws RemoteException {
        if (!modelo.esCasillaValida(coordenada)) {
            vista.mostrarMensajeErrorCasilla();
            return false;
        }
        return true;
    }

    private boolean validarCasillaLibre(Coordenada coordenada) throws RemoteException {
        if (!modelo.esCasillaLibre(coordenada)) {
            vista.mostrarMensajeCasillaOcupada();
            return false;
        }
        return true;
    }

    private boolean validarCasillaJugadorLocal(Coordenada coordenada) throws RemoteException {
        if (!modelo.casillaOcupadaPorJugadorLocal(coordenada, jugadorLocal)) {
            vista.mostrarMensajeNoCorrespondeAlJugador();
            return false;
        }
        return true;
    }

    private void finDePartida() throws RemoteException {
        Jugador ganador = modelo.obtenerGanador();
        cambiarEstadoYActualizarVista(EstadoJuego.PARTIDA_TERMINADA);
        if (ganador == null) {
            vista.mostrarEmpate();
            if (modelo.obtenerMotivoFinPartida().equals(MotivoFinPartida.EMPATE_POR_MOVIMIENTOS_SIN_CAPTURA)) {
                vista.avisoEmpatePorMovimientosSinComerFichas();
            }
        } else {
            vista.mostrarGanador(ganador.getNombre());
            if (ganador.equals(jugadorLocal)) {
                vista.mostrarMensajeAlGanador();
            } else {
                switch (modelo.obtenerMotivoFinPartida()) {
                    case JUGADOR_SIN_MOVIMIENTOS -> vista.avisoJugadorSinMovimientos();
                    case JUGADOR_SIN_FICHAS -> vista.avisoJugadorSinFichas();
                }
                vista.mostrarMensajeAlPerdedor();
            }
        }
    }

    @Override
    public void aplicacionCerrada() throws RemoteException {
        try {
            if (modelo != null) {
                modelo.removerObservador(this);
                modelo.removerJugador(jugadorLocal);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
                case MOLINO -> vista.avisoDeMolino(modelo.nombreJugadorDelMolino());
                case CAMBIO_EN_EL_TABLERO -> vista.actualizarTablero();
                case CAMBIO_TURNO_JUGADOR -> actualizarVistaNuevoTurno();
                case JUGADOR_SIN_FICHAS -> vista.avisoJugadorSinFichas();
                case JUGADOR_SIN_MOVIMIENTOS -> vista.avisoJugadorSinMovimientos();
                case FIN_PARTIDA -> finDePartida();
                case FIN_PARTIDA_ABANDONO -> finDePartidaPorAbandono();
                case PARTIDA_SUSPENDIDA -> cambiarEstadoYActualizarVista(EstadoJuego.PARTIDA_SUSPENDIDA);
            }
        }
    }

    private void finDePartidaPorAbandono() throws RemoteException {
        cambiarEstadoYActualizarVista(EstadoJuego.PARTIDA_TERMINADA);
        vista.mostrarMensajeAlGanador();
        vista.informarOponenteHaAbandonado();
    }

    @Override
    public boolean hayJugadoresRegistrados() throws RemoteException {
        return modelo.hayJugadoresRegistrados();
    }

    @Override
    public ArrayList<Jugador> obtenerJugadoresRegistrados() throws RemoteException {
        return modelo.obtenerJugadoresRegistrados();
    }

    @Override
    public boolean jugadorRegistradoEstaDisponible(int pos) throws RemoteException {
        return modelo.jugadorEstaDisponible(pos);
    }

    @Override
    public boolean esNombreYaRegistrado(String nombre) throws RemoteException {
        return modelo.existeNombreJugador(nombre);
    }

    @Override
    public void jugadorAbandona() {
        try {
            modelo.jugadorHaAbandonado(jugadorLocal);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean partidaHaComenzado() throws RemoteException {
        return modelo.hayPartidaActiva();
    }

    @Override
    public void guardarPartida() throws RemoteException {
        modelo.guardarPartida();
    }

    @Override
    public boolean esPartidaNueva() throws RemoteException {
        return modelo.esPartidaNueva();
    }

    @Override
    public ArrayList<Jugador> obtenerJugadoresParaReanudar() throws RemoteException {
        return modelo.obtenerJugadoresParaReanudar();
    }

    @Override
    public boolean jugadorParaReanudarDisponible(int pos) throws RemoteException {
        return modelo.jugadorParaReanudarDisponible(pos);
    }

    @Override
    public String obtenerNombreJugador() throws RemoteException {
        return jugadorLocal.getNombre();
    }

    @Override
    public boolean partidaSigueActiva() throws RemoteException {
        return modelo.juegoSigueActivo();
    }
}