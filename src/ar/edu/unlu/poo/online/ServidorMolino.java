package ar.edu.unlu.poo.online;

import ar.edu.unlu.poo.modelos.Molino;
import ar.edu.unlu.poo.vistas.MenuMolino;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.servidor.Servidor;

import javax.swing.JOptionPane;
import java.rmi.RemoteException;
import java.rmi.server.ExportException;
import java.util.ArrayList;

public class ServidorMolino {
    /**
     * Se inicia un servidor limpio. Es decir, es una partida en 0, sin nada cargado. Un modelo limpio.
     */
    public ServidorMolino() {
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
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Se ha iniciado correctamente el servidor.\nPara unirte a la partida, volvé al menú principal y seleccioná la opción 'Unirse a un servidor',\ncompletando con los datos de tu computadora/red.'", "Servidor iniciado.", JOptionPane.INFORMATION_MESSAGE);
        } catch (RemoteException | RMIMVCException e) {
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "No se ha podido iniciar correctamente el servidor. Vuelva a intentar.", "Error al iniciar el servidor", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Se inicia un servidor en base a un modelo ya creado desde antes.
     * Esto lo hacemos para reanudar una partida.
     *
     * @param modelo Es el Modelo que se va utilizar para reanudar la partida.
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
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Se ha iniciado correctamente el servidor.\nPara unirte a la partida, volvé al menú principal y seleccioná la opción 'Unirse a un servidor',\ncompletando con los datos de tu computadora/red.'", "Servidor iniciado.", JOptionPane.INFORMATION_MESSAGE);
        } catch (RMIMVCException | RemoteException e) {
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "No se ha podido iniciar correctamente el servidor. Vuelva a intentar.", "Error al iniciar el servidor", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ServidorMolino();
    }
}