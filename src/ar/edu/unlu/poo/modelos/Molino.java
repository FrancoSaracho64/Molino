package ar.edu.unlu.poo.modelos;

import ar.edu.unlu.poo.enumerados.*;
import ar.edu.unlu.poo.interfaces.IMolino;
import ar.edu.unlu.poo.persistencia.PartidaGuardada;
import ar.edu.unlu.poo.persistencia.Persistencia;
import ar.edu.unlu.poo.reglas.ReglasDelJuego;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Molino extends ObservableRemoto implements Serializable, IMolino {
    private final int MOVIMIENTOS_SIN_ELIMINAR_FICHAS = 30;
    public static final int CANTIDAD_FICHAS = 9;
    public static final String JUGADOR_1 = "X";
    public static final String JUGADOR_2 = "O";
    public static final String CASILLA_DISPONIBLE = "#";
    public static final String CASILLA_INVALIDA = "";
    private int movimientosSinCaptura;
    private boolean juegoComenzado;
    private String jugadorUltimoMolino;
    private Jugador jugadorActual;
    private Jugador jugador1;
    private Jugador jugador2;
    private final ArrayList<Jugador> jugadores;
    private final ArrayList<Jugador> jugadoresRegistrados;
    private final Tablero tablero;
    private final ReglasDelJuego reglas;
    private boolean ultimoMovimientoFueMolino;
    private MotivoFinPartida motivoFinPartida;

    public Molino() {
        this.jugadores = new ArrayList<>();
        this.tablero = new Tablero();
        this.reglas = new ReglasDelJuego(tablero);
        this.juegoComenzado = false;
        this.ultimoMovimientoFueMolino = false;
        this.jugadoresRegistrados = Persistencia.cargarJugadoresHistorico();
    }

    /**
     * Se comienza la partida.
     * Si es una partida nueva, es decir, no hay archivos registrados desde antes, se reinician todos los datos necesarios
     * para poder comenzar. Caso contrario, se sigue de largo.
     */
    @Override
    public void comenzarJuego() throws RemoteException {
        if (jugador1 != null && jugador2 != null) {
            notificarObservadores(EventosTablero.INICIO_PARTIDA);
        } else {
            jugador1 = obtenerJ1();
            jugador2 = obtenerJ2();
            movimientosSinCaptura = 0;
            prepararFichas();
            juegoComenzado = true;
            notificarObservadores(EventosTablero.INICIO_PARTIDA);
        }
    }

    @Override
    public String nombreJugadorDelMolino() throws RemoteException {
        return jugadorUltimoMolino;
    }

    private void prepararFichas() {
        jugador1.resetearFichasColocadas();
        jugador1.resetearFichasEnTablero();
        jugador1.setFichas(Molino.generarFichas(jugador1));
        jugador2.resetearFichasColocadas();
        jugador2.resetearFichasEnTablero();
        jugador2.setFichas(Molino.generarFichas(jugador2));
    }

    private void finPartida() throws RemoteException {
        // Informar que el juego ha terminado.
        Jugador ganador = reglas.obtenerGanador(jugador1, jugador2);
        if (ganador == null) {
            jugador1.empataPartida();
            jugador2.empataPartida();
        } else {
            ganador.ganaPartida();
            if (jugador1.equals(ganador)) {
                jugador2.pierdePartida();
            } else {
                jugador1.pierdePartida();
            }
        }
        ArrayList<Jugador> jugadoresHistoricos = Persistencia.cargarJugadoresHistorico();
        for (int i = 0; i < jugadoresHistoricos.size(); i++) {
            if (jugadoresHistoricos.get(i).equals(jugador1)) {
                jugadoresHistoricos.set(i, jugador1);
            } else if (jugadoresHistoricos.get(i).equals(jugador2)) {
                jugadoresHistoricos.set(i, jugador2);
            }
        }
        Persistencia.guardarJugadores(jugadoresHistoricos);
        notificarObservadores(EventosTablero.FIN_PARTIDA);
    }

    @Override
    public String obtenerContenidoCasilla(Coordenada coordenada) {
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
    public boolean verificarMolinoTrasMovimiento(Coordenada coordenada, Jugador jugador) throws RemoteException {
        // Si el jugador ya tiene 3 fichas en el tablero, hacemos la verificación de Molino.
        if (jugador.equals(jugadorActual)) {
            if (jugadorActual.getFichasColocadas() >= 3) {
                if (reglas.hayMolinoEnPosicion(coordenada, jugadorActual)) {
                    jugadorUltimoMolino = jugadorActual.getNombre();
                    ultimoMovimientoFueMolino = true;
                    notificarObservadores(EventosTablero.MOLINO);
                    return reglas.hayFichasParaEliminar(obtenerJugadorOponente());
                } else {
                    ultimoMovimientoFueMolino = false;
                }
            }
        }
        return false;
    }

    @Override
    public void finalizarTurno() throws RemoteException {
        if (jugadorActual.getFichasColocadas() < CANTIDAD_FICHAS && obtenerJugadorOponente().getFichasColocadas() < CANTIDAD_FICHAS) {
            // Si ambos jugadores no han colocado todas sus fichas, simplemente se invierte el turno.
            cambiarTurnoJugador();
        } else if (jugadorActual.getFichasColocadas() == CANTIDAD_FICHAS && obtenerJugadorOponente().getFichasColocadas() < CANTIDAD_FICHAS) {
            // Si el jugador actual ha colocado todas sus fichas pero su oponente no, se invierte el turno.
            cambiarTurnoJugador();
        } else if (juegoSigueActivo()) {
            // Si ambos jugadores han colocado todas sus fichas y el juego sigue activo, se invierte el turno.
            cambiarTurnoJugador();
        } else {
            // Si ninguna de las condiciones anteriores se cumple, significa que el juego ha terminado.
            finPartida();
        }
    }

    @Override
    public MotivoFinPartida obtenerMotivoFinPartida() throws RemoteException {
        return motivoFinPartida;
    }

    @Override
    public Jugador obtenerGanador() throws RemoteException {
        return reglas.obtenerGanador(jugador1, jugador2);
    }

    @Override
    public boolean hayJugadoresRegistrados() throws RemoteException {
        return !jugadoresRegistrados.isEmpty();
    }

    @Override
    public ArrayList<Jugador> obtenerJugadoresRegistrados() throws RemoteException {
        return jugadoresRegistrados;
    }

    @Override
    public boolean jugadorEstaDisponible(int pos) throws RemoteException {
        Jugador jugadorSolicitado = jugadoresRegistrados.get(pos);
        return !jugadores.contains(jugadorSolicitado);
    }

    @Override
    public boolean existeNombreJugador(String nombre) throws RemoteException {
        for (Jugador jugador : jugadoresRegistrados) {
            if (jugador.getNombre().equals(nombre)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean casillaOcupadaPorJugadorLocal(Coordenada coordenada, Jugador jugador) throws RemoteException {
        return tablero.obtenerFicha(coordenada).getJugador().equals(jugador);
    }

    @Override
    public boolean casillaOcupadaPorOponente(Coordenada coordenada, Jugador jugador) throws RemoteException {
        return tablero.obtenerFicha(coordenada).getJugador().equals(obtenerOponente(jugador));
    }

    @Override
    public boolean hayPartidaActiva() throws RemoteException {
        return juegoComenzado;
    }

    @Override
    public void removerJugador(Jugador jugadorLocal) throws RemoteException {
        jugadores.remove(jugadorLocal);
    }

    @Override
    public boolean esCasillaLibre(Coordenada coordenada) throws RemoteException {
        return tablero.obtenerEstadoCasilla(coordenada) == EstadoCasilla.LIBRE;
    }

    @Override
    public void guardarPartida() throws RemoteException {
        jugadores.clear();
        ArrayList<PartidaGuardada> partidaGuardadas = Persistencia.cargarPartidasGuardadas();
        partidaGuardadas.add(new PartidaGuardada(this, jugador1.getNombre(), jugador2.getNombre(), LocalDateTime.now()));
        Persistencia.guardarPartida(partidaGuardadas);
        notificarObservadores(EventosTablero.PARTIDA_SUSPENDIDA);
    }

    @Override
    public boolean esPartidaNueva() throws RemoteException {
        return jugador1 == null && jugador2 == null && !juegoComenzado;
    }

    @Override
    public ArrayList<Jugador> obtenerJugadoresParaReanudar() throws RemoteException {
        ArrayList<Jugador> jugadoresReanudados = new ArrayList<>();
        jugadoresReanudados.add(jugador1);
        jugadoresReanudados.add(jugador2);
        return jugadoresReanudados;
    }

    @Override
    public boolean jugadorParaReanudarDisponible(int pos) throws RemoteException {
        if (jugadores.isEmpty()) {
            return true;
        }
        if (pos == 0) {
            return !jugadores.contains(jugador1);
        } else {
            return !jugadores.contains(jugador2);
        }
    }

    @Override
    public boolean jugadorTieneFichasPendientes(Jugador jugador) throws RemoteException {
        return getJugador(jugador).getFichasColocadas() < CANTIDAD_FICHAS;
    }

    @Override
    public boolean jugadorEstaEnVuelo(Jugador jugador) {
        return getJugador(jugador).getFichasColocadas() == CANTIDAD_FICHAS && getJugador(jugador).getFichasEnTablero() == 3;
    }

    @Override
    public void colocarFicha(Coordenada coordenada, Jugador jugador) throws RemoteException {
        if (jugador.equals(jugadorActual)) {
            tablero.colocarFicha(coordenada, jugadorActual.getFichaParaColocar());
            notificarObservadores(EventosTablero.CAMBIO_EN_EL_TABLERO);
        }
    }

    @Override
    public void quitarFicha(Coordenada coordenada, Jugador jugador) throws RemoteException {
        movimientosSinCaptura = 0;
        obtenerOponente(jugador).decFichasEnTablero();
        tablero.quitarFicha(coordenada);
        ultimoMovimientoFueMolino = false;
        notificarObservadores(EventosTablero.CAMBIO_EN_EL_TABLERO);
    }

    @Override
    public void moverFicha(Coordenada antCoord, Coordenada nueCoord) throws RemoteException {
        tablero.moverFicha(antCoord, nueCoord);
        if (reglas.hayMolinoEnPosicion(nueCoord, jugadorActual)) {
            if (reglas.hayFichasParaEliminar(obtenerOponente(jugadorActual))) {
                movimientosSinCaptura = 0;
                ultimoMovimientoFueMolino = true;
            }
        } else {
            ultimoMovimientoFueMolino = false;
        }
        movimientosSinCaptura++;
        System.out.println("Son " + movimientosSinCaptura + " despues de hacer el movimiento. ");
        notificarObservadores(EventosTablero.CAMBIO_EN_EL_TABLERO);
    }

    /**
     * Se verifica si se produjo o no un empate.
     *
     * @return True si hay empate. Caso contrario, False
     */
    private boolean hayEmpatePorMovimientosSinCaptura() throws RemoteException {
        return movimientosSinCaptura >= MOVIMIENTOS_SIN_ELIMINAR_FICHAS;
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
        // Persistencia de los jugadores.
        if (jugadoresRegistrados.isEmpty()) {
            jugadoresRegistrados.add(jugador);
        } else if (!jugadoresRegistrados.contains(jugador)) {
            jugadoresRegistrados.add(jugador);
        } else {
            for (int i = 0; i < jugadoresRegistrados.size(); i++) {
                if (jugadoresRegistrados.get(i).equals(jugador)) {
                    jugadoresRegistrados.set(i, jugador);
                }
            }
        }
        // Guardo los jugadores.
        Persistencia.guardarJugadores(jugadoresRegistrados);
        // El primer jugador que se conecta comienza la partida. Si la partida se reanuda, no se modifica el J-actual.
        if (jugadores.isEmpty() && jugadorActual == null) {
            jugadorActual = jugador;
        }
        jugadores.add(jugador);
        notificarObservadores(EventosTablero.JUGADOR_CONECTADO);
        if (jugadores.size() == 2) {
            comenzarJuego();
        }
    }

    @Override
    public void jugadorHaAbandonado(Jugador jugador) throws RemoteException {
        // El jugador que se recibe por parametro es el que abandonó la partida.
        finPartidaPorAbandono(getJugador(jugador));
    }

    private void finPartidaPorAbandono(Jugador jugador) throws RemoteException {
        Jugador ganador = obtenerOponente(jugador);
        ganador.ganaPartida();
        if (jugador1.equals(ganador)) {
            jugador2.pierdePartida();
        } else {
            jugador1.pierdePartida();
        }
        ArrayList<Jugador> jugadores_historicos = Persistencia.cargarJugadoresHistorico();
        for (int i = 0; i < jugadores_historicos.size(); i++) {
            if (jugadores_historicos.get(i).equals(jugador1)) {
                jugadores_historicos.set(i, jugador1);
            } else if (jugadores_historicos.get(i).equals(jugador2)) {
                jugadores_historicos.set(i, jugador2);
            }
        }
        Persistencia.guardarJugadores(jugadores_historicos);
        // Informar que el juego ha terminado.
        notificarObservadores(EventosTablero.FIN_PARTIDA_ABANDONO);
    }

    private Jugador obtenerJugadorOponente() throws RemoteException {
        return (jugadorActual.equals(jugador1)) ? jugador2 : jugador1;
    }

    @Override
    public boolean esTurnoDe(Jugador jugador) throws RemoteException {
        return jugadorActual.equals(jugador);
    }

    @Override
    public boolean esCasillaValida(Coordenada coordenada) throws RemoteException {
        return reglas.esCasillaValida(coordenada);
    }

    @Override
    public boolean hayFichasParaEliminarDelOponente(Jugador jugador) throws RemoteException {
        return reglas.hayFichasParaEliminar(obtenerOponente(jugador));
    }

    @Override
    public Accion determinarAccionJugador(Jugador jugador) {
        if (jugador.equals(jugadorActual)) {
            // Aquí puedes añadir más lógica, por ejemplo, si el jugador puede mover una ficha o si debe quitar una ficha del oponente
            if (jugadorActual.getFichasColocadas() < CANTIDAD_FICHAS) {
                return Accion.COLOCAR_FICHA;
            } else {
                if (jugadorActual.getFichasEnTablero() > 3) {
                    return Accion.MOVER_FICHA;
                } else {
                    return Accion.MOVER_FICHA_SUPER;
                }
            }
        } else
            return null;
    }

    @Override
    public boolean fichaTieneMovimientos(Coordenada coordenada) {
        return reglas.fichaTieneMovimiento(coordenada);
    }

    @Override
    public boolean sonCasillasAdyacentes(Coordenada cOrigen, Coordenada cDestino) {
        return reglas.sonCasillasAdyacentes(cOrigen, cDestino);
    }

    private Jugador obtenerJ1() throws RemoteException {
        return jugadores.get(0);
    }

    private Jugador obtenerJ2() throws RemoteException {
        return jugadores.get(1);
    }

    private void cambiarTurnoJugador() throws RemoteException {
        jugadorActual = jugadorActual.equals(jugador1) ? jugador2 : jugador1;
        notificarObservadores(EventosTablero.CAMBIO_TURNO_JUGADOR);
    }

    /**
     * Transformamos el jugador que nos trae el controlador al jugador que tenemos en el modelo.
     * Esto lo hacemos para evitar problemas.
     *
     * @param jugador Jugador enviado desde el controlador.
     * @return Se retorna el jugador almacenado en el modelo. Puede ser el J1 o J2.
     */
    private Jugador getJugador(Jugador jugador) {
        return (jugador.equals(jugador1)) ? jugador1 : jugador2;
    }

    @Override
    public Jugador obtenerOponente(Jugador jugador) {
        return (jugador.equals(jugador1)) ? jugador2 : jugador1;
    }

    /**
     * Verificamos el estado del juego. Luego de que ambos jugadores hayan colocado las 3 fichas, se comienzan a hacer
     * las validaciones correspondientes al estado de la partida.
     * <p></p>
     * Si la partida finaliza, se inicializa "mmotivoFinPartida", para obtener el valor desde el controlador.
     *
     * @return Si la partida sigue en juego, se retorna True. Caso contrario, False.
     */
    @Override
    public boolean juegoSigueActivo() throws RemoteException {
        if (hayEmpatePorMovimientosSinCaptura()) {
            motivoFinPartida = MotivoFinPartida.EMPATE_POR_MOVIMIENTOS_SIN_CAPTURA;
            return false;
        }
        if (obtenerJugadorOponente().getFichasColocadas() == CANTIDAD_FICHAS && obtenerJugadorOponente().getFichasEnTablero() <= 2) {
            motivoFinPartida = MotivoFinPartida.JUGADOR_SIN_FICHAS;
            return false;
        } else if (obtenerJugadorOponente().getFichasColocadas() >= 1 && jugadorActual.getFichasColocadas() >= 1) {
            if (!reglas.jugadorTieneMovimientos(obtenerJugadorOponente())) {
                if (obtenerJugadorOponente().getFichasEnTablero() == 3 || ultimoMovimientoFueMolino) { // Ignoramos porque el jugador está en vuelo.
                    return true;
                }
                motivoFinPartida = MotivoFinPartida.JUGADOR_SIN_MOVIMIENTOS;
                return false;
            }
        }
        // Si no pasó por ninguno anterior, el juego sigue.
        return true;
    }

    @Override
    public boolean fichaSePuedeEliminar(Coordenada coordenada, Jugador jugador) throws RemoteException {
        Jugador op = obtenerOponente(jugador);
        if (op.getFichasColocadas() == CANTIDAD_FICHAS && op.getFichasEnTablero() == 3) {
            return true;
        } else {
            return !reglas.hayMolinoEnPosicion(coordenada, op);
        }
    }

    @Override
    public boolean ultimoMovimientoFueMolino() {
        return ultimoMovimientoFueMolino;
    }
}