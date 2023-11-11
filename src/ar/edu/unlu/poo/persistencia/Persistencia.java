package ar.edu.unlu.poo.persistencia;

import ar.edu.unlu.poo.modelos.Jugador;

import java.io.*;
import java.util.ArrayList;

public class Persistencia {
    private static final String ARCHIVO_JUGADORES = "jugadores.dat";
    private static final String ARCHIVO_TABLERO = "tablero.dat";

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
}
