package ar.edu.unlu.poo.vistas.elementosParaInterfazGrafica;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {
    private final Image backgroundImage;

    public BoardPanel() {
        backgroundImage = new ImageIcon(getClass().getResource("/ar/edu/unlu/poo/images/Tablero.png")).getImage();
        setLayout(null);
        setPreferredSize(new Dimension(500, 500));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Dibuja la imagen de fondo ajustándola al tamaño del panel
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
    }
}
