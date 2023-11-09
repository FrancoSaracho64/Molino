package ar.edu.unlu.poo;

import ar.edu.unlu.poo.controladores.TableroControlador;
import ar.edu.unlu.poo.interfaces.TableroImpl;
import ar.edu.unlu.poo.modelos.Jugador;
import ar.edu.unlu.poo.modelos.Tablero;
import ar.edu.unlu.poo.reglas.ReglasDelJuego;
import ar.edu.unlu.poo.vistas.VistaConsola;
import ar.edu.unlu.poo.vistas.VistaInterfazGrafica;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Molino {
    private static final String ARCHIVO_JUGADORES = "jugadores.dat";

    public static void main(String[] args) {
        System.out.println("¡Bienvenido al Juego del Molino!");
        ArrayList<Jugador> jugadoresRegistrados = cargarJugadores();
        ArrayList<Jugador> jugadoresActivos = new ArrayList<>();
        if(jugadoresRegistrados.isEmpty()){
            System.out.println("\n+++++++++++++++++++++++++++++\nConozcamos a los jugadores...\n+++++++++++++++++++++++++++++\n");
            System.out.println("Jugador 1...");
            Jugador jugador_1 = solicitarJugador();
            System.out.println(jugador_1.getId());
            System.out.println("Jugador 2...");
            Jugador jugador_2 = solicitarJugador();
            System.out.println(jugador_2.getId());
            System.out.println();

            jugadoresActivos.add(jugador_1);
            jugadoresActivos.add(jugador_2);
        } else {
            System.out.println("Se han detectado jugadores...");
            /*
            ...
            Preguntar por algún jugador existente, si no, cargar los jugadores normalmente.
             */
        }
        guardarJugadores(jugadoresActivos);


        Tablero tablero = new Tablero(13, 13);

        int opcion = solicitarOpcion();
        boolean valida = false;
        while (!valida) {
            switch (opcion) {
                case 1: {
                    valida = true;
                    System.out.println("Ejecutando juego por consola...");
                    TableroImpl vista = new VistaConsola();
                    TableroControlador controlador = new TableroControlador(jugadoresActivos, tablero, vista);
                    controlador.iniciarPartida();
                    break;
                }
                case 2: {
                    valida = true;
                    System.out.println("Interfaz grafica");
                    TableroImpl vista = new VistaInterfazGrafica();
                    TableroControlador controlador = new TableroControlador(jugadoresActivos, tablero, vista);
                    controlador.iniciarPartida();
                    //
                    break;
                }
                case 0: {
                    valida = true;
                    break;
                }
                default: {
                    System.out.println("Opcion inválida. Vuelva a intentar...\n");
                    opcion = solicitarOpcion();
                }
            }
        }
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