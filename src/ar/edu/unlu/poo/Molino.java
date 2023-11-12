package ar.edu.unlu.poo;

import ar.edu.unlu.poo.controladores.TableroControlador;
import ar.edu.unlu.poo.interfaces.TableroImpl;
import ar.edu.unlu.poo.modelos.Jugador;
import ar.edu.unlu.poo.modelos.Tablero;
import ar.edu.unlu.poo.persistencia.Persistencia;
import ar.edu.unlu.poo.vistas.utilidadesConsola.EntradaTeclado;
import ar.edu.unlu.poo.vistas.VistaConsola;

import java.util.ArrayList;
import java.util.Scanner;

public class Molino {
    public static void main(String[] args) {
        ArrayList<Jugador> jugadoresActivos;
        ArrayList<Jugador> jugadoresRegistrados = Persistencia.cargarJugadores();
        boolean salida = false;
        System.out.println("¡Bienvenid@ al Juego del Molino!");
        do{
            mostrar_menu();
            int opc = EntradaTeclado.pedirIntOpcion();
            switch (opc) {
                case 1 -> {
                    System.out.println("NOTA IMPORTANTE: El primer jugador cargado, inicia la partida, es decir, mueve primero.\n");
                    System.out.println("Analizando lista de jugadores...");
                    if (jugadoresRegistrados.isEmpty()) {
                        System.out.println("No se ha encontrado registro de jugadores antiguos. Entonces...");
                        jugadoresActivos = pedirJugadores();
                        jugadoresRegistrados.addAll(jugadoresActivos);
                    } else {
                        System.out.println("Se han detectado jugadores antiguos.\nLos jugadores detectados son los siguientes:\n");
                        int i = 1;
                        for (Jugador jugador : jugadoresRegistrados) {
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
                    TableroControlador controlador = new TableroControlador(jugadoresActivos, tablero);
                    boolean valida = false;
                    do {
                        int opcion = solicitarModo();
                        switch (opcion) {
                            case 1 -> {
                                valida = true;
                                System.out.println("Ejecutando juego por consola...");
                                TableroImpl vista = new VistaConsola(controlador);
                                controlador.agregarVista(vista);
                                vista.iniciarJuego();
                            }
                            case 2 -> {
                                valida = true;
                                System.out.println("Interfaz grafica");

                                /*TableroControlador controlador = new TableroControlador(jugadoresActivos, tablero);
                                TableroImpl vista = new VistaInterfazGrafica(controlador);
                                controlador.iniciarPartida();*/
                            }
                            case 0 -> valida = true;
                            default -> System.out.println("Opcion inválida. Vuelva a intentar...\n");
                        }
                    } while (!valida);
                    Persistencia.guardarJugadores(jugadoresRegistrados);
                }
                case 2 -> {
                    if (jugadoresRegistrados.isEmpty()) {
                        System.out.println("Hasta el momento no se han registrado jugadores.");
                    } else {
                        EntradaTeclado.presionarEnterParaContinuar();
                    }
                }
                case 3 -> {
                    //reglas del juego.
                    EntradaTeclado.presionarEnterParaContinuar();
                }
                case 0 -> {
                    salida = true;
                    System.out.println();
                    System.out.println("Fin de la ejecución.");
                    System.out.println("Gracias por Jugar al Molino.");
                }
                default -> System.out.println("Opcion inválida. Vuelva a intentar...\n");
            }
        } while (!salida);
    }

    private static void mostrar_menu(){
        System.out.println();
        System.out.println("\t\tMenu principal");
        System.out.println("1)   Iniciar nueva partida");
        System.out.println("2)   Mostrar TOP 5 de mejores jugadores");
        System.out.println("3)   Leer reglas del juego");
        System.out.println("\n0)   Cerrar");
    }

    private static ArrayList<Jugador> seleccionarJugadoresExistentes(ArrayList<Jugador> jugadoresRegistrados) {
        ArrayList<Jugador> jugadores = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            System.out.println("En caso de que el jugador " +i+ "se encuentre en la lista, ingrese el ID correspondiente.");
            System.out.println("Caso contrario, ingrese: '-1'");
            int id_jugador = EntradaTeclado.pedirIntID();
            if (id_jugador == -1) {
                System.out.println();
                System.out.println("¡Generando Jugador nuevo!");
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
                        id_jugador = EntradaTeclado.pedirIntID();
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

    private static Jugador solicitarJugador(){
        Scanner t = new Scanner(System.in);
        System.out.print("Ingrese nombre del jugador: ");
        String nombre = t.next();
        System.out.println("Jugador '" + nombre + "' registrado correctamente.");
        System.out.println();
        return new Jugador(nombre);
    }

    private static int solicitarModo(){
        System.out.println("¿Cómo desea ejecutar el juego?\n\t1) Consola\n\t2) Interfaz gráfica\n\n\t0) Salir");
        return EntradaTeclado.pedirIntOpcion();
    }
}