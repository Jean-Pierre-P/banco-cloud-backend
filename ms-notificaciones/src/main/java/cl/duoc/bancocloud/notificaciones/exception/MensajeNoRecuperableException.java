package cl.duoc.bancocloud.notificaciones.exception;

/** Error que NO se arregla reintentando (mensaje inválido): va directo a la DLQ. */
public class MensajeNoRecuperableException extends RuntimeException {
    public MensajeNoRecuperableException(String message) {
        super(message);
    }
}
