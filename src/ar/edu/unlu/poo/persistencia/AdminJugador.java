package ar.edu.unlu.poo.persistencia;

import ar.edu.unlu.poo.modelos.Jugador;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AdminJugador {

    private static final String ARCHIVO_JUGADORES = "jugadores.dat";

    public static void guardarJugadores(List<Jugador> jugadores) {
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ARCHIVO_JUGADORES))) {
            salida.writeObject(jugadores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Jugador> cargarJugadores() {
        List<Jugador> jugadores = new ArrayList<>();
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(ARCHIVO_JUGADORES))) {
            jugadores = (List<Jugador>) entrada.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return jugadores;
    }
}
