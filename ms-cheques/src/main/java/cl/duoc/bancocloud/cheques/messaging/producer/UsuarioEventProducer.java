package cl.duoc.bancocloud.cheques.messaging.producer;

import cl.duoc.bancocloud.cheques.messaging.config.MessagingProperties;
import cl.duoc.bancocloud.cheques.messaging.event.UsuarioAutorizadoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class UsuarioEventProducer {
    
    private static final Logger log = LoggerFactory.getLogger(UsuarioEventProducer.class);
    
    private final RabbitTemplate rabbitTemplate;
    private final MessagingProperties properties;

    public UsuarioEventProducer(RabbitTemplate rabbitTemplate, MessagingProperties properties) {
        this.rabbitTemplate = rabbitTemplate;
        this.properties = properties;
    }

    public void enviarEventoAutorizacion(UsuarioAutorizadoEvent evento) {
        log.info("Publicando evento de autorización para usuario: {}", evento.getEmail());
        rabbitTemplate.convertAndSend(
                properties.getExchanges().getPrincipal(),
                properties.getRoutingKeys().getAcceso(),
                evento
        );
    }
}