package ar.edu.unlu.poo.interfaces;

import java.rmi.RemoteException;

public interface IObserver {
    void actualizar() throws RemoteException;
}
