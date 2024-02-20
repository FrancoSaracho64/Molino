package ar.edu.unlu.poo.vistas.elementosParaInterfazGrafica;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class CircularButton extends JButton {
    public CircularButton() {
        ImageIcon fondoCasilla = new ImageIcon(getClass().getResource("/ar/edu/unlu/poo/images/FondoCasillaClaro.png"));
        Image originalImage = fondoCasilla.getImage();
        Image scaledImage = originalImage.getScaledInstance(46, 46, Image.SCALE_SMOOTH);
        fondoCasilla = new ImageIcon(scaledImage);
        // Hace el botón transparente
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setIcon(fondoCasilla);

        // Esto es para asegurar que el área clickeable sea circular también
        Dimension size = getPreferredSize();
        size.width = size.height = Math.max(size.width, size.height);
        setPreferredSize(size);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Define un color marrón claro
        Color marronClaro = new Color(210, 180, 140);

        // Si el botón está siendo presionado, podrías oscurecer el color
        if (getModel().isArmed()) {
            g2.setColor(marronClaro.darker());
        } else {
            g2.setColor(marronClaro);
        }

        // Rellena el botón con el color definido
        g2.fillOval(0, 0, getSize().width - 1, getSize().height - 1);

        // Llama a la implementación de paintComponent de la superclase para permitir otros elementos de UI como el texto del botón
        // Nota: esto se puede quitar si deseas un botón sin texto o icono
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // No pintar el borde
    }

    @Override
    public boolean contains(int x, int y) {
        Shape shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
        return shape.contains(x, y);
    }
}
