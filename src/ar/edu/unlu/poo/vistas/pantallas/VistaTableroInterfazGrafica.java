package ar.edu.unlu.poo.vistas.pantallas;

import ar.edu.unlu.poo.controladores.Controlador;
import ar.edu.unlu.poo.interfaces.IVista;
import ar.edu.unlu.poo.modelos.Jugador;
import ar.edu.unlu.poo.modelos.Tablero;

import java.rmi.RemoteException;

public class VistaTableroInterfazGrafica implements IVista {
    private Controlador controlador;

    public VistaTableroInterfazGrafica(Controlador controlador) {
        this.controlador = controlador;
        this.controlador.setVista(this);
    }

    @Override
    public void iniciar() {

    }

    @Override
    public void mostrarTablero(Tablero tablero) {

    }

    @Override
    public void mostrarTurno(String mensaje) {

    }

    @Override
    public void mostrarMensajeErrorCasilla() {

    }

    @Override
    public void avisoDeMolino(String nombreJugador) {

    }

    @Override
    public Object[] pedirCasilla() {
        return new Object[0];
    }

    @Override
    public void fichaAEliminar() {

    }

    @Override
    public void mostrarGanador(String nombreJugador) {

    }

    @Override
    public void juegoTerminado() {

    }

    @Override
    public void fichaSinMovimiento() {

    }

    @Override
    public void casillaNoAdyacente() {

    }

    @Override
    public void mensajeCasillaFichaAMover() {

    }

    @Override
    public void mensajePedirNuevaCasillaLibreAdyacente() {

    }

    @Override
    public void mostrarEmpate() {

    }

    @Override
    public void mostrarMensajeCasillaOcupada() {

    }

    @Override
    public void mensajeFichaFormaMolino() {

    }

    @Override
    public void mensajePedirNuevaCasillaLibre() {

    }

    @Override
    public void avisoNoHayFichasParaEliminarDelOponente() {

    }

    @Override
    public void jugadorSinMovimientos() {

    }

    @Override
    public void jugadorSinFichas() {

    }

    @Override
    public void mostrarJugadorConectado() {

    }

    @Override
    public void mostrarTurnoActual(Jugador jugadorActual) {

    }

    @Override
    public void mostrarMensajeError(String mensaje) {

    }

    @Override
    public void actualizar() throws RemoteException {

    }
}
