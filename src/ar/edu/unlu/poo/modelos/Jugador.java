package ar.edu.unlu.poo.modelos;

import ar.edu.unlu.poo.enumerados.EstadoJuego;

import java.io.Serializable;
import java.util.ArrayList;

public class Jugador implements Serializable {
    private ArrayList<Ficha> fichas;
    private final Integer id;
    private final String nombre;
    private int puntaje;
    private int victorias;
    private int empates;
    private int derrotas;
    private int fichasColocadas;
    private int fichasEnTablero;

    public Jugador(String nombre) {
        this.id = generarHashID(nombre);
        this.nombre = nombre;
        this.puntaje = 0;
        this.victorias = 0;
        this.derrotas = 0;
        this.fichasColocadas = 0;
        this.fichasEnTablero = 0;
        this.fichas = new ArrayList<>();
    }

    public ArrayList<Ficha> getFichas() {
        return fichas;
    }

    public Ficha getFichaParaColocar() {
        Ficha ficha = fichas.get(fichasColocadas);
        incFichasEnTablero();
        incFichasColocadas();
        return ficha;
    }

    public void setFichas(ArrayList<Ficha> fichas){
        this.fichas = fichas;
    }

    public int getFichasEnTablero() {
        return fichasEnTablero;
    }

    public void resetearFichasEnTablero() {
        this.fichasEnTablero = 0;
    }

    public void incFichasEnTablero(){
        fichasEnTablero++;
    }

    public void decFichasEnTablero(){
        fichasEnTablero--;
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

    public void empataPartida(){
        this.puntaje += 50;
        this.empates++;
    }

    public void ganaPartida(){
        this.puntaje += 100;
        this.victorias ++;
    }

    public void pierdePartida(){
        this.derrotas++;
    }

    @Override
    public String toString() {
        return "  '" + nombre + "'     \n" +
            "   ESTADISTICAS:"+
            "\n     Puntaje: " + puntaje +
            "\n     Victorias: " + victorias +
            "\n     Empates: " + empates +
            "\n     Derrotas: " + derrotas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jugador jugador = (Jugador) o;
        return id.equals(jugador.id) && nombre.equals(jugador.nombre);
    }

    private int generarHashID(String nombre){
        long timestamp = System.currentTimeMillis();
        String nombreLimpio = nombre.replaceAll("\\s+", "_").replaceAll("[^a-zA-Z0-9_]", "");
        String input = nombreLimpio + "_" + timestamp;
        return input.hashCode();
    }
}
