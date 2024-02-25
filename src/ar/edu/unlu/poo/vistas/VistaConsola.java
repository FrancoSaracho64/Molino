package ar.edu.unlu.poo.vistas;

import ar.edu.unlu.poo.enumerados.EstadoJuego;
import ar.edu.unlu.poo.interfaces.IControlador;
import ar.edu.unlu.poo.interfaces.IVista;
import ar.edu.unlu.poo.mensajes.MensajesGanador;
import ar.edu.unlu.poo.mensajes.MensajesMolino;
import ar.edu.unlu.poo.mensajes.MensajesPerdedor;
import ar.edu.unlu.poo.modelos.Coordenada;
import ar.edu.unlu.poo.modelos.Molino;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.rmi.RemoteException;

public class VistaConsola extends JFrame implements IVista {
    private final IControlador controlador;
    private JTextArea textArea;
    private JPanel optionsPanel;
    private JPanel bottomPanel;
    private Image icono;

    public VistaConsola(IControlador controlador) {
        this.controlador = controlador;
        this.controlador.colocarVista(this);
        initComponents();
        setIconImage(icono);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(700, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 15));
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
        bottomPanel = new JPanel(new BorderLayout());
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

        bottomPanel.add(optionsPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        //Eventos
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    if (controlador.partidaHaComenzado()) {
                        if (controlador.partidaSigueActiva()) {
                            int confirm = JOptionPane.showConfirmDialog(JOptionPane.getRootFrame(),
                                    "¿Quiere guardar la partida para reanudarla en otra ocación?\nNOTA: Si presiona 'NO' se le contará como abandono y perderás la partida :/",
                                    "Confirmar salida", JOptionPane.YES_NO_OPTION);
                            if (confirm == JOptionPane.YES_OPTION) {
                                controlador.guardarPartida();
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
                            controlador.aplicacionCerrada();
                            System.exit(0); // Termina la aplicación
                        }
                    }
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                    System.exit(0); // Termina la aplicación
                }
            }
        });
        setVisible(false);
    }

    private void initComponents() {
        icono = new ImageIcon(getClass().getResource("/ar/edu/unlu/poo/images/LogoMolino.jpg")).getImage();
        Image originalImage = icono;
        Image scaledImage = originalImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        icono = new ImageIcon(scaledImage).getImage();
    }

    @Override
    public void iniciarVista() {
        new MenuPrePartida(controlador);
    }

    @Override
    public void mostrarTablero() throws RemoteException {
        StringBuilder tablero = new StringBuilder();
        String letras = "                 A     B     C     D     E     F     G\n";
        tablero.append("\n\n\n");
        tablero.append(letras);
        for (int fila = 0; fila < 13; fila++) {
            tablero.append("              ");
            if (fila % 2 == 0) {
                tablero.append(formatoCelda(numeroFila(fila / 2))).append(" "); // Número de fila
            } else {
                tablero.append("  ");
            }
            for (int columna = 0; columna < 13; columna++) {
                String contenidoCelda = "";
                if (fila % 2 == 0 && columna % 2 == 0) {
                    String contenido = controlador.obtenerContenidoCasilla(new Coordenada(fila / 2, columna / 2));
                    contenidoCelda = switch (contenido) {
                        case Molino.JUGADOR_1 -> "X";
                        case Molino.JUGADOR_2 -> "O";
                        case Molino.CASILLA_DISPONIBLE -> "#";
                        default -> contenidoCelda;
                    };
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
                        contenidoCelda = "|";
                }
                tablero.append(" ").append(formatoCelda(contenidoCelda)).append(" ");
            }
            if (fila % 2 == 0) {
                tablero.append(" ").append(formatoCelda(numeroFila(fila / 2)));
            }
            tablero.append("\n");
        }
        tablero.append(letras);
        tablero.append("\n\n");
        textArea.append(tablero.toString());
    }

    @Override
    public void mostrarMensajeErrorCasilla() {
        println("Ha introducido una coordenada incorrecta.\nVuelva a intentar.");
    }

    @Override
    public void avisoDeMolino(String nombreJugador) {
        println("****** ¡El jugador " + nombreJugador + " hizo molino! ******");
    }

    @Override
    public void mostrarGanador(String nombreJugador) {
        println("El ganador es: " + nombreJugador);
    }

    @Override
    public void juegoTerminado() {
        println("\n--------------------------------------------------------\n-------------El juego ha terminado-------------\n--------------------------------------------------------\n");
    }

    @Override
    public void mostrarMensajeFichaSinMovimiento() {
        println("La ficha seleccionada NO tiene movimientos.\nIntente con otra.");
    }

    @Override
    public void avisoCasillaNoAdyacente() {
        println("La casilla seleccionada no corresponde a una casilla adyacente.\nIntente con otra.");
    }

    @Override
    public void mostrarEmpate() {
        println("\n¡Se ha producido un EMPATE!\n");
    }

    @Override
    public void mostrarMensajeCasillaOcupada() {
        println("La casilla que ha seleccionado está ocupada.\nIntente con otra.");
    }

    @Override
    public void mostrarMensajeFichaFormaMolino() {
        println("La ficha seleccionada forma parte de un molino.\nIntente con otra.");
    }

    @Override
    public void avisoNoHayFichasParaEliminarDelOponente() {
        println("No hay fichas disponibles para eliminar\nen este turno. Mala suerte :/");
    }

    @Override
    public void avisoJugadorSinMovimientos() {
        println("¡Te quedaste sin movimientos posibles! :/\n");
    }

    @Override
    public void avisoJugadorSinFichas() {
        println("¡Te quedaste sin fichas suficientes! :/\n");
    }

    @Override
    public void mostrarJugadorConectado() throws RemoteException {
        setTitle("Juego del Molino - Nueve hombres de Morris   (" + controlador.obtenerNombreJugador() + ")");
        setVisible(true);
        println("Te has conectado.\nEsperando a que se una tu oponente...");
    }

    @Override
    public void mostrarTurnoDelOponente() {
        println("¡Es turno de tu oponente!\nEspera a que realice su movimiento.");
    }

    public void actualizarTablero() throws RemoteException {
        mostrarTablero();
    }

    @Override
    public void mostrarMensajeAlGanador() {
        println(MensajesGanador.getMensajeFelicitacion());
    }

    @Override
    public void mostrarMensajeAlPerdedor() {
        println(MensajesPerdedor.getMensajeDeAnimo());
    }

    @Override
    public void actualizarVistaParaAccion(EstadoJuego estadoActual) {
        switch (estadoActual) {
            case ESPERANDO_TURNO -> println("Ahora es el turno de tu oponente.\nEspera hasta que realice su movimiento ;)");
            case COLOCAR_FICHA, SELECCIONAR_DESTINO_MOVER_SUPER -> {
                mostrarMensajeEsTuTurno();
                mensajePedirNuevaCasillaLibre();
            }
            case SELECCIONAR_ORIGEN_MOVER, SELECCIONAR_ORIGEN_MOVER_SUPER -> {
                mostrarMensajeEsTuTurno();
                mensajeCasillaFichaAMover();
            }
            case SELECCIONAR_DESTINO_MOVER -> {
                mostrarMensajeEsTuTurno();
                mensajePedirNuevaCasillaLibreAdyacente();
            }
            case SELECCIONAR_FICHA_PARA_ELIMINAR -> {
                mostrarMensajeEsTuTurno();
                mensajeFichaAEliminar();
            }
            case PARTIDA_SUSPENDIDA -> {
                WindowListener[] listeners = getWindowListeners();
                for (WindowListener listener : listeners) {
                    removeWindowListener(listener);
                }
                addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                });

                println("******** ¡La partida se ha suspendido! ********\n\n");
                println("Tu oponente ha salido de la partida, pero ha guardado el estado del\njuego en el servidor. En caso de querer retomar la partida,\nno olvides que jugador tenía asignado cada uno. Así no\npierden el progreso. Ya puedes cerrar el juego.");
                optionsPanel.setVisible(false);

                JPanel panelBotonesFin = new JPanel(new BorderLayout());

                JButton botonSalir = new JButton();
                botonSalir.setText("Cerrar aplicación...");
                botonSalir.setVisible(true);
                botonSalir.addActionListener(e -> {
                    System.exit(0);
                });
                JButton botonReanudar = new JButton();
                botonReanudar.setText("Volver al menú principal...");
                botonReanudar.setVisible(true);
                botonReanudar.addActionListener(e -> {
                    dispose();
                    new MenuMolino();
                });
                panelBotonesFin.add(botonSalir, BorderLayout.WEST);
                panelBotonesFin.add(botonReanudar, BorderLayout.EAST);
                bottomPanel.add(panelBotonesFin, BorderLayout.SOUTH);
            }
            case PARTIDA_TERMINADA -> {
                WindowListener[] listeners = getWindowListeners();
                for (WindowListener listener : listeners) {
                    removeWindowListener(listener);
                }
                addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                });

                juegoTerminado();
                optionsPanel.setVisible(false);
                JPanel panelBotonesFin = new JPanel(new BorderLayout());

                JButton bSalir = new JButton("Cerrar aplicación...");
                bSalir.setVisible(true);
                bSalir.addActionListener(e -> {
                    System.exit(0);
                });
                JButton bVolver = new JButton("Volver al menú principal...");
                bVolver.setVisible(true);
                bVolver.addActionListener(e -> {
                    dispose();
                    new MenuMolino();
                });
                panelBotonesFin.add(bSalir, BorderLayout.WEST);
                panelBotonesFin.add(bVolver, BorderLayout.EAST);
                bottomPanel.add(panelBotonesFin, BorderLayout.SOUTH);
            }
        }
    }

    @Override
    public void mostrarMensajeNoCorrespondeAlJugador() {
        println("La ficha seleccionada pertenece a tu oponente.\nIntenta con otra.");
    }

    @Override
    public void mostrarMensajeNoCorrespondeAlOponente() {
        println("¡No podés eliminar tus propias fichas!\nSeleccioná una ficha de tu oponente.");
    }

    @Override
    public void avisoJugadorHizoMolino() {
        println(MensajesMolino.getMensajeFelicitacion());
    }

    @Override
    public void mostrarMensajeCasillaLibre() {
        println("No hay ficha en la coordenada que ingresaste.\nIngresa una casilla ocupada por tu oponente.");
    }

    @Override
    public void avisoEmpatePorMovimientosSinComerFichas() {
        println("¡Han realizado más de 30 movimientos sin comer una ficha!\nEntonces se declara empate.");
    }

    @Override
    public void informarOponenteHaAbandonado() {
        println("¡Tu oponente ha abandonado!\nSe te otorga la victoria. ¡Felicitaciones!");
    }

    @Override
    public void mostrarMensajeEsTuTurno() {
        println("¡Es tu turno! Piensa tu próximo movimiento.");
    }

    private void mensajeCasillaFichaAMover() {
        println("Ingresa la coordenada de la ficha que deseas mover.");
    }

    private void mensajeFichaAEliminar() {
        println("Ingresa la coordenada de la ficha a eliminar de tu oponente.");
    }

    private void mensajePedirNuevaCasillaLibreAdyacente() {
        println("Ingresa una coordenada libre adyacente para colocar la ficha.");
    }

    private void mensajePedirNuevaCasillaLibre() {
        println("Ingresa cualquier coordenada libre para colocar una ficha.");
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

    private void println(String texto) {
        textArea.append("\n" + texto + "\n");
    }

    private void print(JTextArea textArea, String texto) {
        textArea.append(texto);
    }
}
