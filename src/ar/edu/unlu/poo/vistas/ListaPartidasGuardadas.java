package ar.edu.unlu.poo.vistas;

import ar.edu.unlu.poo.online.ClienteMolino;
import ar.edu.unlu.poo.online.ServidorMolino;
import ar.edu.unlu.poo.persistencia.PartidaGuardada;
import ar.edu.unlu.poo.persistencia.Persistencia;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ListaPartidasGuardadas extends JFrame {
    public ListaPartidasGuardadas(ArrayList<PartidaGuardada> partidasGuardadas){
        setTitle("Partidas guardadas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(750, 200);
        // Crear un modelo de lista y agregar los elementos
        DefaultListModel<PartidaGuardada> model = new DefaultListModel<>();
        for (PartidaGuardada partida : partidasGuardadas) {
            model.addElement(partida);
        }
        JList<PartidaGuardada> listaElementos = new JList<>(model);
        JScrollPane scrollPane = new JScrollPane(listaElementos);
        add(scrollPane, BorderLayout.CENTER);
        JButton continuarButton = new JButton("Continuar");
        continuarButton.addActionListener(e -> {
            PartidaGuardada seleccion = listaElementos.getSelectedValue();
            if (seleccion != null) {
                // Quitamos la partida seleccionada del archivo para que luego no siga apareciendo.
                ArrayList<PartidaGuardada> partidas = Persistencia.cargarPartidasGuardadas();
                partidas.remove(seleccion);
                Persistencia.guardarPartida(partidas);
                //
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Has seleccionado: " + seleccion);
                dispose();
                // Aquí puedes continuar con la aplicación según la selección del usuario
                new ServidorMolino(seleccion.getModelo());
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Espera, para comenzar correctamente la partida, primero debe unirse otro jugador desde otra computadora.\nLuego de que se haya unido, selecciona en OK y se ejecutará tu cliente.", "¡ESPERA!", JOptionPane.INFORMATION_MESSAGE);
                new ClienteMolino();
            } else {
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Por favor selecciona un elemento.");
            }
        });
        add(continuarButton, BorderLayout.SOUTH);
        setVisible(true);
    }
}
