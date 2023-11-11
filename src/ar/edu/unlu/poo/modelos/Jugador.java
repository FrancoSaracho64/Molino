package ar.edu.unlu.poo.modelos;

import java.io.*;
import java.util.ArrayList;

public class Jugador implements Serializable {
    private static Integer id_jugador = 0;
    private final Integer id;
    private final String nombre;
    private int puntaje;
    private int victorias;
    private int derrotas;

    public Jugador(String nombre) {
        id_jugador++;
        this.id = id_jugador;
        this.nombre = nombre;
        this.puntaje = 0;
        this.victorias = 0;
        this.derrotas = 0;
    }

    public static void actualizarUltimoId(ArrayList<Jugador> jugadores) {
        for (Jugador jugador : jugadores) {
            if (jugador.getId() > id_jugador) {
                id_jugador = jugador.getId();
            }
        }
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public int getVictorias() {
        return victorias;
    }

    public int getDerrotas() {
        return derrotas;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public void setVictorias(int victorias) {
        this.victorias = victorias;
    }

    public void setDerrotas(int derrotas) {
        this.derrotas = derrotas;
    }

    @Override
    public String toString() {
        return "Jugador ---> " + nombre + "  //  ID: **" + id +"**\n" +
            "ESTADISTICAS:"+
            "\n     Puntaje: " + puntaje +
            "\n     Victorias=" + victorias +
            "\n     Derrotas=" + derrotas;
    }
}
