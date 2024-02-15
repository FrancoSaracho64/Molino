package ar.edu.unlu.poo.mensajes;

import java.util.List;
import java.util.Random;

public class MensajesGanador {
    private static final List<String> MENSAJES_FELICITACION = List.of(
            "¡Impresionante victoria! Eres un campeón.",
            "¡Qué talento! Has jugado magistralmente.",
            "¡Eres el maestro del juego! Felicitaciones.",
            "¡Victoria aplastante! Has demostrado ser el mejor.",
            "Triunfo absoluto. Tu habilidad es indiscutible.",
            "¡Dominaste el juego como un verdadero maestro! Felicidades.",
            "¡Qué estratega! Tu victoria es bien merecida.",
            "Sencillamente impresionante. ¡Una victoria espectacular!",
            "Tu talento para el juego brilla con luz propia. ¡Felicidades!",
            "¡Inigualable! Tu victoria quedará en la historia.",
            "Eres un genio estratégico. ¡Felicidades por tu gran victoria!",
            "¡Increíble juego! Tu victoria es el resultado de tu dedicación.",
            "Has jugado con maestría. ¡Enhorabuena por tu merecida victoria!",
            "¡Sublime! Tu juego hoy fue una obra de arte.",
            "¡Invencible! Nadie pudo detenerte en tu camino a la victoria.",
            "Has demostrado ser un adversario formidable. ¡Felicidades!",
            "¡Espectacular! Tu victoria hoy fue un verdadero espectáculo.",
            "¡Excepcional! Tu habilidad y estrategia son de otro nivel.",
            "¡Triunfo legendario! Te has consagrado como un verdadero campeón.",
            "¡Asombroso! Tu victoria hoy ha sido una demostración de pura habilidad."
    );

    public static String getMensajeFelicitacion() {
        Random random = new Random();
        int indiceMensaje = random.nextInt(MENSAJES_FELICITACION.size());
        return MENSAJES_FELICITACION.get(indiceMensaje);
    }
}
