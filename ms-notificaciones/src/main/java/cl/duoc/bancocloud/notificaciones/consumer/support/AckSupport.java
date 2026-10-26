package cl.duoc.bancocloud.notificaciones.consumer.support;

import cl.duoc.bancocloud.notificaciones.exception.MensajeNoRecuperableException;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

/**
 * Política única de confirmación (ACK) para todos los consumidores:
 *
 *  - Éxito                          -> basicAck
 *  - Error NO recuperable           -> basicNack(requeue=false)  -> va a la DLQ
 *  - Error recuperable, 1.er intento -> basicNack(requeue=true)   -> se reintenta una vez
 *  - Error recuperable, reintento    -> basicNack(requeue=false)  -> va a la DLQ
 */
public final class AckSupport {

    private static final Logger log = LoggerFactory.getLogger(AckSupport.class);

    private AckSupport() {
    }

    public static void procesar(Channel channel, long deliveryTag, boolean redelivered,
                                String descripcion, Runnable accion) throws IOException {
        try {
            accion.run();
            channel.basicAck(deliveryTag, false);
        } catch (MensajeNoRecuperableException | IllegalArgumentException e) {
            log.error("[{}] Error NO recuperable, se envía a DLQ: {}", descripcion, e.getMessage());
            channel.basicNack(deliveryTag, false, false);
        } catch (Exception e) {
            if (!redelivered) {
                log.warn("[{}] Error recuperable, se reintenta una vez: {}", descripcion, e.getMessage());
                channel.basicNack(deliveryTag, false, true);
            } else {
                log.error("[{}] Falló también el reintento, se envía a DLQ: {}", descripcion, e.getMessage());
                channel.basicNack(deliveryTag, false, false);
            }
        }
    }
}
