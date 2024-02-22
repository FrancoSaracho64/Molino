package ar.edu.unlu.poo.modelos;

import ar.edu.unlu.poo.enumerados.EstadoCasilla;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Casilla implements Serializable {
    private final ArrayList<Casilla> casillasAdyacentes = new ArrayList<>();
    private Coordenada coordenada;
    private Ficha ficha;
    private EstadoCasilla estado;

    /**
     * Se crea una casilla sin coordenada.
     * <p></p>
     * Se le asigna "EstadoCasilla.INVALIDA".
     */
    public Casilla(){
        this.estado = EstadoCasilla.INVALIDA;
    }

    public Casilla(Coordenada coordenada){
        this.coordenada = coordenada;
        this.estado = EstadoCasilla.LIBRE;
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

    /**
     * Dada una lista de Casillas, las agregamos como adyacentes a la casilla seleccionada.
     * @param adyacentes Coordenadas adyacentes.
     */
    public void agregarAdyacentes(List<Casilla> adyacentes) {
        casillasAdyacentes.addAll(adyacentes);
    }

    public ArrayList<Coordenada> getCoordenadasCasillasAdyacentes(){
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
