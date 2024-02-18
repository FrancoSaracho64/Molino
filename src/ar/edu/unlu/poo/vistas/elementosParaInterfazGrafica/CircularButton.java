package ar.edu.unlu.poo.vistas.elementosParaInterfazGrafica;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class CircularButton extends JButton {
    public CircularButton() {
        // Hace el botón transparente
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);

        // Esto es para asegurar que el área clickeable sea circular también
        Dimension size = getPreferredSize();
        size.width = size.height = Math.max(size.width, size.height);
        setPreferredSize(size);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color color = new Color(0, 0, 0, 0);
        if (getModel().isArmed()) {
            g2.setColor(color.darker());
        } else {
            g2.setColor(color);
        }
        g2.fillOval(0, 0, getSize().width - 1, getSize().height - 1);
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(getForeground());
        g.drawOval(0, 0, getSize().width - 1, getSize().height - 1);
    }

    @Override
    public boolean contains(int x, int y) {
        Shape shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
        return shape.contains(x, y);
    }
}
