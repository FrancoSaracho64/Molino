package ar.edu.unlu.poo.online;

import ar.edu.unlu.poo.modelos.Molino;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.servidor.Servidor;

import javax.swing.JOptionPane;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ServidorMolino {
    /**
     * Se inicia un servidor limpio. Es decir, son una partida en 0, sin nada cargado. Un modelo limpio.
     *
     * @param automatico TRUE si se ejecuta automaticamente en LOCAL. FALSE para ejecutarlo manualmente.
     */
    public ServidorMolino(boolean automatico) {
        if (automatico) {
            Molino modelo = new Molino();
            Servidor servidor = new Servidor("127.0.0.1", 8888);
            try {
                servidor.iniciar(modelo);
            } catch (RemoteException | RMIMVCException e) {
                e.printStackTrace();
            }
        } else {
            ArrayList<String> ips = Util.getIpDisponibles();
            String ip = (String) JOptionPane.showInputDialog(
                    null,
                    "Seleccione la IP en la que escuchará peticiones el servidor", "IP del servidor",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    ips.toArray(),
                    null
            );
            String port = (String) JOptionPane.showInputDialog(
                    null,
                    "Seleccione el puerto en el que escuchará peticiones el servidor", "Puerto del servidor",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    null,
                    8888
            );
            Molino modelo = new Molino();
            Servidor servidor = new Servidor(ip, Integer.parseInt(port));
            try {
                servidor.iniciar(modelo);
            } catch (RemoteException | RMIMVCException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Se inicia un servidor en base a un modelo ya creado desde antes.
     * Esto lo hacemos para reanudar una partida.
     *
     * @param modelo
     */
    public ServidorMolino(Molino modelo) {
        ArrayList<String> ips = Util.getIpDisponibles();
        String ip = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la IP en la que escuchará peticiones el servidor", "IP del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                ips.toArray(),
                null
        );
        String port = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el puerto en el que escuchará peticiones el servidor", "Puerto del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                8888
        );
        Servidor servidor = new Servidor(ip, Integer.parseInt(port));
        try {
            servidor.iniciar(modelo);
        } catch (RemoteException | RMIMVCException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ServidorMolino(false);
    }
}