package ar.edu.unlu.poo.persistencia;

import ar.edu.unlu.poo.modelos.Jugador;
import ar.edu.unlu.poo.modelos.Molino;

import java.io.*;
import java.util.ArrayList;

public class Persistencia {
    private static final String ARCHIVO_JUGADORES_HISTORICO = "historicoJugadores.dat";
    private static final String ARCHIVO_PARTIDAS_GUARDADAS = "partidasGuardadas.dat";

    public static void guardarJugadores(ArrayList<Jugador> jugadores) {
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ARCHIVO_JUGADORES_HISTORICO))) {
            salida.writeObject(jugadores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Jugador> cargarJugadoresHistorico() {
        File archivo = new File(ARCHIVO_JUGADORES_HISTORICO);
        if (archivo.exists()) {
            try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(ARCHIVO_JUGADORES_HISTORICO))) {
                return (ArrayList<Jugador>) entrada.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    public static void guardarPartida(ArrayList<PartidaGuardada> partidas) {
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ARCHIVO_PARTIDAS_GUARDADAS))) {
            salida.writeObject(partidas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<PartidaGuardada> cargarPartidasGuardadas() {
        File archivo = new File(ARCHIVO_JUGADORES_HISTORICO);
        if (archivo.exists()) {
            try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(ARCHIVO_PARTIDAS_GUARDADAS))) {
                return (ArrayList<PartidaGuardada>) entrada.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }
}
