package ar.edu.unlu.poo.mensajes;

import java.util.List;
import java.util.Random;

public class MensajesPerdedor {
    private static final List<String> MENSAJES_ANIMO = List.of(
            "No te desanimes, cada partida es una nueva oportunidad para brillar.",
            "¡Estuvo muy cerca! La próxima vez seguro que la victoria será tuya.",
            "Recuerda, en el juego y en la vida, lo importante es disfrutar el camino.",
            "A veces se gana, a veces se aprende. ¡Ánimo para la próxima!",
            "La derrota de hoy es la semilla para la victoria de mañana.",
            "No es la victoria lo que define al jugador, sino el coraje de continuar.",
            "¡Qué partida tan emocionante! Estoy seguro de que la próxima vez tendrás tu revancha.",
            "Los grandes campeones también enfrentaron derrotas. ¡No te rindas!",
            "El verdadero triunfo es levantarse y seguir jugando. ¡Te esperamos en la próxima partida!",
            "¡No hay nada que una buena revancha no pueda arreglar! ¿Listo para la próxima?",
            "El esfuerzo y la perseverancia siempre se recompensan. ¡Sigue adelante!",
            "Cada juego es una lección. ¡Aprovecha lo aprendido para tu próxima victoria!",
            "La grandeza no se mide por las victorias, sino por cómo te levantas después de caer.",
            "Aunque hoy no hayas ganado, tu espíritu competitivo es digno de admiración.",
            "Recuerda que incluso los más grandes maestros han enfrentado derrotas.",
            "Tu determinación es tu mayor activo. ¡No dejes que una derrota te desanime!",
            "A veces, un paso atrás es necesario para tomar impulso. ¡La próxima será la tuya!",
            "La resiliencia es clave en cualquier competición. ¡Tu momento llegará!",
            "No es el fin, es solo el comienzo de tu camino hacia la victoria.",
            "La verdadera batalla es contra uno mismo. Hoy has ganado experiencia invaluable."
    );

    public static String getMensajeDeAnimo() {
        Random random = new Random();
        int indiceMensaje = random.nextInt(MENSAJES_ANIMO.size());
        return MENSAJES_ANIMO.get(indiceMensaje);
    }
}
