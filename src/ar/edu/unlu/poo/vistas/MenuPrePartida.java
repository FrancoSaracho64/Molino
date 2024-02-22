package ar.edu.unlu.poo.vistas;

import ar.edu.unlu.poo.interfaces.IControlador;
import ar.edu.unlu.poo.modelos.Jugador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

public class MenuPrePartida extends JFrame {
    private JPanel panel;
    private JTextArea textArea;
    private JButton bSeleccionarJugadorPorPos;
    private JButton iniciarPartidaButton;
    private JButton bCrearNuevoJugador;
    private JButton bSeleccionarExistente;
    private Jugador jugadorLocal;
    private final IControlador controlador;
    private Image icono;

    public MenuPrePartida(IControlador controlador) {
        this.controlador = controlador;
        initComponents();
        setIconImage(icono);
        setContentPane(panel);
        setTitle("Juego del Molino - Menú principal");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(800, 500);
        textArea.setSize(10, 50);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    int confirm = JOptionPane.showConfirmDialog(JOptionPane.getRootFrame(),
                            "¿Estás seguro de que quieres cerrar la aplicación? El juego todavía no ha comenzado.",
                            "Confirmar salida", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (controlador != null) {
                            controlador.aplicacionCerrada();
                        }
                        System.exit(0); // Termina la aplicación
                    }
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        iniciarPartidaButton.addActionListener(e -> {
            try {
                controlador.agregarJugador(jugadorLocal);
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
            dispose();
        });

        bCrearNuevoJugador.addActionListener(e -> {
            println("");
            println("¡Generando Jugador nuevo!");
            // Verificamos que ese nombre no exista
            boolean ocupado;
            String nombre = JOptionPane.showInputDialog(null, "Ingrese nombre del nuevo jugador: ");
            if (nombre.isBlank() || nombre.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No se puede introducir un nombre vacío. Vuelva a intentar.", "Error con el nombre", JOptionPane.ERROR_MESSAGE);
                println("*** Error creando jugador ***   >>>   Se introdujo un nombre vacío/en blanco-");
            } else {
                try {
                    ocupado = controlador.esNombreYaRegistrado(nombre);
                    if (ocupado) {
                        JOptionPane.showMessageDialog(null, "Ya existe un jugador con este mismo nombre. Intente con seleccionarlo desde la lista o cree un jugador nuevo con otro nombre.", "Error con el nombre", JOptionPane.ERROR_MESSAGE);
                        println("*** Error creando jugador ***   >>>   El nombre ingresado ya se encuentra registrado. Intente con otro.");
                        return;
                    }
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                println(">>> ¡Hecho! Jugador creado con exito. ¡Bienvenido " + nombre + "!");
                jugadorLocal = new Jugador(nombre);
                bCrearNuevoJugador.setVisible(false);
                bSeleccionarJugadorPorPos.setVisible(false);
                iniciarPartidaButton.setVisible(true);
            }
        });

        bSeleccionarJugadorPorPos.addActionListener(e -> {
            try {
                boolean disponible;
                int pos;
                pos = solicitarPosicion();
                if (pos == -1) {
                    return;
                }
                disponible = controlador.jugadorRegistradoEstaDisponible(pos);
                if (!disponible) {
                    println(">>> El jugador seleccionado NO está disponible. Intente con otro.");
                    return;
                }
                jugadorLocal = controlador.obtenerJugadoresRegistrados().get(pos);
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }

            println(">>> Se ha seleccionado el jugador " + jugadorLocal.getNombre());
            bCrearNuevoJugador.setVisible(false);
            bSeleccionarJugadorPorPos.setVisible(false);
            iniciarPartidaButton.setVisible(true);
        });

        bSeleccionarExistente.addActionListener(e -> {
            try {
                boolean disponible;
                int pos;
                pos = solicitarPosicion();
                if (pos == -1) {
                    return;
                }
                disponible = controlador.jugadorParaReanudarDisponible(pos);
                if (!disponible) {
                    println(">>> El jugador seleccionado ya fue elegido. Intente con el otro :/");
                    return;
                }
                jugadorLocal = controlador.obtenerJugadoresParaReanudar().get(pos);
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }

            println(">>> Se ha seleccionado el jugador " + jugadorLocal.getNombre());
            bSeleccionarExistente.setVisible(false);
            iniciarPartidaButton.setVisible(true);
        });

        println("   ¡Bienvenido nuevamente al Juego del Molino!\n");
        println("Primero que nada vamos a analizar el estado del Juego encontrado en el Servidor seleccionado...\n");
        try {
            if (controlador.esPartidaNueva()) {
                println("¡Se va a iniciar una nueva partida dentro de muy poco! Comencemos por conocerte, así te registramos.");
                println("NOTA IMPORTANTE: El primer jugador que se una a la sesión mueve primero.\n");
                println("* * * * ... Analizando lista de jugadores registrados en el servidor... * * * *\n");
                if (!controlador.hayJugadoresRegistrados()) {
                    println("No se ha encontrado registro de jugadores antiguos.");
                    bCrearNuevoJugador.setVisible(true);
                } else {
                    bCrearNuevoJugador.setVisible(true);
                    bSeleccionarJugadorPorPos.setVisible(true);
                    println("¡Se han detectado jugadores dentro del servidor!\n>>> Jugadores detectados:\n");
                    int i = 1;
                    for (Jugador jugador : controlador.obtenerJugadoresRegistrados()) {
                        println("Jugador N°" + i + "  :");
                        println(jugador.toString());
                        println("");
                        i++;
                    }
                    println("\n");
                    println("En caso de que el jugador se encuentre en la lista, podés seleccionarlo con el número de su posición.");
                    println("Caso contrario, ¡cree un nuevo jugador!");
                }
            } else {
                println("Se ha encontrado una partida que está a punto de reanudarse.");
                println("Seleccioná el jugador que te corresponda, no hagas trampa ;)");
                bSeleccionarExistente.setVisible(true);
                int i = 1;
                for (Jugador jugador : controlador.obtenerJugadoresParaReanudar()) {
                    println("Jugador N°" + i + "  :");
                    println(jugador.toString());
                    println("");
                    i++;
                }
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void initComponents() {
        icono = new ImageIcon(getClass().getResource("/ar/edu/unlu/poo/images/LogoMolino.jpg")).getImage();
        Image originalImage = icono;
        Image scaledImage = originalImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        icono = new ImageIcon(scaledImage).getImage();
    }

    private int solicitarPosicion() {
        boolean inputValido = false;
        int i = 0;
        while (!inputValido) {
            String index = JOptionPane.showInputDialog(null, "Ingrese la posicion que identifique a su jugador ('0' para volver al menú): ");
            try {
                int size = controlador.obtenerJugadoresRegistrados().size();
                i = Integer.parseInt(index);
                if (i >= 0 && i <= size) {
                    inputValido = true;
                } else {
                    JOptionPane.showMessageDialog(null, "El ID ingresado no está dentro del rango válido (1 / " + size + "). Introduzca '0' para volver al menú.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Ingrese un número válido para el ID.");
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        return i - 1;
    }

    private void println(String texto) {
        textArea.append(texto + "\n");
    }

    private void limpiarPantalla() {
        textArea.setText("");
    }
}
