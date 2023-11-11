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
    private int fichasColocadas;

    public Jugador(String nombre) {
        this.id = id_jugador;
        id_jugador++;
        this.nombre = nombre;
        this.puntaje = 0;
        this.victorias = 0;
        this.derrotas = 0;
        this.fichasColocadas = 0;
    }

    public static void actualizarUltimoId(ArrayList<Jugador> jugadores) {
        id_jugador = jugadores.size();
    }

    public int getFichasColocadas() {
        return fichasColocadas;
    }

    public void incFichasColocadas() {
        this.fichasColocadas++;
    }

    public void resetearFichasColocadas(){
        this.fichasColocadas = 0;
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
        return "Jugador ---> " + nombre + "  /  ID: " + id +"\n" +
            "ESTADISTICAS:"+
            "\n     Puntaje: " + puntaje +
            "\n     Victorias: " + victorias +
            "\n     Derrotas: " + derrotas;
    }
}
