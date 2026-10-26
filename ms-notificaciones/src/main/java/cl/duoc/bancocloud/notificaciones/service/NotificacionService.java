package cl.duoc.bancocloud.notificaciones.service;

import cl.duoc.bancocloud.notificaciones.event.ChequeEvent;
import cl.duoc.bancocloud.notificaciones.exception.MensajeNoRecuperableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/** Lógica de negocio de las notificaciones. No sabe nada de RabbitMQ. */
@Service
public class NotificacionService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionService.class);

    public void notificarEmision(ChequeEvent e) {
        validar(e);
        enviar(e.clienteId(), "Su cheque #" + e.chequeId() + " por $" + e.monto()
                + " a nombre de " + e.beneficiario() + " fue emitido.");
    }

    public void notificarCambioEstado(ChequeEvent e) {
        validar(e);
        enviar(e.clienteId(), "Su cheque #" + e.chequeId() + " cambió a estado " + e.estado() + ".");
    }

    public void notificarDevolucion(ChequeEvent e) {
        validar(e);
        enviar(e.clienteId(), "Su cheque #" + e.chequeId() + " fue DEVUELTO. Motivo: " + e.motivo());
    }

    private void validar(ChequeEvent e) {
        if (e == null || e.chequeId() == null || e.clienteId() == null) {
            throw new MensajeNoRecuperableException("Evento inválido: faltan chequeId o clienteId");
        }
        if (e.monto() == null || e.monto().signum() <= 0) {
            throw new MensajeNoRecuperableException("Evento inválido: monto debe ser mayor a 0");
        }
    }

    /** Simulación: aquí iría el envío real de correo / SMS / push. */
    private void enviar(String destinatario, String mensaje) {
        log.info("NOTIFICACIÓN -> cliente {}: {}", destinatario, mensaje);
    }
}
