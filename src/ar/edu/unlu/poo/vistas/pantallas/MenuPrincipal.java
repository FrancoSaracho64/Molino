package ar.edu.unlu.poo.vistas.pantallas;

import ar.edu.unlu.poo.controladores.TableroControlador;
import ar.edu.unlu.poo.interfaces.VistaTableroI;
import ar.edu.unlu.poo.modelos.Jugador;
import ar.edu.unlu.poo.modelos.Tablero;
import ar.edu.unlu.poo.persistencia.Persistencia;
import ar.edu.unlu.poo.vistas.VistaConsola;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MenuPrincipal {
    private final JFrame frame;
    private JPanel panel1;
    private JButton btnMenu;
    private JTextField editText;
    private JTextArea textArea;
    private JRadioButton radioConsola;
    private JRadioButton radioConsolaGrafica;
    private JRadioButton radioInterGrafica;
    private JButton btnSiguiente;
    private JButton seleccionarJ1Button;
    private JButton seleccionarJ2Button;
    private JButton seleccionarJugadorPorIDButton;
    private JButton iniciarPartidaButton;
    private JButton crearNuevoJugadorButton;
    private ArrayList<Jugador> jugadoresActivos;
    private ArrayList<Jugador> jugadoresRegistrados;

    public MenuPrincipal(){
        frame = new JFrame();
        frame.setContentPane(panel1);
        frame.setTitle("Juego del Molino - Menú principal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);
        jugadoresActivos = new ArrayList<>();
        jugadoresRegistrados = Persistencia.cargarJugadoresHistorico();

        iniciarPartidaButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               limpiarPantalla();
               println("\n\nSeleccione con los RadioButton como quiere ejecutar el juego.\nLuego presione en 'Comenzar juego'.");
               btnSiguiente.setVisible(true);
               radioConsola.setVisible(true);
               radioConsolaGrafica.setVisible(true);
               iniciarPartidaButton.setVisible(false);
           }
       });
        crearNuevoJugadorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                println("");
                println("¡Generando Jugador nuevo!");
                String nombre = JOptionPane.showInputDialog(null, "Ingrese nombre del nuevo jugador: ");
                println("Jugador creado con exito. ¡Bienvenido " + nombre+ "!");
                Jugador jugador = new Jugador (nombre);
                jugadoresActivos.add(jugador);
                jugadoresRegistrados.add(jugador);
                if (jugadoresActivos.size() == 2){
                    crearNuevoJugadorButton.setVisible(false);
                    seleccionarJugadorPorIDButton.setVisible(false);
                    iniciarPartidaButton.setVisible(true);
                }
            }
        });

        seleccionarJugadorPorIDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id_jugador = solicitarID();
                boolean valido = false;
                while (!valido) {
                    if (!jugadoresActivos.isEmpty()){
                        if (jugadoresActivos.get(0).getId() != id_jugador){
                            valido = true;
                            for (Jugador jugadoresRegistrado : jugadoresRegistrados) {
                                if (jugadoresRegistrado.getId() == id_jugador) {
                                    jugadoresActivos.add(jugadoresRegistrado);
                                    println("Se ha seleccionado el jugador " + jugadoresRegistrado.getNombre());
                                    break;
                                }
                            }
                        }
                        else {
                            println("Ha ingresado el mismo jugador. Vuelva a intentar.");
                            println("");
                            id_jugador = solicitarID();
                        }
                    } else {
                        valido = true;
                        for (Jugador jugadoresRegistrado : jugadoresRegistrados) {
                            if (jugadoresRegistrado.getId() == id_jugador) {
                                jugadoresActivos.add(jugadoresRegistrado);
                                println("Se ha seleccionado el jugador " + jugadoresRegistrado.getNombre());
                                break;
                            }
                        }

                    }
                }
                if (jugadoresActivos.size() == 2){
                    crearNuevoJugadorButton.setVisible(false);
                    seleccionarJugadorPorIDButton.setVisible(false);
                    iniciarPartidaButton.setVisible(true);
                }
            }
        });

        seleccionarJ2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Jugador jugador = pedirJugador();
                seleccionarJ2Button.setVisible(false);
                iniciarPartidaButton.setVisible(true);
                jugadoresActivos.add(jugador);
            }
        });

        seleccionarJ1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Jugador jugador = pedirJugador();
                jugadoresActivos.add(jugador);
                seleccionarJ1Button.setVisible(false);
                seleccionarJ2Button.setVisible(true);
            }
        });
        btnMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String opc = procesarEntrada(editText.getText());
                editText.setText("");
                if (opc != null){
                    switch (opc) {
                        case "1" -> {
                            btnMenu.setVisible(false);
                            editText.setVisible(false);
                            limpiarPantalla();
                            println("NOTA IMPORTANTE: El jugador 1 mueve primero.\n");
                            println("* * * Analizando lista de jugadores... * * *\n");
                            if (jugadoresRegistrados.isEmpty()) {
                                println("No se ha encontrado registro de jugadores antiguos.");
                                seleccionarJ1Button.setVisible(true);
                            } else {
                                crearNuevoJugadorButton.setVisible(true);
                                seleccionarJugadorPorIDButton.setVisible(true);
                                println("Se han detectado jugadores antiguos.\nLos jugadores detectados son los siguientes:\n");
                                int i = 1;
                                for (Jugador jugador : jugadoresRegistrados) {
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
                        }
                        case "2" -> {
                            ArrayList<Jugador> jugadoresRegistrados = Persistencia.cargarJugadoresHistorico();
                            if (jugadoresRegistrados.isEmpty()) {
                                println("---- Hasta el momento no se han registrado jugadores ----\n");
                            } else {
                                ordenamientoPuntaje(jugadoresRegistrados);
                                int pos = 1;
                                for (Jugador jugador : jugadoresRegistrados) {
                                    println("POSICION "+pos+"--->  Nombre: " + jugador.getNombre() + " - Puntaje: " + jugador.getPuntaje());
                                    pos++;
                                }
                            }
                        }
                        default -> println("Opcion inválida. Vuelva a intentar...\n");
                    }
                }
            }
        });

        btnSiguiente.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                   editText.setText("");
                   Tablero tablero = new Tablero(13, 13);

                   if (jugadoresRegistrados.isEmpty()){
                       jugadoresRegistrados.addAll(jugadoresActivos);
                   } else {
                       for (int i = 0; i < jugadoresRegistrados.size(); i++){
                           if (jugadoresRegistrados.get(i).getId() == jugadoresActivos.get(0).getId()){
                               jugadoresRegistrados.set(i, jugadoresActivos.get(0));
                           } else if (jugadoresRegistrados.get(i).getId() == jugadoresActivos.get(1).getId()){
                               jugadoresRegistrados.set(i, jugadoresActivos.get(1));
                           }
                       }
                   }
                   // Guardo los jugadores.
                   Persistencia.guardarJugadores(jugadoresRegistrados);
                   TableroControlador controlador = new TableroControlador(jugadoresActivos, tablero);
                   int opcion = 0;
                   if (radioConsola.isSelected())
                       opcion = 1;
                   else if (radioConsolaGrafica.isSelected())
                       opcion = 2;
                   else if (radioInterGrafica.isSelected())
                       opcion = 3;
                   frame.dispose();
                   switch (opcion) {
                       case 1 -> {
                           println("Ejecutando juego por consola...");
                           VistaTableroI vista = new VistaConsola(controlador);
                           controlador.agregarVista(vista);
                           vista.iniciarJuego();
                       }
                       case 2 -> {
                           println("Ejecutando juego por Consola/Interfaz grafica");

                           VistaTableroI vista = new VistaVistaTableroConsola(controlador);
                           controlador.agregarVista(vista);
                           vista.iniciarJuego();
                       }
                       case 3 -> {
                           println("Interfaz grafica");
                           // TODO: IMPLEMENTAR
                       }
                   }

               }
           });
        println("\t\t¡Bienvenid@ al Juego del Molino!\n\n");
        mostrar_menu();
    }

    private int solicitarID() {
        boolean inputValido = false;
        int id_jugador = 0;
        while (!inputValido) {
            String inputID = JOptionPane.showInputDialog(null, "Ingrese un ID: ");
            try {
                id_jugador = Integer.parseInt(inputID);
                if (id_jugador >= 0 && id_jugador < jugadoresRegistrados.size()) {
                    inputValido = true;
                } else {
                    JOptionPane.showMessageDialog(null, "El ID ingresado no está dentro del rango válido (0 / "+ (jugadoresRegistrados.size()-1) + ").");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Ingrese un número válido para el ID.");
            }
        }
        return id_jugador;
    }

    public static void ordenamientoPuntaje(List<Jugador> jugadores) {
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

    private void mostrar_menu(){
        println("\tMenu principal");
        println("1)   Iniciar nueva partida");
        println("2)   Mostrar TOP 5 de mejores jugadores");
    }

    private ArrayList<Jugador> pedirJugadores() {
        ArrayList<Jugador> jugadores = new ArrayList<>();
        //
        println("\n+++++++++++++++++++++++++++++\n¡Conozcamos a los jugadores!\n+++++++++++++++++++++++++++++");
        String userInput = JOptionPane.showInputDialog(null, "Ingrese nombre del jugador 1: ");
        println("Jugador   '" + userInput + "'   registrado correctamente.");
        Jugador j1 = new Jugador(userInput);
        jugadores.add(j1);
        //
        userInput = JOptionPane.showInputDialog(null, "Ingrese nombre del jugador 2: ");
        println("Jugador   '" + userInput + "'   registrado correctamente.");
        Jugador j2 = new Jugador(userInput);
        jugadores.add(j2);
        return jugadores;
    }

    private Jugador pedirJugador() {
        String userInput = JOptionPane.showInputDialog(null, "Ingrese nombre del jugador: ");
        println("Jugador   '" + userInput + "'   registrado correctamente.");
        return new Jugador(userInput);
    }

    private void println(String texto) {
        textArea.append(texto + "\n");
    }

    private void limpiarPantalla(){
        textArea.setText("");
    }
}
