package ar.edu.unlu.poo.vistas;

import ar.edu.unlu.poo.enumerados.EstadoCasilla;
import ar.edu.unlu.poo.enumerados.Simbolo;
import ar.edu.unlu.poo.interfaces.ControladorImpl;
import ar.edu.unlu.poo.interfaces.TableroImpl;
import ar.edu.unlu.poo.modelos.Coordenada;
import ar.edu.unlu.poo.modelos.Ficha;
import ar.edu.unlu.poo.modelos.Tablero;
import ar.edu.unlu.poo.vistas.utilidadesConsola.EntradaTeclado;

import java.util.Scanner;

public class VistaConsola implements TableroImpl {
    private static final String CASILLA_DISPONIBLE = "#";
    private static final String CASILLA_INVALIDA = "";
    private ControladorImpl controlador;

    public VistaConsola(ControladorImpl controlador){
        this.controlador = controlador;
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
        System.out.println("-------------------------------------------------");
        // Muestra las letras de las columnas
        System.out.print("|    "); // Espacio inicial
        for (int columna = 0; columna < 13; columna++) {
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
        for (int fila = 0; fila < 13; fila++) {
            System.out.print("|");
            if (fila % 2 == 0) {
                System.out.print(" " + formatoCelda(numeroFila(fil)) + "  "); // Número de fila
                fil++;
            }
            for (int columna = 0; columna < 13; columna++) {
                // TODO: pedir al controlador ----------------------
                Ficha ficha = tablero.obtenerFicha(fila, columna);
                String contenidoCelda = CASILLA_INVALIDA;
                if (ficha == null) {
                    if (tablero.obtenerCasilla(fila, columna).getEstadoCasilla() != EstadoCasilla.INVALIDA){
                        contenidoCelda = CASILLA_DISPONIBLE;
                    }

                } else {
                    if (ficha.getSimbolo() == Simbolo.X) {
                        contenidoCelda = "X";
                    } else if (ficha.getSimbolo() == Simbolo.O){
                        contenidoCelda = "O";
                    }
                }
                // TODO---------------------------------------------
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
        System.out.print("|");
        System.out.println("\n-------------------------------------------------");
    }

    @Override
    public void mostrarMensaje(String mensaje) {

    }

    @Override
    public void mostrarMensajeErrorCasilla() {
        System.out.println("Ha introducido una coordenada incorrecta. Vuelva a intentar.");
    }

    @Override
    public Coordenada pedirCasilla() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese la columna (A-G): ");
        char columna = EntradaTeclado.pedirColumna();
        System.out.print("Ingrese la fila (1-7): ");
        int fila = EntradaTeclado.pedirFila();
        return new Coordenada(fila, columna);
    }

    @Override
    public void iniciarJuego() {
        controlador.comenzarJuego();
    }
}