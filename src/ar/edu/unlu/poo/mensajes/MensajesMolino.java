package ar.edu.unlu.poo.mensajes;

import java.util.List;
import java.util.Random;

public class MensajesMolino {
    private static final List<String> MENSAJES_FELICITACION = List.of(
            "¡Increíble! Has formado un molino.",
            "¡Felicidades! Un molino perfectamente alineado.",
            "¡Gran estrategia! Molino completado.",
            "¡Molino formado! Bien jugado.",
            "¡Excelente movimiento! Has formado un molino.",
            "¡Impresionante! Eso es un molino.",
            "¡Qué habilidad! Has conseguido un molino.",
            "¡Molino exitoso! ¡Buen trabajo!",
            "¡Eso es! Un molino impecable.",
            "¡Fantástico! Tienes un molino.",
            "¡Bravo! Un molino magistralmente formado.",
            "¡Molino logrado! Eres un estratega.",
            "¡Exactamente! Has armado un molino.",
            "¡Magnífico! Un molino bien merecido.",
            "¡Asombroso! Molino conseguido.",
            "¡Sorprendente! Un molino formado con maestría.",
            "¡Molino perfecto! Tienes una estrategia envidiable.",
            "¡Qué jugada! Un molino brillante.",
            "¡Molino completado! Sigues impresionando.",
            "¡Un molino más para la colección! Felicidades."
    );

    public static String getMensajeFelicitacion() {
        Random random = new Random();
        int indiceMensaje = random.nextInt(MENSAJES_FELICITACION.size());
        return MENSAJES_FELICITACION.get(indiceMensaje);
    }
}
