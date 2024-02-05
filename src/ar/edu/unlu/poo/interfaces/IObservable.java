package ar.edu.unlu.poo.interfaces;

public interface IObservable {
    void agregarObservador(IObserver observador);
    void quitarObservador(IObserver observador);
    void notificarObservadores();
}
