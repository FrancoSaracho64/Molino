package ar.edu.unlu.poo.vistas;

import ar.edu.unlu.poo.enumerados.EstadoCasilla;
import ar.edu.unlu.poo.enumerados.Simbolo;
import ar.edu.unlu.poo.interfaces.TableroImpl;
import ar.edu.unlu.poo.modelos.Ficha;
import ar.edu.unlu.poo.modelos.Tablero;

public class VistaConsola implements TableroImpl {
    public VistaConsola() {

    }

    private int pedirOpcion (){
        return 0;
    }

    private int pedirColumna(){
        return 0;
    }

    private int pedirFila(){
        return 0;
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

    public void limpiarConsola(){
        try{
            ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", "cls");
            Process process = processBuilder.inheritIO().start();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mostrarTablero(Tablero tablero) {
        int col = 0;
        int fil = 0;
        int filfin = 0;
        int colfin = 0;
        System.out.println("-------------------------------------------------");
        // Muestra las letras de las columnas
        System.out.print("|    "); // Espacio inicial
        for (int columna = 0; columna < tablero.getCountColumnas(); columna++) {
            if ( columna!= 12) {
                if (columna % 2 == 0) {
                    System.out.print(" " + formatoCelda(letraColumna(col)) + "    ");
                    col++;
                }
            } else {
                System.out.print(" " + formatoCelda(letraColumna(col) + "     |"));
            }
        }
        System.out.println();
        for (int fila = 0; fila < tablero.getCountFilas(); fila++) {
            System.out.print("|");
            if (fila % 2 == 0) {
                System.out.print(" " + formatoCelda(numeroFila(fil)) + "  "); // Número de fila
                fil++;
            }
            for (int columna = 0; columna < tablero.getCountColumnas(); columna++) {
                Ficha ficha = tablero.obtenerFicha(fila, columna);
                String contenidoCelda = "";
                if (ficha == null) {
                    if (tablero.obtenerCasilla(fila, columna).getEstadoCasilla() != EstadoCasilla.INVALIDA){
                        contenidoCelda = "#";
                    }
                } else {
                    if (ficha.getSimbolo() == Simbolo.X) {
                        contenidoCelda = "X";
                    } else if (ficha.getSimbolo() == Simbolo.O){
                        contenidoCelda = "O";
                    }
                }
                System.out.print(" " + formatoCelda(contenidoCelda) + " ");
            }
            if (fila % 2 == 0) {
                System.out.print("  "+formatoCelda(numeroFila(filfin)));
                filfin++;
                System.out.print(" |");
            } else {
                System.out.print("        |");
            }
            System.out.println();
        }
        System.out.print("|    ");

        for (int columna = 0; columna < tablero.getCountColumnas(); columna++) {
            if(columna != 12){
                if (columna % 2 == 0) {
                    System.out.print(" " + formatoCelda(letraColumna(colfin)) + "    ");
                    colfin++;
                }
            } else {
                System.out.print(" " + formatoCelda(letraColumna(colfin) + "     "));
            }
        }
        System.out.print("|");
        System.out.println("\n-------------------------------------------------");
    }

    @Override
    public void mostrarMensaje() {

    }
}

