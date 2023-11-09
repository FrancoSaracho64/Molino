package ar.edu.unlu.poo.tipoJuego;

import ar.edu.unlu.poo.enumerados.EstadoCasilla;
import ar.edu.unlu.poo.enumerados.Simbolo;
import ar.edu.unlu.poo.modelos.Ficha;
import ar.edu.unlu.poo.modelos.Jugador;
import ar.edu.unlu.poo.modelos.Tablero;
import ar.edu.unlu.poo.reglas.ReglasDelJuego;

public class JuegoConsola {
    public JuegoConsola() {
        ReglasDelJuego reglas = new ReglasDelJuego();
        Jugador jugador_blancas = new Jugador("Jugador 1");
        Jugador jugador_negras = new Jugador("Jugador 2");
        Tablero molino = new Tablero(13, 13);

        Ficha blanca1 = new Ficha();
        Ficha blanca2 = new Ficha();
        Ficha blanca3 = new Ficha();
        Ficha blanca4 = new Ficha();
        Ficha blanca5 = new Ficha();
        Ficha blanca6 = new Ficha();
        Ficha blanca7 = new Ficha();
        Ficha blanca8 = new Ficha();
        Ficha blanca9 = new Ficha();

        Ficha negra1 = new Ficha();
        Ficha negra2 = new Ficha();
        Ficha negra3 = new Ficha();
        Ficha negra4 = new Ficha();
        Ficha negra5 = new Ficha();
        Ficha negra6 = new Ficha();
        Ficha negra7 = new Ficha();
        Ficha negra8 = new Ficha();
        Ficha negra9 = new Ficha();

        mostrarTablero(molino);
    }

    private void mostrarTablero (Tablero tablero){
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

    private String letraColumna (int columna){
        return String.valueOf((char) ('A' + columna));
    }

    private String numeroFila (int fila){
        return String.valueOf(fila + 1);
    }

    private String formatoCelda (String contenido){
        return String.format("%1s", contenido);
    }
}

