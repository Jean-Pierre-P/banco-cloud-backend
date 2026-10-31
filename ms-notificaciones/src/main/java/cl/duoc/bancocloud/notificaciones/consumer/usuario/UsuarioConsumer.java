package cl.duoc.bancocloud.notificaciones.consumer.usuario;

import cl.duoc.bancocloud.notificaciones.event.UsuarioAutorizadoEvent;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UsuarioConsumer {

    private static final Logger log = LoggerFactory.getLogger(UsuarioConsumer.class);

    @RabbitListener(queues = "${app.messaging.queues.acceso.name}")
    public void consumirAutorizacion(
            UsuarioAutorizadoEvent evento,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            if (evento.getEmail() == null || evento.getEmail().isEmpty()) {
                log.error("[NACK] Email inválido, enviando a DLQ.");
                channel.basicNack(deliveryTag, false, false);
                return;
            }

            log.info("Simulando envío de correo de bienvenida a: {}", evento.getEmail());
            channel.basicAck(deliveryTag, false);
            log.info("[ACK] Notificación de acceso procesada exitosamente.");

        } catch (Exception e) {
            log.error("[NACK] Error procesando notificación, enviando a DLQ.", e);
            channel.basicNack(deliveryTag, false, false);
        }
    }
}