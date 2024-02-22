package ar.edu.unlu.poo.vistas;

import ar.edu.unlu.poo.modelos.Jugador;
import ar.edu.unlu.poo.online.ClienteMolino;
import ar.edu.unlu.poo.online.ServidorMolino;
import ar.edu.unlu.poo.persistencia.PartidaGuardada;
import ar.edu.unlu.poo.persistencia.Persistencia;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MenuMolino extends JFrame {
    private JPanel panel1;
    private JLabel titulo;
    private JButton bReglas;
    private JButton bReanudar;
    private JButton bUnirseRed;
    private JButton bIniciarRed;
    private JButton sobreIniciarRed;
    private JButton sobreUnirseRed;
    private JButton sobreReanudar;
    private JButton bTopJugadores;
    private Image icono;

    public MenuMolino() {
        // Iniciar y configurar Frame
        initElements();
        setSize(350, 300);
        setContentPane(panel1);
        setIconImage(icono);
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Menú principal - Molino");

        titulo.setText("¡Bienvenido al juego Molino!");
        bIniciarRed.setText("Crear un servidor");
        bUnirseRed.setText("Unirse a un servidor");
        bReanudar.setText("Reanudar una partida");
        bTopJugadores.setText("Ver Ranking de jugadores");
        bReglas.setText("¿Cómo jugar? | Reglas");

        // Eventos

        bIniciarRed.addActionListener(e -> {
            new ServidorMolino();
        });
        bUnirseRed.addActionListener(e -> {
            dispose();
            new ClienteMolino();
        });
        bReanudar.addActionListener(e -> {
            ArrayList<PartidaGuardada> partidasGuardadas = Persistencia.cargarPartidasGuardadas();
            if (partidasGuardadas.isEmpty()) {
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "No se han encontrado partidas guardadas en el ordenador", "¡AVISO!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                dispose();
                new ListaPartidasGuardadas(partidasGuardadas);
            }
        });
        bTopJugadores.addActionListener(e -> {
            ArrayList<Jugador> mejoresJugadores = Persistencia.cargarJugadoresHistorico();
            if (mejoresJugadores.isEmpty()) {
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "No se han encontrado Jugadores en este ordenador.", "ERROR", JOptionPane.INFORMATION_MESSAGE);
            } else {
                dispose();
                new MejoresJugadoresDialog(null, mejoresJugadores);
            }
        });
        bReglas.addActionListener(e -> {
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Juego de mesa \"Molino\" / \"Nueve hombres de Morris\", desarrollado en Java para la materia POO (2023)\n" +
                    "\n" +
                    "\n" +
                    "** Reglas de juego**\n" +
                    "Cada jugador dispone de nueve piezas, u \"hombres\", que se mueven en el tablero entre veinticuatro intersecciones. \n" +
                    "El objetivo del juego es dejar al oponente con menos de tres piezas o sin movimiento posible.\n" +
                    "\n" +
                    "** Colocación las piezas**\n" +
                    "El juego comienza con un tablero vacío. Los jugadores se turnan para colocar sus piezas en las intersecciones vacías. \n" +
                    "Si un jugador es capaz de formar una fila de tres piezas a lo largo de una de las líneas del tablero, tiene un \"molino\" \n" +
                    "y puede eliminar una de las piezas de su oponente en el tablero; las piezas retiradas no se pueden volver a poner en \n" +
                    "juego. Los jugadores deben retirar cualquier otra pieza antes de retirar una pieza que forme parte de un molino. \n" +
                    "Una vez que las 18 piezas se han colocado, los jugadores se turnan moviendo.\n" +
                    "\n" +
                    "** Movimiento de las piezas**\n" +
                    "Para mover, el jugador desliza una de sus piezas a lo largo de una línea en el tablero a una intersección vacía \n" +
                    "adyacente. Si no puede hacerlo, ha perdido el juego.\n" +
                    "Al igual que en la etapa de colocación, un jugador que coloca tres de sus piezas en línea en el tablero tiene un \n" +
                    "molino y puede eliminar una de las piezas de su oponente, evitando las piezas que formen parte de un molino, si\n" +
                    "es posible.\n" +
                    "Cualquier jugador al que sólo le queden dos piezas no podrá eliminar más piezas del oponente y, por tanto, perderá \n" +
                    "la partida.\n" +
                    "\n" +
                    "** Vuelo** \n" +
                    "Una vez que un jugador es reducido a tres piezas, sus piezas pueden \"volar\", \"brincar\" o\n" +
                    "\"saltar\" a cualquier intersección vacía, no solo a las adyacentes." +
                    "\n" +
                    "\n" +
                    "*  IMPORTANTE  *\n" +
                    "--- Si se producen 30 movimientos sin realizar ninguna captura, la partida terminará en empate.\n" +
                    "--- Si ambos jugadores tienen 3 fichas, el jugador que hace MOLINO, puede eliminar una ficha del\n" +
                    "        oponente por más que estén formando MOLINO. Es decir, el que hace MOLINO en esta situación, gana ;)", "Reglas del juego", JOptionPane.INFORMATION_MESSAGE);
        });

        sobreIniciarRed.addActionListener(e -> {
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "IMPORTANTE: Al iniciar el servidor desde este menu, primero se tiene que unir otra persona desde\notra computadora. Una vez que se haya unido, ahí recién nos podremos unir nosotros,\ncolocando nuestra IP que hayamos seleccionado.", "Sobre... iniciar un servidor", JOptionPane.INFORMATION_MESSAGE);
        });
        sobreUnirseRed.addActionListener(e -> {
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Si otra persona desde otra computadora inició el servidor, simplemente iniciamos esta opción y colocamos\nlos datos necesarios para poder unirmos. Si vos creaste el servidor, primero esperá a que otra\npersona se una, y luego te unis vos.", "Sobre... unirse a una partida en red", JOptionPane.INFORMATION_MESSAGE);
        });
        sobreReanudar.addActionListener(e -> {
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Si se encuentra una partida guardada en nuestro ordenador, se podrá reanudar.", "Sobre... reaunudar una partida", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void initElements() {
        icono = new ImageIcon(getClass().getResource("/ar/edu/unlu/poo/images/LogoMolino.jpg")).getImage();
        Image originalImage = icono;
        Image scaledImage = originalImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        icono = new ImageIcon(scaledImage).getImage();
    }

    private static class MejoresJugadoresDialog extends JDialog {
        public MejoresJugadoresDialog(Frame parent, ArrayList<Jugador> mejoresJugadores) {
            super(parent, "Ranking mejores Jugadores", true);
            setLocationRelativeTo(null);
            // Crear un modelo de lista con los datos de los mejores jugadores
            DefaultListModel<Jugador> listModel = new DefaultListModel<>();
            ordenamientoPuntaje(mejoresJugadores);
            for (Jugador jugador : mejoresJugadores) {
                listModel.addElement(jugador);
            }
            // Crear una lista para mostrar los mejores jugadores
            JList<Jugador> listaMejoresJugadores = new JList<>(listModel);
            // Agregar la lista a un JScrollPane para que sea desplazable
            JScrollPane scrollPane = new JScrollPane(listaMejoresJugadores);
            // Agregar el JScrollPane al contenido del diálogo
            getContentPane().add(scrollPane, BorderLayout.CENTER);
            // Configurar el tamaño y la ubicación del diálogo
            setSize(800, 300);
            setLocationRelativeTo(parent);
        }
    }

    private static void ordenamientoPuntaje(List<Jugador> jugadores) {
        int n = jugadores.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (jugadores.get(j).getPuntaje() < jugadores.get(j + 1).getPuntaje()) {
                    Jugador temp = jugadores.get(j);
                    jugadores.set(j, jugadores.get(j + 1));
                    jugadores.set(j + 1, temp);
                }
            }
        }
    }
}
