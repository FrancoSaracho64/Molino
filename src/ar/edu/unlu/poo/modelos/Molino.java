package ar.edu.unlu.poo.modelos;

import ar.edu.unlu.poo.enumerados.Accion;
import ar.edu.unlu.poo.enumerados.EstadoCasilla;
import ar.edu.unlu.poo.enumerados.EventosTablero;
import ar.edu.unlu.poo.enumerados.MotivoFinPartida;
import ar.edu.unlu.poo.interfaces.IMolino;
import ar.edu.unlu.poo.persistencia.Persistencia;
import ar.edu.unlu.poo.reglas.ReglasDelJuego;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Molino extends ObservableRemoto implements IMolino {
    public static final int CANTIDAD_FICHAS = 9;
    private static final String CASILLA_DISPONIBLE = "#";
    private static final String CASILLA_INVALIDA = "";
    private static final String JUGADOR_1 = "X";
    private static final String JUGADOR_2 = "O";
    private int movimientosSinCaptura;
    private boolean juegoComenzado;
    private Jugador ultimoMolino;
    private Jugador jugadorActual;
    private Jugador jugador1;
    private Jugador jugador2;
    private final ArrayList<Jugador> jugadores;
    private final ArrayList<Jugador> jugadoresRegistrados;
    private final Tablero tablero;
    private final ReglasDelJuego reglas;
    private MotivoFinPartida motivoFinPartida;

    public Molino() {
        this.jugadores = new ArrayList<>();
        this.tablero = new Tablero();
        this.reglas = new ReglasDelJuego(tablero);
        this.juegoComenzado = false;
        this.jugadoresRegistrados = Persistencia.cargarJugadoresHistorico();
    }

    @Override
    public void comenzarJuego() throws RemoteException {
        jugador1 = obtenerJ1();
        jugador2 = obtenerJ2();
        movimientosSinCaptura = 0;
        prepararFichas();
        juegoComenzado = true;
        notificarObservadores(EventosTablero.INICIO_PARTIDA);
    }

    @Override
    public Jugador getOponente(Jugador jugadorLocal) {
        if (jugadorLocal.equals(jugador1)) {
            return jugador2;
        } else {
            return jugador1;
        }
    }

    @Override
    public String getNombreMolino() throws RemoteException {
        return ultimoMolino.getNombre();
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
        notificarObservadores(EventosTablero.FIN_PARTIDA);
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
        ArrayList<Jugador> jugadores_historicos = Persistencia.cargarJugadoresHistorico();
        for (int i = 0; i < jugadores_historicos.size(); i++) {
            if (jugadores_historicos.get(i).equals(jugador1)) {
                jugadores_historicos.set(i, jugador1);
            } else if (jugadores_historicos.get(i).equals(jugador2)) {
                jugadores_historicos.set(i, jugador2);
            }
        }
        Persistencia.guardarJugadores(jugadores_historicos);
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
    public boolean verificarMolinoTrasMovimiento(Coordenada coordenada, Jugador jugador) throws RemoteException {
        // Si el jugador ya tiene 3 fichas en el tablero, hacemos la verificación de Molino.
        if (jugador.equals(jugadorActual)) {
            if (jugadorActual.getFichasColocadas() >= 3) {
                if (reglas.hayMolinoEnPosicion(coordenada, jugadorActual)) {
                    ultimoMolino = jugadorActual;
                    notificarObservadores(EventosTablero.MOLINO);
                    return reglas.hayFichasParaEliminar(obtenerJugadorOponente());
                }
            }
        }
        return false;
    }

    @Override
    public void finalizarTurno() throws RemoteException {
        finalizarTurnoJugadorActual();
    }

    @Override
    public MotivoFinPartida obtenerMotivoFinPartida() throws RemoteException {
        return motivoFinPartida;
    }

    @Override
    public Coordenada generarCoordenada(Object[] coordenada) {
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
    public boolean casillaOcupadaPorJugador(Coordenada coordenada, Jugador jugador) throws RemoteException {
        return tablero.obtenerFicha(coordenada).getJugador().equals(jugador);
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
    public void colocarFicha(Coordenada coordenada, Jugador jugador) throws RemoteException {
        if (jugador.equals(jugadorActual)) {
            tablero.colocarFicha(coordenada, jugadorActual.getFichaParaColocar());
            // Mostramos el cambio de la nueva ficha ingresada
            notificarObservadores(EventosTablero.CAMBIO_EN_EL_TABLERO);
        } else {
            throw new RemoteException("No es tu turno.");
        }
    }

    @Override
    public void quitarFicha(Coordenada coordenada, Jugador jugador) throws RemoteException {
        movimientosSinCaptura = 0;
        getJugador(jugador).decFichasEnTablero();
        tablero.quitarFicha(coordenada);
        // Finalizamos el turno y luego actualizamos la vista.
        notificarObservadores(EventosTablero.CAMBIO_EN_EL_TABLERO);
    }

    @Override
    public void moverFicha(Coordenada antCoord, Coordenada nueCoord) throws RemoteException {
        tablero.moverFicha(antCoord, nueCoord);
        if (reglas.hayMolinoEnPosicion(nueCoord, jugadorActual)) {
            if (reglas.hayFichasParaEliminar(getOponente(jugadorActual))) {
                movimientosSinCaptura = 0;
            }
        }
        movimientosSinCaptura++;
        System.out.println("Son " + movimientosSinCaptura + " despues de hacer el movimiento. ");
        if (verificarEmpatePorMovimientosSinCaptura()) {
            motivoFinPartida = MotivoFinPartida.EMPATE_POR_MOVIMIENTOS_SIN_CAPTURA;
            finPartida();
        } else {
            // Finalizamos el turno y luego actualizamos la vista.
            notificarObservadores(EventosTablero.CAMBIO_EN_EL_TABLERO);
        }
    }

    /**
     * Se verifica si se produjo o no un empate.
     *
     * @return True si hay empate. Caso contrario, False
     * @throws RemoteException
     */
    private boolean verificarEmpatePorMovimientosSinCaptura() throws RemoteException {
        return movimientosSinCaptura >= 4;
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

        // El primer jugador que se conecta comienza la partida.
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
    public void jugadorHaAbandonado(Jugador jugador) throws RemoteException {
        // El jugador que se recibe por parametro es el que abandonó la partida.
        finPartidaPorAbandono(jugador);
    }

    private void finPartidaPorAbandono(Jugador jugador) throws RemoteException {
        // Informar que el juego ha terminado.
        notificarObservadores(EventosTablero.FIN_PARTIDA);
        Jugador ganador = getOponente(jugador);
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
        jugadorActual = getJugador(jugador);
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
    public boolean hayFichasParaEliminar(Jugador oponente) throws RemoteException {
        return reglas.hayFichasParaEliminar(getJugador(oponente));
    }

    @Override
    public boolean hayMolinoEnPosicion(Coordenada coord, Jugador jugadorOponente) {
        return reglas.hayMolinoEnPosicion(coord, jugadorOponente);
    }

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
        return jugadores.getFirst();
    }

    private Jugador obtenerJ2() throws RemoteException {
        return jugadores.get(1);
    }

    /*private void finalizarTurnoJugadorActual() throws RemoteException {
        cambiarTurnoJugador();
        // Si ambos jugadores han colocado todas sus fichas, entonces verificamos el estado de juego activo.
        if (todosHanColocadoTodasLasFichas()) {
            if (!juegoActivo()) {
                // Si el juego no está activo, finaliza la partida.
                finPartida();
            }
        }
    }*/

    private void finalizarTurnoJugadorActual() throws RemoteException {
        if (jugadorActual.getFichasColocadas() < CANTIDAD_FICHAS && obtenerJugadorOponente().getFichasColocadas() < CANTIDAD_FICHAS) {
            // Si ambos jugadores no han colocado todas sus fichas, simplemente se invierte el turno.
            cambiarTurnoJugador();
        } else if (jugadorActual.getFichasColocadas() == CANTIDAD_FICHAS && obtenerJugadorOponente().getFichasColocadas() < CANTIDAD_FICHAS) {
            // Si el jugador actual ha colocado todas sus fichas pero su oponente no, se invierte el turno.
            cambiarTurnoJugador();
        } else if (juegoActivo()) {
            // Si ambos jugadores han colocado todas sus fichas y el juego sigue activo, se invierte el turno.
            cambiarTurnoJugador();
        } else {
            // Si ninguna de las condiciones anteriores se cumple, significa que el juego ha terminado.
            finPartida();
        }
    }

    private void cambiarTurnoJugador() {
        jugadorActual = jugadorActual.equals(jugador1) ? jugador2 : jugador1;
    }

    /**
     * Transformamos el jugador que nos trae el controlador al jugador que tenemos en el modelo.
     * Esto lo hacemos para evitar problemas.
     *
     * @param jugador Jugador enviado desde el controlador.
     * @return Se retorna el jugador almacenado en el modelo. Puede ser el J1 o J2.
     */
    private Jugador getJugador(Jugador jugador) {
        if (jugador.equals(jugador1))
            return jugador1;
        else return jugador2;
    }

    /**
     * Verificamos el estado del juego. Luego de que ambos jugadores hayan colocado las 3 fichas, se comienzan a hacer
     * las validaciones correspondientes al estado de la partida.
     * <p></p>
     * Si la partida finaliza, se inicializa "mmotivoFinPartida", para obtener el valor desde el controlador.
     *
     * @return Si la partida sigue en juego, se retorna True. Caso contrario, False.
     * @throws RemoteException
     */
    private boolean juegoActivo() throws RemoteException {
        if (obtenerJugadorOponente().getFichasEnTablero() <= 2) {
            motivoFinPartida = MotivoFinPartida.JUGADOR_SIN_FICHAS;
            return false;
        } else if (!reglas.jugadorTieneMovimientos(obtenerJugadorOponente())) {
            motivoFinPartida = MotivoFinPartida.JUGADOR_SIN_MOVIMIENTOS;
            return false;
        }
        // Si no pasó por ninguno anterior, el juego sigue.
        return true;
    }
}