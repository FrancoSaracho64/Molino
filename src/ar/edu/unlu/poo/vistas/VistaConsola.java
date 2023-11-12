package ar.edu.unlu.poo.vistas;

import ar.edu.unlu.poo.interfaces.ControladorImpl;
import ar.edu.unlu.poo.interfaces.Observer;
import ar.edu.unlu.poo.interfaces.TableroImpl;
import ar.edu.unlu.poo.modelos.Coordenada;
import ar.edu.unlu.poo.modelos.Tablero;
import ar.edu.unlu.poo.vistas.utilidadesConsola.EntradaTeclado;

public class VistaConsola implements TableroImpl, Observer {
    private ControladorImpl controlador;

    public VistaConsola(ControladorImpl controlador){
        this.controlador = controlador;
        this.controlador.agregarObserver(this);
    }

    private void limpiarConsola() {
        try {
            String os = System.getProperty("os.name").toLowerCase();

            ProcessBuilder processBuilder;
            if (os.contains("win")) {
                // Para Windows
                processBuilder = new ProcessBuilder("cmd", "/c", "cls");
            } else {
                // Para sistemas tipo Unix (Linux, macOS)
                processBuilder = new ProcessBuilder("clear");
            }

            Process process = processBuilder.inheritIO().start();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String letraColumna (int columna){
        return String.valueOf((char) ('A' + columna));
    }

    private String numeroFila (int fila){
        return String.valueOf(fila + 1);
    }

    private String formatoCelda (String contenido){
        return String.format("%1s", contenido);
    }

    @Override
    public void mostrarTablero(Tablero tablero) {
        int col = 0;
        int fil = 0;
        int filfin = 0;
        int colfin = 0;
        System.out.println("\n\n");
        // Muestra las letras de las columnas
        System.out.print("     "); // Espacio inicial
        for (int columna = 0; columna < 13; columna++) {
            if ( columna!= 12) {
                if (columna % 2 == 0) {
                    System.out.print(" " + formatoCelda(letraColumna(col)) + "    ");
                    col++;
                }
            } else {
                System.out.print(" " + formatoCelda(letraColumna(col) + "      "));
            }
        }
        System.out.println();
        for (int fila = 0; fila < 13; fila++) {
            System.out.print(" ");
            if (fila % 2 == 0) {
                System.out.print(" " + formatoCelda(numeroFila(fil)) + "  "); // Número de fila
                fil++;
            } else {
                System.out.print("    ");
            }
            // Se representa el estado de la casilla
            for (int columna = 0; columna < 13; columna++) {
                String contenidoCelda = controlador.contenidoCasilla(fila, columna);
                if (contenidoCelda.isEmpty()) {
                    if ((fila == 0 || fila == 12))
                        contenidoCelda = "─";
                    if ((fila == 2 || fila == 10) && (columna >= 2 && columna <= 10))
                        contenidoCelda = "─";
                    if ((fila == 4 || fila == 8) && (columna >= 4 && columna <= 8))
                        contenidoCelda = "─";
                    if ((fila == 6) && ((columna == 1) || (columna == 3) || (columna == 9) || (columna == 11)))
                        contenidoCelda = "─";
                    if ((columna == 0 || columna == 12))
                        contenidoCelda = "│";
                    if ((columna == 2 || columna == 10) && (fila >= 2 && fila <= 10))
                        contenidoCelda = "│";
                    if ((columna == 4 || columna == 8) && (fila >= 4 && fila <= 8))
                        contenidoCelda = "│";
                    if ((columna == 6) && ((fila == 1) || (fila == 3) || (fila == 9) || (fila == 11)))
                        contenidoCelda = "│";
                }
                System.out.print(" " + formatoCelda(contenidoCelda) + " ");
            }
            if (fila % 2 == 0) {
                System.out.print("  " + formatoCelda(numeroFila(filfin)));
                filfin++;
                System.out.print("  ");
            } else {
                System.out.print("     ");
            }
            System.out.println();
        }
        System.out.print("     ");

        for (int columna = 0; columna < 13; columna++) {
            if(columna != 12){
                if (columna % 2 == 0) {
                    System.out.print(" " + formatoCelda(letraColumna(colfin)) + "    ");
                    colfin++;
                }
            } else {
                System.out.print(" " + formatoCelda(letraColumna(colfin) + "     "));
            }
        }
        System.out.print(" ");
        System.out.println("\n");
    }

    @Override
    public void mostrarTurno(String mensaje) {
        System.out.println("Turno de: " +mensaje);
    }

    @Override
    public void mostrarMensajeErrorCasilla() {
        System.out.println("Ha introducido una coordenada incorrecta. Vuelva a intentar.");
    }

    @Override
    public Coordenada pedirCasilla() {
        char columna = EntradaTeclado.pedirColumna();
        int fila = EntradaTeclado.pedirFila();
        return new Coordenada(fila, columna);
    }

    @Override
    public void iniciarJuego() {
        controlador.comenzarJuego();
    }

    @Override
    public void actualizar() {
        //limpiarConsola();
        mostrarTablero(controlador.obtenerTablero());
    }
}