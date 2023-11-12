package ar.edu.unlu.poo.interfaces;

public interface Observable {
    void agregarObservador(Observer observador);
    void quitarObservador(Observer observador);
    void notificarObservadores();
}
