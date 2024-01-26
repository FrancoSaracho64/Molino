package ar.edu.unlu.poo.modelos;

import ar.edu.unlu.poo.enumerados.EstadoCasilla;

import java.util.ArrayList;
import java.util.List;

public class Casilla {
    private final Coordenada coordenada;
    private Ficha ficha;
    private EstadoCasilla estado;
    private ArrayList<Casilla> casillasAdyacentes;

    public Casilla(Coordenada coordenada){
        this.coordenada = coordenada;
        this.casillasAdyacentes = new ArrayList<>();
        this.estado = EstadoCasilla.INVALIDA;
        this.ficha = null;
    }

    public void setEstadoCasilla(EstadoCasilla estadoCasilla) {
        this.estado = estadoCasilla;
    }

    public EstadoCasilla getEstadoCasilla() {
        return estado;
    }

    public Ficha getFicha() {
        return ficha;
    }

    public void colocarFicha(Ficha ficha) {
        this.ficha = ficha;
        this.estado = EstadoCasilla.OCUPADA;
    }

    public void quitarFicha() {
        ficha = null;
        estado = EstadoCasilla.LIBRE;
    }

    public void agregarAdyacentes(List<Casilla> adyacentes) {
        // Agregar una casilla adyacente a la lista
        casillasAdyacentes.addAll(adyacentes);
    }

    public ArrayList<Casilla> getCasillasAdyacentes() {
        return casillasAdyacentes;
    }

    public ArrayList<Coordenada> getCoordenadasAdyacentes(){
        ArrayList<Coordenada> coordenadas = new ArrayList<>();
        for (Casilla casilla : casillasAdyacentes){
            coordenadas.add(casilla.getCoordenada());
        }
        return coordenadas;
    }

    public Coordenada getCoordenada(){
        return coordenada;
    }
}
