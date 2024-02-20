package ar.edu.unlu.poo.vistas;

import ar.edu.unlu.poo.enumerados.EstadoJuego;
import ar.edu.unlu.poo.interfaces.IControlador;
import ar.edu.unlu.poo.interfaces.IVista;
import ar.edu.unlu.poo.mensajes.MensajesGanador;
import ar.edu.unlu.poo.mensajes.MensajesMolino;
import ar.edu.unlu.poo.mensajes.MensajesPerdedor;
import ar.edu.unlu.poo.modelos.Coordenada;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

public class VistaTableroConsola extends JFrame implements IVista {
    private final IControlador controlador;
    private JTextArea textAreaTablero;
    private JTextArea textAreaMensajes;
    private JPanel optionsPanel;
    private JPanel rightPanel;
    private Image icono;

    public VistaTableroConsola(IControlador controlador) {
        this.controlador = controlador;
        this.controlador.setVista(this);
        initComponents();
        setIconImage(icono);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(750, 350);
        setLocationRelativeTo(null);
        setResizable(false);

        //Eventos
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    if (controlador.laPartidaHaComenzado()) {
                        if (controlador.laPartidaSigueActiva()) {
                            int confirm = JOptionPane.showConfirmDialog(JOptionPane.getRootFrame(),
                                    "¿Quiere guardar la partida para reanudarla en otra ocación?\nNOTA: Si presiona 'NO' se le contará como abandono y perderás la partida :/",
                                    "Confirmar salida", JOptionPane.YES_NO_OPTION);
                            if (confirm == JOptionPane.YES_OPTION) {
                                controlador.guardarPartidaEstadoActual();
                                System.exit(0); // Termina la aplicación
                            }
                            if (confirm == JOptionPane.NO_OPTION) {
                                // Si el usuario confirma, cierra la aplicación.
                                controlador.jugadorAbandona();
                                System.exit(0); // Termina la aplicación
                            }
                        } else {
                            System.exit(0); // Termina la aplicación
                        }
                    } else {
                        int confirm = JOptionPane.showConfirmDialog(JOptionPane.getRootFrame(),
                                "¿Estás seguro de que quieres cerrar la aplicación? El juego todavía no ha comenzado.",
                                "Confirmar salida", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            // Si el usuario confirma, cierra la aplicación.
                            controlador.cerrarAplicacion();
                            System.exit(0); // Termina la aplicación
                        }
                    }
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        setLayout(new BorderLayout());
        textAreaTablero = new JTextArea(28, 28);
        textAreaTablero.setEditable(false);
        add(textAreaTablero, BorderLayout.WEST);
        rightPanel = new JPanel(new BorderLayout());
        textAreaMensajes = new JTextArea(10, 35);
        textAreaMensajes.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textAreaMensajes);
        rightPanel.add(scrollPane, BorderLayout.NORTH);
        add(rightPanel, BorderLayout.EAST);
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        JPanel textFieldsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel filaLabel = new JLabel("Fila:");
        JTextField fila = new JTextField(5);
        fila.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (str == null) {
                    return;
                }
                // Verifica si el texto ingresado es numérico y limita la longitud a 1.
                if (str.matches("\\d") && (getLength() + str.length()) <= 1) {
                    super.insertString(offs, str, a);
                }
            }
        });
        JLabel columnaLabel = new JLabel("Columna:");
        JTextField columna = new JTextField(5);
        PlainDocument doc = (PlainDocument) columna.getDocument();
        doc.setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                // Verifica si la inserción excedería el límite de un carácter
                if ((fb.getDocument().getLength() + text.length() - length) <= 1) {
                    super.replace(fb, offset, length, text.toUpperCase(), attrs); // Permitir inserción
                } else {
                    Toolkit.getDefaultToolkit().beep(); // Emite un sonido de error si se intenta exceder el límite
                }
            }
        });
        textFieldsPanel.add(columnaLabel);
        textFieldsPanel.add(columna);
        textFieldsPanel.add(filaLabel);
        textFieldsPanel.add(fila);
        JButton botonEnviarMovimiento = new JButton("Enviar movimiento...");
        botonEnviarMovimiento.addActionListener(e -> {
            try {
                controlador.casillaSeleccionadaDesdeLaVista(generarCoordenada(fila.getText(), columna.getText()));
                // Reiniciamos el contenido.
                fila.setText("");
                columna.setText("");
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        });
        optionsPanel.add(textFieldsPanel);
        optionsPanel.add(botonEnviarMovimiento);
        rightPanel.add(optionsPanel, BorderLayout.SOUTH);
        setVisible(false);
    }

    private void initComponents() {
        icono = new ImageIcon(getClass().getResource("/ar/edu/unlu/poo/images/LogoMolino.jpg")).getImage();
        Image originalImage = icono;
        Image scaledImage = originalImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        icono = new ImageIcon(scaledImage).getImage();
    }

    @Override
    public void iniciar() {
        new MenuPrePartida(controlador);
    }

    @Override
    public void mostrarTablero() throws RemoteException {
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
                String contenidoCelda = "";
                if (fila % 2 == 0 && columna % 2 == 0) {
                    contenidoCelda = controlador.contenidoCasilla(new Coordenada(fila / 2, columna / 2));
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
        textAreaTablero.append(tablero.toString());
    }

    @Override
    public void mostrarMensajeErrorCasilla() {
        println(textAreaMensajes, "Ha introducido una coordenada incorrecta. Vuelva a intentar.");
    }

    @Override
    public void avisoDeMolino(String nombreJugador) {
        println(textAreaMensajes, "¡El jugador " + nombreJugador + " hizo molino!");
    }

    @Override
    public void mostrarGanador(String nombreJugador) {
        println(textAreaMensajes, "El ganador es: " + nombreJugador);
    }

    @Override
    public void juegoTerminado() {
        println(textAreaMensajes, "\n--------------------------------------------------------\n-------------El juego ha terminado-------------\n--------------------------------------------------------\n");
    }

    @Override
    public void mostrarMensajeFichaSinMovimiento() {
        println(textAreaMensajes, "La ficha seleccionada NO tiene movimientos. Intente con otra.");
    }

    @Override
    public void casillaNoAdyacente() {
        println(textAreaMensajes, "La casilla seleccionada no corresponde a una casilla adyacente. Intente con otra.");
    }

    @Override
    public void mostrarEmpate() {
        println(textAreaMensajes, "\n¡Se ha producido un EMPATE!\n");
    }

    @Override
    public void mostrarMensajeCasillaOcupada() {
        println(textAreaMensajes, "La casilla que ha seleccionado está ocupada. Intente con otra.");
    }

    @Override
    public void mensajeFichaFormaMolino() {
        println(textAreaMensajes, "La ficha seleccionada forma parte de un molino. Intente con otra.");
    }

    @Override
    public void avisoNoHayFichasParaEliminarDelOponente() {
        println(textAreaMensajes, "No hay fichas disponibles para eliminar en este turno. Mala suerte :/");
    }

    @Override
    public void jugadorSinMovimientos() {
        println(textAreaMensajes, "¡Te quedaste sin movimientos posibles! :/\n");
    }

    @Override
    public void jugadorSinFichas() {
        println(textAreaMensajes, "¡Te quedaste sin fichas suficientes! :/\n");
    }

    @Override
    public void mostrarJugadorConectado() throws RemoteException {
        setTitle("Juego del Molino - Nueve hombres de Morris   (" + controlador.nombreJugador() + ")");
        setVisible(true);
        textAreaMensajes.setText("Te has conectado.\nEsperando a que se una tu oponente...");
    }

    @Override
    public void mostrarTurnoActual() {
        println(textAreaMensajes, "¡Es turno de tu oponente! Espera a que realice su movimiento.");
    }

    @Override
    public void mostrarMensajeError(String mensaje) {
        println(textAreaMensajes, "Error: " + mensaje);
    }

    public void actualizarTablero() throws RemoteException {
        textAreaTablero.setText("");
        mostrarTablero();
    }

    @Override
    public void mensajeAlGanador() {
        println(textAreaMensajes, MensajesGanador.getMensajeFelicitacion());
    }

    @Override
    public void mensajeAlPerdedor() {
        println(textAreaMensajes, MensajesPerdedor.getMensajeDeAnimo());
    }

    @Override
    public void actualizarParaAccion(EstadoJuego estadoActual) {
        switch (estadoActual) {
            case ESPERANDO_TURNO -> {
                limpiarMensajes();
                println(textAreaMensajes, "Ahora es el turno de tu oponente. Espera hasta que realice su movimiento ;)");
            }
            case COLOCAR_FICHA, SELECCIONAR_DESTINO_MOVER_SUPER -> {
                limpiarMensajes();
                mensajePedirNuevaCasillaLibre();
            }
            case SELECCIONAR_ORIGEN_MOVER, SELECCIONAR_ORIGEN_MOVER_SUPER -> {
                limpiarMensajes();
                mensajeCasillaFichaAMover();
            }
            case SELECCIONAR_DESTINO_MOVER -> {
                limpiarMensajes();
                mensajePedirNuevaCasillaLibreAdyacente();
            }
            case SELECCIONAR_FICHA_PARA_ELIMINAR -> {
                mensajeFichaAEliminar();
            }
            case PARTIDA_SUSPENDIDA -> {
                limpiarMensajes();
                println(textAreaMensajes, "******** ¡La partida se ha suspendido! ********\n\n");
                println(textAreaMensajes, "Tu oponente ha salido de la partida, pero ha guardado el estado del\njuego en el servidor. En caso de querer retomar la partida,\nno olvides que jugador tenía asignado cada uno. Así no\npierden el progreso. Ya puedes cerrar el juego.");
                optionsPanel.setVisible(false);
                JButton botonSalir = new JButton();
                botonSalir.setText("Cerrar aplicación...");
                botonSalir.setVisible(true);
                botonSalir.addActionListener(e -> {
                    System.exit(0);
                });
                rightPanel.add(botonSalir, BorderLayout.SOUTH);
            }
            case PARTIDA_TERMINADA -> {
                limpiarMensajes();
                juegoTerminado();
                optionsPanel.setVisible(false);
                JButton botonSalir = new JButton();
                botonSalir.setText("Cerrar aplicación...");
                botonSalir.setVisible(true);
                botonSalir.addActionListener(e -> {
                    System.exit(0);
                });
                rightPanel.add(botonSalir, BorderLayout.SOUTH);
            }
        }
    }

    @Override
    public void mostrarMensajeNoCorrespondeAlJugador() {
        println(textAreaMensajes, "La ficha seleccionada pertenece a tu oponente. Intenta con otra.");
    }

    @Override
    public void mostrarMensajeNoCorrespondeAlOponente() {
        println(textAreaMensajes, "¡No podés eliminar tus propias fichas! Seleccioná una ficha de tu oponente.");
    }

    @Override
    public void avisoJugadorHizoMolino() {
        println(textAreaMensajes, MensajesMolino.getMensajeFelicitacion());
    }

    @Override
    public void mostrarMensajeCasillaLibre() {
        println(textAreaMensajes, "No hay ficha en la coordenada que ingresaste.\nIngresa una casilla ocupada por tu oponente.");
    }

    @Override
    public void empatePorMovimientosSinComerFichas() {
        println(textAreaMensajes, "¡Han realizado más de 30 movimientos sin comer una ficha! Entonces se declara empate.");
    }

    @Override
    public void informarOponenteHaAbandonado() {
        println(textAreaMensajes, "¡Tu oponente ha abandonado! Se te otorga la victoria. ¡Felicitaciones!");
    }

    private void limpiarMensajes() {
        textAreaMensajes.setText("");
    }

    private void mensajeCasillaFichaAMover() {
        println(textAreaMensajes, "Ingresa la coordenada de la ficha que deseas mover.");
    }

    private void mensajeFichaAEliminar() {
        println(textAreaMensajes, "Ingresa la coordenada de la ficha a eliminar de tu oponente.");
    }

    private void mensajePedirNuevaCasillaLibreAdyacente() {
        println(textAreaMensajes, "Ingresa una coordenada libre adyacente para colocar la ficha.");
    }

    private void mensajePedirNuevaCasillaLibre() {
        println(textAreaMensajes, "Ingresa cualquier coordenada libre para colocar una ficha.");
    }

    private Coordenada generarCoordenada(String fila, String columna) {
        int filaResultado;
        if (fila.isEmpty() || fila.isBlank()) {
            filaResultado = -1;
        } else {
            filaResultado = Integer.parseInt(fila);
            switch (filaResultado) {
                case 1 -> filaResultado = 0;
                case 2 -> filaResultado = 1;
                case 3 -> filaResultado = 2;
                case 4 -> filaResultado = 3;
                case 5 -> filaResultado = 4;
                case 6 -> filaResultado = 5;
                case 7 -> filaResultado = 6;
                default -> filaResultado = -1;
            }
        }
        int columnaResultado;
        if (columna.isEmpty() || columna.isBlank()) {
            columnaResultado = -1;
        } else {
            columna = columna.toUpperCase();
            switch (columna) {
                case "A" -> columnaResultado = 0;
                case "B" -> columnaResultado = 1;
                case "C" -> columnaResultado = 2;
                case "D" -> columnaResultado = 3;
                case "E" -> columnaResultado = 4;
                case "F" -> columnaResultado = 5;
                case "G" -> columnaResultado = 6;
                default -> columnaResultado = -1;
            }
        }
        return new Coordenada(filaResultado, columnaResultado);
    }

    private String numeroFila(int fila) {
        return String.valueOf(fila + 1);
    }

    private String formatoCelda(String contenido) {
        return String.format("%1s", contenido);
    }

    private void println(JTextArea textArea, String texto) {
        textArea.append(texto + "\n");
    }

    private void print(JTextArea textArea, String texto) {
        textArea.append(texto);
    }
}
