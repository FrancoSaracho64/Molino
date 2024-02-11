package ar.edu.unlu.poo.vistas.pantallas;

import ar.edu.unlu.poo.controladores.Controlador;
import ar.edu.unlu.poo.interfaces.IVista;
import ar.edu.unlu.poo.modelos.Coordenada;
import ar.edu.unlu.poo.modelos.Jugador;
import ar.edu.unlu.poo.modelos.Tablero;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class VistaTableroConsola implements IVista {
    private final JFrame frame1;
    private JPanel paneJ;
    private Controlador controlador;
    private JTextArea textArea;
    private JButton button;

    public VistaTableroConsola(Controlador controlador) {
        this.controlador = controlador;
        this.controlador.setVista(this);

        frame1 = new JFrame();
        frame1.setContentPane(paneJ);
        frame1.setTitle("Juego del Molino - Nueve hombres de Morris");
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setSize(600, 800);
        frame1.setVisible(true);

        //Eventos
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlador.realizarAccion();
            }
        });
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
    public void mostrarTablero(Tablero taba) throws RemoteException {
        StringBuilder tablero = new StringBuilder();
        String letras = "          A        B        C        D        E        F        G\n";
        tablero.append("\n\n");
        tablero.append(letras);
        for (int fila = 0; fila < 13; fila++) {
            tablero.append("     ");
            if (fila % 2 == 0) {
                tablero.append(formatoCelda(numeroFila(fila / 2))).append(" "); // Número de fila
            } else {
                tablero.append("   ");
            }
            for (int columna = 0; columna < 13; columna++) {
                String contenidoCelda ="";
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
                    if ((columna == 2) && (fila >= 2 && fila <= 10))
                        contenidoCelda = "|";
                    if ((columna == 10) && (fila >= 2 && fila <= 10))
                        contenidoCelda = "|";
                    if ((columna == 4 || columna == 8) && (fila >= 4 && fila <= 8))
                        contenidoCelda = "|";
                    if ((columna == 6) && ((fila == 1) || (fila == 3) || (fila == 9) || (fila == 11)))
                        contenidoCelda = "    |";
                }
                if (fila != 6) {
                    if ((fila == 0 || fila == 12) && columna == 6) {
                        tablero.append(" ").append(formatoCelda(contenidoCelda)).append(" ");
                    } else {
                        if ((fila == 1 || fila == 11) && columna == 12) tablero.append(" ");
                        if ((fila == 3 || fila == 9) && columna == 10) tablero.append(" ");
                        tablero.append("  ").append(formatoCelda(contenidoCelda)).append("  ");
                        if (fila == 5 && columna == 7) tablero.append("     ");
                        if (fila == 7 && columna == 7) tablero.append("     ");
                    }
                } else {
                    tablero.append("  ").append(formatoCelda(contenidoCelda)).append(" ");
                    if (columna == 7) tablero.append("        ");
                }
            }
            if (fila % 2 == 0) {
                tablero.append("  ").append(formatoCelda(numeroFila(fila / 2))).append("  ");
            } else {
                tablero.append("     ");
            }
            tablero.append("\n");
        }
        tablero.append(letras);
        tablero.append("\n\n");
        textArea.append(tablero.toString());
    }

    @Override
    public void mostrarTurno(String nombreJugador) {
        println("Turno de: " + nombreJugador);
    }

    @Override
    public void mostrarMensajeErrorCasilla() {
        println("Ha introducido una coordenada incorrecta. Vuelva a intentar.");
    }

    @Override
    public void avisoDeMolino(String nombreJugador) {
        println("¡El jugador " + nombreJugador + " hizo molino!");
    }

    public Object[] pedirCasilla() {
        boolean inputValido = false;
        int fila = -1;
        char columna = '\0';
        while (!inputValido) {
            String inputFila = JOptionPane.showInputDialog(null, "Ingrese la fila (1-7): ");
            String inputColumna = JOptionPane.showInputDialog(null, "Ingrese la columna (A-G): ");
            try {
                fila = Integer.parseInt(inputFila);
                if (fila >= 1 && fila <= 7) {
                    inputValido = true;
                } else {
                    JOptionPane.showMessageDialog(null, "La fila ingresada no está dentro del rango válido (1-7).");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingrese un número válido para la fila.");
            }
            if (inputColumna.length() == 1) {
                columna = Character.toUpperCase(inputColumna.charAt(0));
                if (columna >= 'A' && columna <= 'G') {
                    inputValido = true;
                } else {
                    JOptionPane.showMessageDialog(null, "La columna ingresada no está dentro del rango válido (A-G).");
                    inputValido = false;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese una única letra para la columna.");
                inputValido = false;
            }
        }
        return new Object[]{fila, columna};
    }

    @Override
    public void fichaAEliminar() {
        println("Introduzca la coordenada de la ficha a eliminar del oponente...");
    }

    @Override
    public void mostrarGanador(String nombreJugador) {
        println("\n\nEl ganador es: " + nombreJugador);
    }

    @Override
    public void juegoTerminado() {
        println("El juego ha terminado.");
    }

    @Override
    public void fichaSinMovimiento() {
        println("La ficha seleccionada NO tiene movimientos. Intente con otra.");
    }

    @Override
    public void casillaNoAdyacente() {
        println("La casilla seleccionada no corresponde a una casilla adyacente. Intente con otra.");
    }

    @Override
    public void mensajePedirNuevaCasillaLibreAdyacente() {
        println("Ingrese una coordenada libre adyacente para colocar la ficha...");
    }

    @Override
    public void mostrarEmpate(String nombreJ1, String nombreJ2) {
        println("¡Se ha producido un EMPATE entre " + nombreJ1 + " y " + nombreJ2 + "!");
    }

    @Override
    public void mostrarMensajeCasillaOcupada() {
        println("La casilla que ha seleccionado está ocupada. Intente con otra.");
    }

    @Override
    public void mensajeFichaFormaMolino() {
        println("La pieza seleccionada forma un molino. Intente con otra.");
    }

    @Override
    public void mensajePedirNuevaCasillaLibre() {
        println("Ingrese cualquier coordenada libre para colocar la ficha...");
    }

    @Override
    public void avisoNoHayFichasParaEliminarDelOponente() {
        println("No hay fichas disponibles para eliminar en este turno. Mala suerte :/");
    }

    @Override
    public void jugadorSinMovimientos() {
        println("¡Te quedaste sin movimientos posibles! :/\n");
    }

    @Override
    public void jugadorSinFichas() {
        println("¡Te quedaste sin fichas suficientes! :/\n");
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
    public void mensajeCasillaFichaAMover() {
        println("Ingrese la coordenada de la ficha que desea mover...");
    }

    public void actualizar() throws RemoteException {
        textArea.setText("");
        mostrarTablero(controlador.obtenerTablero());
    }

    private void println(String texto) {
        textArea.append(texto + "\n");
    }

    private void print(String texto) {
        textArea.append(texto);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
