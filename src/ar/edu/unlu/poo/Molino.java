package ar.edu.unlu.poo;

import ar.edu.unlu.poo.controladores.TableroControlador;
import ar.edu.unlu.poo.interfaces.TableroImpl;
import ar.edu.unlu.poo.modelos.Jugador;
import ar.edu.unlu.poo.modelos.Tablero;
import ar.edu.unlu.poo.vistas.VistaConsola;
import ar.edu.unlu.poo.vistas.VistaInterfazGrafica;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Molino {
    private static final String ARCHIVO_JUGADORES = "jugadores.dat";

    public static void main(String[] args) {
        ArrayList<Jugador> jugadoresActivos;
        ArrayList<Jugador> jugadoresRegistrados = cargarJugadores();

        System.out.println("¡Bienvenid@ al Juego del Molino!");
        System.out.println("NOTA: El primer jugador cargado, inicia la partida, es decir, mueve primero.\n");

        System.out.println("Analizando lista de jugadores...");
        if(jugadoresRegistrados.isEmpty()){
            System.out.println("No se ha encontrado registro de jugadores antiguos. Entonces...");
            jugadoresActivos = pedirJugadores();
            jugadoresRegistrados.addAll(jugadoresActivos);
        } else {
            System.out.println("Se han detectado jugadores antiguos.\nLos jugadores detectados son los siguientes:\n");
            int i = 1;
            for (Jugador jugador : jugadoresRegistrados){
                System.out.println("Jugador N°" + i + " :");
                System.out.println(jugador.toString());
                System.out.println();
                i++;
            }
            System.out.println();
            System.out.println("Seleccion/Carga de Jugadores:");
            jugadoresActivos = new ArrayList<>(seleccionarJugadoresExistentes(jugadoresRegistrados));
        }

        Tablero tablero = new Tablero(13, 13);

        int opcion = solicitarOpcion();
        boolean valida = false;
        while (!valida) {
            switch (opcion) {
                case 1 -> {
                    valida = true;
                    System.out.println("Ejecutando juego por consola...");
                    TableroImpl vista = new VistaConsola();
                    TableroControlador controlador = new TableroControlador(jugadoresActivos, tablero, vista);
                    controlador.iniciarPartida();
                }
                case 2 -> {
                    valida = true;
                    System.out.println("Interfaz grafica");
                    TableroImpl vista = new VistaInterfazGrafica();
                    TableroControlador controlador = new TableroControlador(jugadoresActivos, tablero, vista);
                    controlador.iniciarPartida();
                }
                case 0 -> {
                    valida = true;
                }
                default -> {
                    System.out.println("Opcion inválida. Vuelva a intentar...\n");
                    opcion = solicitarOpcion();
                }
            }
        }
        guardarJugadores(jugadoresRegistrados);
    }

    private static ArrayList<Jugador> seleccionarJugadoresExistentes(ArrayList<Jugador> jugadoresRegistrados) {
        ArrayList<Jugador> jugadores = new ArrayList<>();
        Scanner t = new Scanner(System.in);
        for (int i = 1; i <= 2; i++) {
            System.out.println("En caso de que el jugador " +i+ "se encuentre en la lista, ingrese el ID correspondiente.");
            System.out.println("Caso contrario, ingrese: '-1'");
            System.out.print("     ID_JUGADOR:   ");
            int id_jugador = t.nextInt();
            if (id_jugador == -1) {
                Jugador jugador = solicitarJugador();
                jugadores.add(jugador);
                jugadoresRegistrados.add(jugador);
            } else {
                boolean valido = false;
                while (!valido) {
                    if (id_jugador >= 0 && id_jugador <= jugadoresRegistrados.size() - 1) {
                        valido = true;
                        for (Jugador jugadoresRegistrado : jugadoresRegistrados) {
                            if (jugadoresRegistrado.getId() == id_jugador) {
                                jugadores.add(jugadoresRegistrado);
                                break;
                            }
                        }
                    } else {
                        System.out.println("Ingrese un ID válido. Vuelva a intentar.");
                        System.out.println();
                        System.out.println("En caso de que el jugador " +i+ "se encuentre en la lista, ingrese el ID correspondiente.");
                        System.out.println("Caso contrario, ingrese: '-1'");
                        System.out.print("     ID_JUGADOR:   ");
                        id_jugador = t.nextInt();
                    }
                }
            }
        }
        return jugadores;
    }

    private static ArrayList<Jugador> pedirJugadores() {
        ArrayList<Jugador> jugadores = new ArrayList<>();
        System.out.println("\n+++++++++++++++++++++++++++++\n¡Conozcamos a los jugadores!\n+++++++++++++++++++++++++++++\n");
        System.out.println("Jugador 1...");
        Jugador jugador_1 = solicitarJugador();
        jugadores.add(jugador_1);
        System.out.println("Jugador 2...");
        Jugador jugador_2 = solicitarJugador();
        jugadores.add(jugador_2);
        return jugadores;
    }

    public static void guardarJugadores(ArrayList<Jugador> jugadores) {
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ARCHIVO_JUGADORES))) {
            salida.writeObject(jugadores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Jugador> cargarJugadores() {
        ArrayList<Jugador> jugadores = new ArrayList<>();
        File archivo = new File(ARCHIVO_JUGADORES);
        if (archivo.exists()) {
            try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(ARCHIVO_JUGADORES))) {
                jugadores = (ArrayList<Jugador>) entrada.readObject();
                Jugador.actualizarUltimoId(jugadores);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return jugadores;
    }

    private static Jugador solicitarJugador(){
        Scanner t = new Scanner(System.in);
        System.out.print("Ingrese nombre del jugador: ");
        String nombre = t.next();
        System.out.println(nombre + " cargado.");
        return new Jugador(nombre);
    }

    private static int solicitarOpcion(){
        Scanner t = new Scanner(System.in);
        System.out.println("¿Cómo desea ejecutar el juego?\n\t1) Consola\n\t2) Interfaz gráfica\n\n\t0) Salir");
        System.out.print("Opcion seleccionada: ");
        return t.nextInt();
    }
}