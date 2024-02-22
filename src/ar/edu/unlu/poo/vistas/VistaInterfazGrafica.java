package ar.edu.unlu.poo.vistas;

import ar.edu.unlu.poo.controladores.Controlador;
import ar.edu.unlu.poo.enumerados.EstadoJuego;
import ar.edu.unlu.poo.interfaces.IVista;
import ar.edu.unlu.poo.mensajes.MensajesGanador;
import ar.edu.unlu.poo.mensajes.MensajesMolino;
import ar.edu.unlu.poo.mensajes.MensajesPerdedor;
import ar.edu.unlu.poo.modelos.Coordenada;
import ar.edu.unlu.poo.modelos.Molino;
import ar.edu.unlu.poo.vistas.elementosParaInterfazGrafica.BoardPanel;
import ar.edu.unlu.poo.vistas.elementosParaInterfazGrafica.CircularButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class VistaInterfazGrafica extends JFrame implements IVista {
    private final Controlador controlador;
    private ImageIcon fichaBlanca;
    private ImageIcon fichaNegra;
    private ImageIcon fondoCasilla;
    private Image icono;
    private JPanel rightPanel;
    private JTextArea textArea;
    private JPanel panelBotonesFin;
    private final Map<ButtonCoordinates, JButton> botonesPorCoordenada = new HashMap<>();

    public VistaInterfazGrafica(Controlador controlador) {
        this.controlador = controlador;
        this.controlador.setVista(this);

        initElements();
        setLocationRelativeTo(null);
        setSize(900, 540);
        setLayout(new BorderLayout());

        setIconImage(icono);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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
                    ex.printStackTrace();
                    System.exit(0); // Termina la aplicación
                }
            }
        });
        setVisible(false);
    }

    private void initElements() {
        // Ajustar el tamaño de la imagen de la ficha
        icono = new ImageIcon(getClass().getResource("/ar/edu/unlu/poo/images/LogoMolino.jpg")).getImage();
        Image originalImage = icono;
        Image scaledImage = originalImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        icono = new ImageIcon(scaledImage).getImage();
        fichaNegra = new ImageIcon(getClass().getResource("/ar/edu/unlu/poo/images/FichaNegra.png"));
        originalImage = fichaNegra.getImage();
        scaledImage = originalImage.getScaledInstance(53, 53, Image.SCALE_SMOOTH);
        fichaNegra = new ImageIcon(scaledImage);
        fichaBlanca = new ImageIcon(getClass().getResource("/ar/edu/unlu/poo/images/FichaBlanca.png"));
        originalImage = fichaBlanca.getImage();
        scaledImage = originalImage.getScaledInstance(53, 53, Image.SCALE_SMOOTH);
        fichaBlanca = new ImageIcon(scaledImage);
        fondoCasilla = new ImageIcon(getClass().getResource("/ar/edu/unlu/poo/images/FondoCasillaClaro.png"));
        originalImage = fondoCasilla.getImage();
        scaledImage = originalImage.getScaledInstance(46, 46, Image.SCALE_SMOOTH);
        fondoCasilla = new ImageIcon(scaledImage);
    }

    /**
     * Se crea un botón
     *
     * @param panel panel contenedor
     * @param col   CORDENADA COL - TABLERO
     * @param fil   CORDENADA FIL - TABLERO
     * @param xPos  pos X del panel
     * @param yPos  pos Y del panel
     */
    private void crearBoton(JPanel panel, int col, int fil, int xPos, int yPos) {
        CircularButton boton = new CircularButton();
        ButtonCoordinates coords = new ButtonCoordinates(col, fil);
        boton.addActionListener(e -> {
            try {
                controlador.casillaSeleccionadaDesdeLaVista(new Coordenada(coords.x, coords.y));
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        });
        boton.setBounds(xPos, yPos, 50, 50);
        botonesPorCoordenada.put(coords, boton);
        panel.add(boton);
    }

    @Override
    public void iniciar() {
        new MenuPrePartida(controlador);
    }

    @Override
    public void mostrarTablero() throws RemoteException {
        for (Map.Entry<ButtonCoordinates, JButton> entrada : botonesPorCoordenada.entrySet()) {
            ButtonCoordinates coords = entrada.getKey();
            JButton button = entrada.getValue();
            String contenidoCasilla = controlador.contenidoCasilla(new Coordenada(coords.x, coords.y));
            if (contenidoCasilla.equals(Molino.JUGADOR_1)) {
                button.setIcon(fichaBlanca);
                button.setBorderPainted(false);
            } else if (contenidoCasilla.equals(Molino.JUGADOR_2)) {
                button.setIcon(fichaNegra);
                button.setBorderPainted(false);
            } else {
                button.setIcon(fondoCasilla);
            }
        }
    }

    @Override
    public void mostrarMensajeErrorCasilla() {
        println("Has seleccionado una casilla incorrecta. Volvé a intentar.");
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

    private void cambiarEstadoBotones(boolean estado) {
        for (Map.Entry<ButtonCoordinates, JButton> entrada : botonesPorCoordenada.entrySet()) {
            JButton button = entrada.getValue();
            button.setEnabled(estado);
        }
    }

    @Override
    public void mostrarMensajeFichaSinMovimiento() {
        println("La ficha seleccionada NO tiene movimientos. Intente con otra.");
    }

    @Override
    public void casillaNoAdyacente() {
        println("La casilla seleccionada no corresponde a una\ncasilla adyacente. Intente con otra.");
    }

    @Override
    public void mostrarEmpate() {
        println("¡Se ha producido un EMPATE!");
    }

    @Override
    public void mostrarMensajeCasillaOcupada() {
        println("La casilla que seleccionaste está ocupada.\nIntentá con otra.");
    }

    @Override
    public void mensajeFichaFormaMolino() {
        println("La ficha seleccionada forma un molino.\nIntente con otra.");
    }

    @Override
    public void avisoNoHayFichasParaEliminarDelOponente() {
        println("No hay fichas disponibles para eliminar\nen este turno. Mala suerte :/");
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
    public void mostrarJugadorConectado() throws RemoteException {
        setTitle("Juego del Molino - Nueve hombres de Morris   (" + controlador.nombreJugador() + ")");
        // Tablero
        BoardPanel boardPanel = new BoardPanel();
        crearBoton(boardPanel, 0, 0, 29, 29);
        crearBoton(boardPanel, 0, 3, 226, 29);
        crearBoton(boardPanel, 0, 6, 425, 29);
        crearBoton(boardPanel, 1, 1, 95, 95);
        crearBoton(boardPanel, 1, 3, 226, 95);
        crearBoton(boardPanel, 1, 5, 358, 95);
        crearBoton(boardPanel, 2, 2, 157, 157);
        crearBoton(boardPanel, 2, 3, 226, 157);
        crearBoton(boardPanel, 2, 4, 296, 157);
        crearBoton(boardPanel, 3, 0, 29, 226);
        crearBoton(boardPanel, 3, 1, 95, 226);
        crearBoton(boardPanel, 3, 2, 157, 226);
        crearBoton(boardPanel, 3, 4, 296, 226);
        crearBoton(boardPanel, 3, 5, 358, 226);
        crearBoton(boardPanel, 3, 6, 425, 226);
        crearBoton(boardPanel, 4, 2, 157, 298);
        crearBoton(boardPanel, 4, 3, 226, 298);
        crearBoton(boardPanel, 4, 4, 296, 298);
        crearBoton(boardPanel, 5, 1, 95, 360);
        crearBoton(boardPanel, 5, 3, 226, 360);
        crearBoton(boardPanel, 5, 5, 358, 360);
        crearBoton(boardPanel, 6, 0, 29, 425);
        crearBoton(boardPanel, 6, 3, 226, 425);
        crearBoton(boardPanel, 6, 6, 425, 425);
        add(boardPanel, BorderLayout.WEST);

        rightPanel = new JPanel(new BorderLayout());
        // Texto
        textArea = new JTextArea(10, 30); // Ajusta los valores para que se ajuste a tus necesidades
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea); // Añade el textArea a un JScrollPane
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        panelBotonesFin = new JPanel(new BorderLayout());

        JButton bSalir = new JButton("Cerrar aplicación...");
        setVisible(true);
        bSalir.addActionListener(e -> {
            System.exit(0);
        });
        JButton bVolver = new JButton("Volver al menú principal...");
        bVolver.setVisible(true);
        bVolver.addActionListener(e -> {
            dispose();
            new MenuMolino();
        });

        panelBotonesFin.add(bSalir, BorderLayout.EAST);
        panelBotonesFin.add(bVolver, BorderLayout.WEST);
        rightPanel.add(panelBotonesFin, BorderLayout.SOUTH);
        panelBotonesFin.setVisible(false);
        add(rightPanel, BorderLayout.EAST);
        setVisible(true);
        textArea.setText("Te has conectado.\nEsperando a que se conecte tu oponente...");
    }

    @Override
    public void mostrarTurnoDelOponente() {
        println("¡Es turno de tu oponente!\nEspera a que realice su movimiento ;)");
    }

    @Override
    public void mostrarMensajeError(String mensaje) {
        println("Error: " + mensaje);
    }

    public void actualizarTablero() throws RemoteException {
        mostrarTablero();
    }

    @Override
    public void mensajeAlGanador() {
        println(MensajesGanador.getMensajeFelicitacion());
    }

    @Override
    public void mensajeAlPerdedor() {
        println(MensajesPerdedor.getMensajeDeAnimo());
    }

    @Override
    public void actualizarParaAccion(EstadoJuego estadoActual) {
        switch (estadoActual) {
            case ESPERANDO_TURNO -> {
                limpiarMensajes();
                println("Ahora es el turno de tu oponente.\nEspera hasta que realice su movimiento ;)");
            }
            case COLOCAR_FICHA, SELECCIONAR_DESTINO_MOVER_SUPER -> {
                limpiarMensajes();
                mensajeEsTuTurno();
                mensajePedirNuevaCasillaLibre();
            }
            case SELECCIONAR_ORIGEN_MOVER, SELECCIONAR_ORIGEN_MOVER_SUPER -> {
                limpiarMensajes();
                mensajeEsTuTurno();
                mensajeCasillaFichaAMover();
            }
            case SELECCIONAR_DESTINO_MOVER -> {
                limpiarMensajes();
                mensajeEsTuTurno();
                mensajePedirNuevaCasillaLibreAdyacente();
            }
            case SELECCIONAR_FICHA_PARA_ELIMINAR -> {
                limpiarMensajes();
                mensajeEsTuTurno();
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

                limpiarMensajes();
                println("******** ¡La partida se ha suspendido! ********\n\n");
                println("Tu oponente ha salido de la partida, pero ha guardado el estado del\njuego en el servidor. En caso de querer reanudar la partida,\nno olvides que jugador tenía asignado cada uno. Así no\npierden el progreso. Ya puedes cerrar el juego.");
                cambiarEstadoBotones(false);
                panelBotonesFin.setVisible(true);
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

                limpiarMensajes();
                juegoTerminado();
                cambiarEstadoBotones(false);
                panelBotonesFin.setVisible(true);
            }
        }
    }

    @Override
    public void mostrarMensajeNoCorrespondeAlJugador() {
        println("La ficha seleccionada pertenece a tu oponente\n. Intenta con otra.");
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
        println("No hay ninguna ficha en la casilla que seleccionaste.\nSelecciona una casilla ocupada por tu oponente.");
    }

    @Override
    public void empatePorMovimientosSinComerFichas() {
        println("¡Han realizado más de 30 movimientos sin comer\nuna ficha! Entonces se declara empate.");
    }

    @Override
    public void informarOponenteHaAbandonado() {
        println("¡Tu oponente ha abandonado!\nSe te otorga la victoria. ¡Felicitaciones!");
    }

    @Override
    public void mensajeEsTuTurno() {
        println("¡Es tu turno! Piensa tu próximo movimiento.");
    }

    private void limpiarMensajes() {
        textArea.setText("");
    }

    private void mensajeCasillaFichaAMover() {
        println("Selecciona la ficha que desees mover.");
    }

    private void mensajeFichaAEliminar() {
        println("Selecciona la ficha que quieras eliminar de tu oponente.");
    }

    private void mensajePedirNuevaCasillaLibreAdyacente() {
        println("Selecciona una casilla libre adyacente para mover tu ficha.");
    }

    private void mensajePedirNuevaCasillaLibre() {
        println("Selecciona cualquier casilla libre para colocar una ficha.");
    }

    private void println(String texto) {
        textArea.append("\n" + texto + "\n");
    }

    private void print(String texto) {
        textArea.append(texto);
    }

    private record ButtonCoordinates(int x, int y) {
        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
}