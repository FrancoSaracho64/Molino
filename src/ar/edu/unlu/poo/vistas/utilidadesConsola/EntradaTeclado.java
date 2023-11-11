package ar.edu.unlu.poo.vistas.utilidadesConsola;

import java.util.InputMismatchException;
import java.util.Scanner;

public class EntradaTeclado {
    public static int pedirIntOpcion(){
        //Funcion para pedir la opcion deseada por teclado
        //Inicializaciones
        int numero = 0;
        boolean entrada;
        Scanner teclado = new Scanner(System.in);
        //Funcion
        do {
            try {
                System.out.print("Opción:  ");
                entrada = true;
                numero = teclado.nextInt();
            }catch (InputMismatchException ex){
                teclado.nextLine();
                entrada = false;
                System.out.println("\n***********************************************");
                System.out.println("Debe introducir un numero.");
                System.out.println("Intente otra vez.");
                System.out.println("***********************************************\n");
            }
        }while (!entrada);
        return numero;
    }

    public static int pedirFila(){
        //Inicializaciones
        int numero = 0;
        boolean entrada;
        Scanner teclado = new Scanner(System.in);
        do {
            try {
                entrada = true;
                numero = teclado.nextInt();
            }catch (InputMismatchException ex){
                teclado.nextLine();
                entrada = false;
                System.out.println("\n**********************************");
                System.out.println("---- Debe introducir una Fila ----");
                System.out.println("         Intente otra vez.");
                System.out.println("**********************************\n");
            }
        }while (!entrada);
        return numero;
    }

    public static char pedirColumna(){
        Scanner scanner = new Scanner(System.in);
        char caracter = 0;
        String entradaUsuario;
        do {
            System.out.println("Ingrese un solo carácter:");

            // Lee la entrada del usuario
            entradaUsuario = scanner.nextLine();
            entradaUsuario.toUpperCase();
            // Validación: Verifica si la entrada es un solo carácter
            if (entradaUsuario.length() == 1) {
                caracter = entradaUsuario.charAt(0);
            } else {
                System.out.println("\n*************************************");
                System.out.println("---- Debe introducir una Columna ----");
                System.out.println("          Intente otra vez.");
                System.out.println("***********************************\n");
            }
        } while (entradaUsuario.length() != 1);
        return caracter;
    }

    public static int pedirIntID(){
        //Funcion para pedir la opcion deseada por teclado
        //Inicializaciones
        int numero = 0;
        boolean entrada;
        Scanner teclado = new Scanner(System.in);
        //Funcion
        do {
            try {
                System.out.print("     ID_JUGADOR:   ");
                entrada = true;
                numero = teclado.nextInt();
            }catch (InputMismatchException ex){
                teclado.nextLine();
                entrada = false;
                System.out.println("\n***********************************************");
                System.out.println("Debe introducir un numero.");
                System.out.println("Intente otra vez.");
                System.out.println("***********************************************\n");
            }
        }while (!entrada);
        return numero;
    }

    public static void presionarEnterParaContinuar(){
        Scanner teclado = new Scanner(System.in);
        System.out.print("Presione ENTER para continuar");
        teclado.nextLine();
    }
}
