package ar.edu.unlu.poo.modelos;

public class Ficha {
    private final Jugador jugador;
    private boolean enMolino;

    public Ficha(Jugador jugador) {
        this.jugador = jugador;
        this.enMolino = false;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public boolean isEnMolino() {
        return enMolino;
    }

    public void formaMolino(){
        enMolino = true;
    }

    public void noFormaMolino(){
        enMolino = false;
    }
}
