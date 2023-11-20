package ar.edu.unlu.poo.persistencia;

import ar.edu.unlu.poo.modelos.Jugador;

import java.io.*;
import java.util.ArrayList;

public class Persistencia {
    private static final String ARCHIVO_JUGADORES_HISTORICO = "historicoJugadores.dat";
    private static final String ARCHIVO_JUGADORES_ACTUALES = "jugadoresActuales.dat";
    private static final String ARCHIVO_TABLERO = "tablero.dat";

    public static void guardarJugadores(ArrayList<Jugador> jugadores) {
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ARCHIVO_JUGADORES_HISTORICO))) {
            salida.writeObject(jugadores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void guardarJugadoresActuales(ArrayList<Jugador> jugadores) {
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ARCHIVO_JUGADORES_ACTUALES))) {
            salida.writeObject(jugadores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Jugador> cargarJugadoresActuales() {
        ArrayList<Jugador> jugadores = new ArrayList<>();
        File archivo = new File(ARCHIVO_JUGADORES_ACTUALES);
        if (archivo.exists()) {
            try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(ARCHIVO_JUGADORES_ACTUALES))) {
                jugadores = (ArrayList<Jugador>) entrada.readObject();
                Jugador.actualizarUltimoId(jugadores);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return jugadores;
    }

    public static ArrayList<Jugador> cargarJugadoresHistorico() {
        ArrayList<Jugador> jugadores = new ArrayList<>();
        File archivo = new File(ARCHIVO_JUGADORES_HISTORICO);
        if (archivo.exists()) {
            try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(ARCHIVO_JUGADORES_HISTORICO))) {
                jugadores = (ArrayList<Jugador>) entrada.readObject();
                Jugador.actualizarUltimoId(jugadores);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return jugadores;
    }
}
