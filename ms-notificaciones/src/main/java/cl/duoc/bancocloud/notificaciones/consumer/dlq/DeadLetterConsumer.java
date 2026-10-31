package cl.duoc.bancocloud.notificaciones.consumer.dlq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;

/**
 * Escucha las DLQ y REGISTRA en logs cada mensaje no entregado.
 * Se confirma automáticamente (ACK) para que la DLQ no se llene de mensajes ya registrados.
 */
@Component
public class DeadLetterConsumer {

    private static final Logger log = LoggerFactory.getLogger(DeadLetterConsumer.class);

    @RabbitListener(queues = {
            "${app.messaging.queues.cheque-emitido.dlq}",
            "${app.messaging.queues.cheque-estado.dlq}",
            "${app.messaging.queues.cheque-devuelto.dlq}",
            "${app.messaging.queues.acceso.dlq}"
    }, ackMode = "AUTO")
    public void registrarMensajeMuerto(Message message) {
        String origen = message.getMessageProperties().getConsumerQueue();
        Object xDeath = message.getMessageProperties().getHeaders().get("x-death");
        String cuerpo = new String(message.getBody(), StandardCharsets.UTF_8);
        log.error("MENSAJE EN DLQ [{}] cuerpo={} x-death={}", origen, cuerpo, xDeath);
    }
}
