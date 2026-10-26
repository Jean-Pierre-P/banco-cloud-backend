package cl.duoc.bancocloud.notificaciones.consumer.cheque;

import cl.duoc.bancocloud.notificaciones.consumer.support.AckSupport;
import cl.duoc.bancocloud.notificaciones.event.ChequeEvent;
import cl.duoc.bancocloud.notificaciones.service.NotificacionService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * Consumidores del dominio "cheques". Los nombres de colas vienen de application.yml.
 * Cada listener solo delega en el servicio y deja que AckSupport confirme el mensaje.
 */
@Component
public class ChequeConsumer {

    private final NotificacionService notificacionService;

    public ChequeConsumer(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @RabbitListener(queues = "${app.messaging.queues.cheque-emitido.name}", ackMode = "MANUAL")
    public void onChequeEmitido(ChequeEvent event, Channel channel,
                                @Header(AmqpHeaders.DELIVERY_TAG) long tag,
                                @Header(AmqpHeaders.REDELIVERED) boolean redelivered) throws IOException {
        AckSupport.procesar(channel, tag, redelivered, "cheque-emitido",
                () -> notificacionService.notificarEmision(event));
    }

    @RabbitListener(queues = "${app.messaging.queues.cheque-estado.name}", ackMode = "MANUAL")
    public void onEstadoCambiado(ChequeEvent event, Channel channel,
                                 @Header(AmqpHeaders.DELIVERY_TAG) long tag,
                                 @Header(AmqpHeaders.REDELIVERED) boolean redelivered) throws IOException {
        AckSupport.procesar(channel, tag, redelivered, "cheque-estado",
                () -> notificacionService.notificarCambioEstado(event));
    }

    @RabbitListener(queues = "${app.messaging.queues.cheque-devuelto.name}", ackMode = "MANUAL")
    public void onChequeDevuelto(ChequeEvent event, Channel channel,
                                 @Header(AmqpHeaders.DELIVERY_TAG) long tag,
                                 @Header(AmqpHeaders.REDELIVERED) boolean redelivered) throws IOException {
        AckSupport.procesar(channel, tag, redelivered, "cheque-devuelto",
                () -> notificacionService.notificarDevolucion(event));
    }
}
