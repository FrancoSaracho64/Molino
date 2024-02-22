package ar.edu.unlu.poo.online;

import ar.edu.unlu.poo.controladores.Controlador;
import ar.edu.unlu.poo.interfaces.IVista;
import ar.edu.unlu.poo.vistas.VistaConsola;
import ar.edu.unlu.poo.vistas.VistaConsolaMejorada;
import ar.edu.unlu.poo.vistas.VistaInterfazGrafica;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.cliente.Cliente;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ClienteMolino {
    public ClienteMolino() {
        ArrayList<String> opciones = new ArrayList<>();
        opciones.add("Interfáz gráfica");
        opciones.add("Consola");
        opciones.add("Consola (con UI mejorada)");
        ArrayList<String> ips = Util.getIpDisponibles();
        String ip = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la IP en la que escuchará peticiones el cliente", "IP del cliente",
                JOptionPane.QUESTION_MESSAGE,
                null,
                ips.toArray(),
                null
        );
        String port = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el puerto en el que escuchará peticiones el cliente", "Puerto del cliente",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                9999
        );
        String ipServidor = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la IP en la corre el servidor", "IP del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null
        );
        String portServidor = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el puerto en el que corre el servidor", "Puerto del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                8888
        );
        String interfaz = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione como quiere visualizar el juego", "Interfaz gráfica",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones.toArray(),
                null
        );
        Controlador controlador = new Controlador();
        IVista vista;

        if (interfaz.equals("Consola")) {
            vista = new VistaConsola(controlador);
        } else if (interfaz.equals("Consola (con UI mejorada)")) {
            vista = new VistaConsolaMejorada(controlador);
        } else {
            vista = new VistaInterfazGrafica(controlador);
        }
        Cliente cliente = new Cliente(ip, Integer.parseInt(port), ipServidor, Integer.parseInt(portServidor));
        try {
            cliente.iniciar(controlador);
            vista.iniciar();
        } catch (RemoteException | RMIMVCException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ClienteMolino();
    }
}
