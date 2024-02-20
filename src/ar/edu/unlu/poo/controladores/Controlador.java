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
    private Jugador oponente;
    private EstadoJuego estadoActual;
    private Coordenada coordTemporalMovimiento;

    public Controlador() {
    }

    public void iniciarPartida() throws RemoteException {
        // Comenzar el juego
        setOponente();
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
            vista.mostrarTurnoActual();
            return;
        }
        switch (estadoActual) {
            case COLOCAR_FICHA -> {
                if (validarCasillaValida(coordenada) && validarCasillaLibre(coordenada)) {
                    modelo.colocarFicha(coordenada, jugadorLocal);
                    if (modelo.verificarMolinoTrasMovimiento(coordenada, jugadorLocal)) {
                        vista.avisoJugadorHizoMolino();
                        if (modelo.hayFichasParaEliminar(oponente)) {
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
                            if (modelo.hayFichasParaEliminar(oponente)) {
                                cambiarEstadoYActualizarVista(EstadoJuego.SELECCIONAR_FICHA_PARA_ELIMINAR);
                            } else {
                                vista.avisoNoHayFichasParaEliminarDelOponente();
                                finalizarTurno();
                            }
                        } else {
                            finalizarTurno();
                        }
                    } else {
                        vista.casillaNoAdyacente();
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
                        if (modelo.hayFichasParaEliminar(oponente)) {
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
                        if (modelo.casillaOcupadaPorJugador(coordenada, oponente)) {
                            if (modelo.fichaSePuedeEliminar(coordenada, oponente)) {
                                modelo.quitarFicha(coordenada, oponente);
                                finalizarTurno();
                            } else {
                                vista.mensajeFichaFormaMolino();
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

    private void cambiarEstadoYActualizarVista(EstadoJuego nuevoEstado) {
        estadoActual = nuevoEstado;
        vista.actualizarParaAccion(nuevoEstado);
    }

    private void finalizarTurno() {
        try {
            modelo.finalizarTurno();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String contenidoCasilla(Coordenada coordenada) throws RemoteException {
        return modelo.getContenidoCasilla(coordenada);
    }

    @Override
    public void setVista(IVista vista) {
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
        if (!modelo.casillaOcupadaPorJugador(coordenada, jugadorLocal)) {
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
                vista.empatePorMovimientosSinComerFichas();
            }
        } else {
            vista.mostrarGanador(ganador.getNombre());
            if (ganador.equals(jugadorLocal)) {
                vista.mensajeAlGanador();
            } else {
                switch (modelo.obtenerMotivoFinPartida()) {
                    case JUGADOR_SIN_MOVIMIENTOS -> vista.jugadorSinMovimientos();
                    case JUGADOR_SIN_FICHAS -> vista.jugadorSinFichas();
                }
                vista.mensajeAlPerdedor();
            }
        }
    }

    @Override
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
                case CAMBIO_EN_EL_TABLERO -> vista.actualizarTablero();
                case CAMBIO_TURNO_JUGADOR -> actualizarVistaNuevoTurno();
                case JUGADOR_SIN_FICHAS -> vista.jugadorSinFichas();
                case JUGADOR_SIN_MOVIMIENTOS -> vista.jugadorSinMovimientos();
                case FIN_PARTIDA -> finDePartida();
                case FIN_PARTIDA_ABANDONO -> finDePartidaPorAbandono();
                case PARTIDA_SUSPENDIDA -> cambiarEstadoYActualizarVista(EstadoJuego.PARTIDA_SUSPENDIDA);
            }
        }
    }

    private void finDePartidaPorAbandono() {
        cambiarEstadoYActualizarVista(EstadoJuego.PARTIDA_TERMINADA);
        vista.mensajeAlGanador();
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
    public boolean existeElNombre(String nombre) throws RemoteException {
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
    public boolean laPartidaHaComenzado() throws RemoteException {
        return modelo.hayPartidaActiva();
    }

    @Override
    public void guardarPartidaEstadoActual() throws RemoteException {
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
    public String nombreJugador() throws RemoteException {
        return jugadorLocal.getNombre();
    }

    @Override
    public boolean laPartidaSigueActiva() throws RemoteException {
        return modelo.juegoSigueActivo();
    }
}