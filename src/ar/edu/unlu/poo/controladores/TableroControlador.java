package ar.edu.unlu.poo.controladores;

import ar.edu.unlu.poo.interfaces.TableroImpl;
import ar.edu.unlu.poo.modelos.Ficha;
import ar.edu.unlu.poo.modelos.Jugador;
import ar.edu.unlu.poo.modelos.Participante;
import ar.edu.unlu.poo.modelos.Tablero;
import ar.edu.unlu.poo.reglas.ReglasDelJuego;

import java.util.ArrayList;

public class TableroControlador {
    private ArrayList<Jugador> jugadores;
    private Tablero tablero;
    private TableroImpl vista;
    private ReglasDelJuego reglas;

    public TableroControlador(ArrayList<Jugador> jugadores, Tablero tablero, TableroImpl vista){
        this.jugadores = jugadores;
        this.tablero = tablero;
        this.vista = vista;
        this.reglas = new ReglasDelJuego();
    }

    public void iniciarPartida(){
        ArrayList<Ficha> fichasJ1 = generarFichas();
        ArrayList<Ficha> fichasJ2 = generarFichas();

        Participante j1 = new Participante(jugadores.get(0), fichasJ1);
        Participante j2 = new Participante(jugadores.get(1), fichasJ2);

        boolean juegoActivo = true;
        boolean turno_activo = true; // true: jugador1 --- false: jugador2
        boolean ganador;

        while (juegoActivo){
            if(turno_activo){
                if(j1.getFichasColocadas() < 9){
                    juegoActivo = false;
                }

                turno_activo = false;
                juegoActivo = reglas.verificarPartidaFinalizada(tablero, j1, j2);
            } else {
                if(j2.getFichasColocadas() < 9){

                }
                juegoActivo = false;
                turno_activo = true;
            }
        }
        System.out.println("El juego ha terminado.");
        System.out.println("El ganador es: ");
        vista.mostrarTablero(tablero);
    }

    private ArrayList<Ficha> generarFichas(){
        ArrayList<Ficha> fichas = new ArrayList<>();
        Ficha f1 = new Ficha();
        Ficha f2 = new Ficha();
        Ficha f3 = new Ficha();
        Ficha f4 = new Ficha();
        Ficha f5 = new Ficha();
        Ficha f6 = new Ficha();
        Ficha f7 = new Ficha();
        Ficha f8 = new Ficha();
        Ficha f9 = new Ficha();
        fichas.add(f1);
        fichas.add(f2);
        fichas.add(f3);
        fichas.add(f4);
        fichas.add(f5);
        fichas.add(f6);
        fichas.add(f7);
        fichas.add(f8);
        fichas.add(f9);

        return fichas;
    }
}
