package ar.edu.unlu.poo.vistas;

import ar.edu.unlu.poo.controladores.Controlador;
import ar.edu.unlu.poo.modelos.Jugador;
import ar.edu.unlu.poo.persistencia.Persistencia;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class MenuPrincipal {
    private final JFrame frame;
    private JPanel panel;
    private JButton btnMenu;
    private JTextField editText;
    private JTextArea textArea;
    private JButton seleccionarJ1Button;
    private JButton seleccionarJugadorPorIDButton;
    private JButton iniciarPartidaButton;
    private JButton crearNuevoJugadorButton;
    private Jugador jugadorLocal;
    private final Controlador controlador;

    public MenuPrincipal(Controlador controlador) {
        this.controlador = controlador;
        frame = new JFrame();
        frame.setContentPane(panel);
        frame.setTitle("Juego del Molino - Menú principal");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Aquí puedes agregar cualquier acción que desees realizar antes de cerrar.
                // Por ejemplo, mostrar un diálogo de confirmación.
                try {
                    int confirm = JOptionPane.showConfirmDialog(frame,
                            "¿Estás seguro de que quieres cerrar la aplicación? El juego todavía no ha comenzado.",
                            "Confirmar salida", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        // Si el usuario confirma, cierra la aplicación.
                        controlador.cerrarAplicacion();
                        frame.dispose(); // Cierra la ventana
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
            this.frame.dispose();
        });

        crearNuevoJugadorButton.addActionListener(e -> {
            println("");
            println("¡Generando Jugador nuevo!");
            // Verificamos que ese nombre no exista
            boolean ocupado;
            String nombre = JOptionPane.showInputDialog(null, "Ingrese nombre del nuevo jugador: ");
            try {
                ocupado = controlador.existeElNombre(nombre);
                if (ocupado) {
                    println("El nombre ingresado ya se encuentra registrado. Intente con otro.");
                    return;
                }
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
            println("Jugador creado con exito. ¡Bienvenido " + nombre + "!");
            jugadorLocal = new Jugador(nombre);
            crearNuevoJugadorButton.setVisible(false);
            seleccionarJugadorPorIDButton.setVisible(false);
            iniciarPartidaButton.setVisible(true);
        });

        seleccionarJugadorPorIDButton.addActionListener(e -> {
            try {
                boolean disponible;
                int pos;
                pos = solicitarPosicion();
                if (pos == -1) {
                    return;
                }
                disponible = controlador.jugadorRegistradoEstaDisponible(pos);
                if (!disponible) {
                    println("El jugador seleccionado NO está disponible. Intente con otro.");
                    return;
                }
                jugadorLocal = controlador.obtenerJugadoresRegistrados().get(pos);
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }

            println("Se ha seleccionado el jugador " + jugadorLocal.getNombre());
            crearNuevoJugadorButton.setVisible(false);
            seleccionarJugadorPorIDButton.setVisible(false);
            iniciarPartidaButton.setVisible(true);
        });

        seleccionarJ1Button.addActionListener(e -> {
            jugadorLocal = pedirJugador();
            seleccionarJ1Button.setVisible(false);
            iniciarPartidaButton.setVisible(true);
        });

        btnMenu.addActionListener(e -> {
            String opc = procesarEntrada(editText.getText());
            editText.setText("");
            if (opc != null) {
                switch (opc) {
                    case "1" -> {
                        btnMenu.setVisible(false);
                        editText.setVisible(false);
                        limpiarPantalla();
                        println("NOTA IMPORTANTE: El jugador 1 mueve primero.\n");
                        println("* * * Analizando lista de jugadores... * * *\n");
                        try {
                            if (!controlador.hayJugadoresRegistrados()) {
                                println("No se ha encontrado registro de jugadores antiguos.");
                                seleccionarJ1Button.setVisible(true);
                            } else {
                                crearNuevoJugadorButton.setVisible(true);
                                seleccionarJugadorPorIDButton.setVisible(true);
                                println("Se han detectado jugadores antiguos.\nLos jugadores detectados son los siguientes:\n");
                                int i = 1;
                                for (Jugador jugador : controlador.obtenerJugadoresRegistrados()) {
                                    println("Jugador N°" + i + "  :");
                                    println(jugador.toString());
                                    println("");
                                    i++;
                                }
                                println("\n\n");
                                println("Seleccione/Cargue los jugadores a través del botón.");
                                println("\n");
                                println("En caso de que el jugador se encuentre en la lista, ingrese el ID correspondiente.");
                                println("Caso contrario, toque el botón para crear un jugador.");
                            }
                        } catch (RemoteException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    case "2" -> {
                        ArrayList<Jugador> jugadoresRegistrados = Persistencia.cargarJugadoresHistorico();
                        if (jugadoresRegistrados.isEmpty()) {
                            println("---- Hasta el momento no se han registrado jugadores ----\n");
                        } else {
                            ordenamientoPuntaje(jugadoresRegistrados);
                            int pos = 1;
                            for (Jugador jugador : jugadoresRegistrados) {
                                println("POSICION " + pos + " --->  Nombre: " + jugador.getNombre() + " - Puntaje: " + jugador.getPuntaje());
                                pos++;
                            }
                        }
                    }
                    default -> println("Opcion inválida. Vuelva a intentar...\n");
                }
            }
        });

        println("\t\t¡Bienvenid@ al Juego del Molino!\n\n");
        mostrar_menu();
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

    private static void ordenamientoPuntaje(List<Jugador> jugadores) {
        int n = jugadores.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (jugadores.get(j).getPuntaje() < jugadores.get(j + 1).getPuntaje()) {
                    // Intercambiar jugadores
                    Jugador temp = jugadores.get(j);
                    jugadores.set(j, jugadores.get(j + 1));
                    jugadores.set(j + 1, temp);
                }
            }
        }
    }

    private String procesarEntrada(String input) {
        input = input.trim();
        if (input.isEmpty())
            return null;
        return input;
    }

    private void mostrar_menu() {
        println("\tMenu principal");
        println("1)   Iniciar nueva partida");
        println("2)   Mostrar TOP 5 de mejores jugadores\n");
    }

    private Jugador pedirJugador() {
        String userInput = JOptionPane.showInputDialog(null, "Ingrese nombre del jugador: ");
        println("Jugador   '" + userInput + "'   registrado correctamente.");
        return new Jugador(userInput);
    }

    private void println(String texto) {
        textArea.append(texto + "\n");
    }

    private void limpiarPantalla() {
        textArea.setText("");
    }
}
