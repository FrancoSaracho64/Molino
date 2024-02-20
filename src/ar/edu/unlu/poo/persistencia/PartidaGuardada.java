package ar.edu.unlu.poo.persistencia;

import ar.edu.unlu.poo.modelos.Molino;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PartidaGuardada implements Serializable {
    private final Molino modelo;
    private final String jugador1;
    private final String jugador2;
    private final LocalDateTime fechaGuardada;

    public PartidaGuardada(Molino modelo, String jugador1, String jugador2, LocalDateTime fechaGuardada) {
        this.modelo = modelo;
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.fechaGuardada = fechaGuardada;
    }

    public Molino getModelo() {
        return modelo;
    }

    @Override
    public String toString() {
        return " PartidaGuardada ---> (J1) " + jugador1 + " vs. " + jugador2 + " (J2)  ---->>  GUARDADA EL :   " + formatearFecha();
    }

    private String formatearFecha() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return fechaGuardada.format(formatter);
    }
}
