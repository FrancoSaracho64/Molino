package ar.edu.unlu.poo.vistas;

import ar.edu.unlu.poo.controladores.Controlador;
import ar.edu.unlu.poo.interfaces.IVista;
import ar.edu.unlu.poo.modelos.Coordenada;
import ar.edu.unlu.poo.modelos.Jugador;
import ar.edu.unlu.poo.modelos.Tablero;
import ar.edu.unlu.poo.vistas.pantallas.MenuPrincipal;
import ar.edu.unlu.poo.vistas.utilidadesConsola.EntradaTeclado;

import java.rmi.RemoteException;

public class VistaConsola implements IVista {
    private Controlador controlador;

    public VistaConsola(Controlador controlador) {
        this.controlador = controlador;
        this.controlador.setVista(this);
    }

    private void limpiarConsola() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            ProcessBuilder processBuilder;
            if (os.contains("win")) {
                // Para Windows
                processBuilder = new ProcessBuilder("cmd", "/c", "cls");
            } else {
                // Para sistemas tipo Unix (Linux, macOS)
                processBuilder = new ProcessBuilder("clear");
            }
            Process process = processBuilder.inheritIO().start();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String letraColumna(int columna) {
        return String.valueOf((char) ('A' + columna));
    }

    private String numeroFila(int fila) {
        return String.valueOf(fila + 1);
    }

    private String formatoCelda(String contenido) {
        return String.format("%1s", contenido);
    }

    @Override
    public void iniciar() {
        new MenuPrincipal(this, controlador);
    }

    @Override
    public void mostrarTablero(Tablero tablero) throws RemoteException {
        int col = 0;
        int fil = 0;
        int filfin = 0;
        int colfin = 0;
        System.out.println("\n\n");
        // Muestra las letras de las columnas
        System.out.print("     ");  // Espacio inicial
        for (int columna = 0; columna < 13; columna++) {
            if (columna != 12) {
                if (columna % 2 == 0) {
                    System.out.print(" " + formatoCelda(letraColumna(col)) + "    ");
                    col++;
                }
            } else {
                System.out.print(" " + formatoCelda(letraColumna(col) + "      "));
            }
        }
        System.out.println();
        for (int fila = 0; fila < 13; fila++) {
            System.out.print(" ");
            if (fila % 2 == 0) {
                System.out.print(" " + formatoCelda(numeroFila(fil)) + "  "); // Número de fila
                fil++;
            } else {
                System.out.print("    ");
            }
            // Se representa el estado de la casilla
            for (int columna = 0; columna < 13; columna++) {
                String contenidoCelda = "";
                if (fila % 2 == 0 && columna % 2 == 0) {
                    contenidoCelda = controlador.contenidoCasilla(new Coordenada(fila/2, columna/2));
                }
                if (contenidoCelda.isEmpty()) {
                    if ((fila == 0 || fila == 12))
                        contenidoCelda = "-";
                    if ((fila == 2 || fila == 10) && (columna >= 2 && columna <= 10))
                        contenidoCelda = "-";
                    if ((fila == 4 || fila == 8) && (columna >= 4 && columna <= 8))
                        contenidoCelda = "-";
                    if ((fila == 6) && ((columna == 1) || (columna == 3) || (columna == 9) || (columna == 11)))
                        contenidoCelda = "-";
                    if ((columna == 0 || columna == 12))
                        contenidoCelda = "|";
                    if ((columna == 2 || columna == 10) && (fila >= 2 && fila <= 10))
                        contenidoCelda = "|";
                    if ((columna == 4 || columna == 8) && (fila >= 4 && fila <= 8))
                        contenidoCelda = "|";
                    if ((columna == 6) && ((fila == 1) || (fila == 3) || (fila == 9) || (fila == 11)))
                        contenidoCelda = "|";
                }
                System.out.print(" " + formatoCelda(contenidoCelda) + " ");
            }
            if (fila % 2 == 0) {
                System.out.print("  " + formatoCelda(numeroFila(filfin)));
                filfin++;
                System.out.print("  ");
            } else {
                System.out.print("     ");
            }
            System.out.println();
        }
        System.out.print("     ");

        for (int columna = 0; columna < 13; columna++) {
            if (columna != 12) {
                if (columna % 2 == 0) {
                    System.out.print(" " + formatoCelda(letraColumna(colfin)) + "    ");
                    colfin++;
                }
            } else {
                System.out.print(" " + formatoCelda(letraColumna(colfin) + "     "));
            }
        }
        System.out.print(" ");
        System.out.println("\n");
    }

    @Override
    public void mostrarTurno(String nombreJugador) {
        System.out.println("** Turno de: " + nombreJugador + " **");
    }

    @Override
    public void mostrarMensajeErrorCasilla() {
        System.out.println("Ha introducido una coordenada incorrecta. Vuelva a intentar.");
    }

    @Override
    public void avisoDeMolino(String nombreJugador) {
        System.out.println("¡El jugador " + nombreJugador + " hizo molino!");
        EntradaTeclado.presionarEnterParaContinuar();
    }

    @Override
    public Object[] pedirCasilla() {
        char columna = EntradaTeclado.pedirColumna();
        int fila = EntradaTeclado.pedirFila();
        return new Object[]{fila, columna};
    }

    @Override
    public void fichaAEliminar() {
        System.out.println("Introduzca la coordenada de la ficha a eliminar del oponente...");
    }

    @Override
    public void mostrarGanador(String nombreJugador) {
        System.out.println("\n\n\n");
        System.out.println("¡El ganador es: " + nombreJugador + "! ¡Felicitaciones! :D");
    }

    @Override
    public void juegoTerminado() {
        System.out.println("------ ** El juego ha terminado ** ------");
    }

    @Override
    public void fichaSinMovimiento() {
        System.out.println("La ficha seleccionada NO tiene movimientos. Intente con otra.");
    }

    @Override
    public void casillaNoAdyacente() {
        System.out.println("La casilla seleccionada no corresponde a una casilla adyacente. Intente con otra.");
    }

    @Override
    public void mensajePedirNuevaCasillaLibreAdyacente() {
        System.out.println("Ingrese una coordenada libre adyacente para colocar la ficha...");
    }

    @Override
    public void mostrarEmpate(String nombreJ1, String nombreJ2) {
        System.out.println("¡Se ha producido un EMPATE entre " + nombreJ1 + " y " + nombreJ2 + "!");
    }

    @Override
    public void mostrarMensajeCasillaOcupada() {
        System.out.println("La casilla que ha seleccionado está ocupada. Intente con otra.");
    }

    @Override
    public void mensajeFichaFormaMolino() {
        System.out.println("La pieza seleccionada forma un molino. Intente con otra.");
    }

    @Override
    public void mensajePedirNuevaCasillaLibre() {
        System.out.println("Ingrese cualquier coordenada libre para colocar la ficha...");
    }

    @Override
    public void avisoNoHayFichasParaEliminarDelOponente() {
        System.out.println("No hay fichas disponibles para eliminar en este turno. Mala suerte :/");
    }

    @Override
    public void jugadorSinMovimientos() {
        System.out.println("¡Te quedaste sin movimientos posibles! :/\n");
    }

    @Override
    public void jugadorSinFichas() {
        System.out.println("¡Te quedaste sin fichas suficientes! :/\n");
    }

    @Override
    public void mostrarJugadorConectado() {
        System.out.println("Se ha conectado un jugador");
    }

    @Override
    public void mostrarTurnoActual(Jugador jugadorActual) {

    }

    @Override
    public void mostrarMensajeError(String mensaje) {

    }

    @Override
    public void mensajeCasillaFichaAMover() {
        System.out.println("Ingrese la coordenada de la ficha que desea mover...");
    }

    @Override
    public void actualizar() throws RemoteException {
        limpiarConsola();
        mostrarTablero(controlador.obtenerTablero());
    }
}