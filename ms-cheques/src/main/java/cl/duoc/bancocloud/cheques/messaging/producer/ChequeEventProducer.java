package cl.duoc.bancocloud.cheques.messaging.producer;

import cl.duoc.bancocloud.cheques.messaging.config.MessagingProperties;
import cl.duoc.bancocloud.cheques.messaging.event.ChequeEvent;
import cl.duoc.bancocloud.cheques.model.Cheque;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ChequeEventProducer {

    private static final Logger log = LoggerFactory.getLogger(ChequeEventProducer.class);

    private final RabbitTemplate rabbitTemplate;
    private final MessagingProperties props;

    public ChequeEventProducer(RabbitTemplate rabbitTemplate, MessagingProperties props) {
        this.rabbitTemplate = rabbitTemplate;
        this.props = props;
    }

    public void publicarEmitido(Cheque c) {
        enviar(props.routingKeys().chequeEmitido(), evento(c, "EMITIDO"));
    }

    public void publicarEstadoCambiado(Cheque c) {
        enviar(props.routingKeys().chequeEstado(), evento(c, "ESTADO_CAMBIADO"));
    }

    public void publicarDevuelto(Cheque c) {
        enviar(props.routingKeys().chequeDevuelto(), evento(c, "DEVUELTO"));
    }

    private ChequeEvent evento(Cheque c, String tipo) {
        return new ChequeEvent(c.getId(), c.getClienteId(), c.getBeneficiario(), c.getMonto(),
                c.getFecha(), c.getEstado().name(), c.getMotivoDevolucion(), tipo);
    }

    private void enviar(String routingKey, ChequeEvent event) {
        rabbitTemplate.convertAndSend(props.exchange(), routingKey, event);
        log.info("Evento {} publicado (cheque {}) con routing key {}", event.tipoEvento(), event.chequeId(), routingKey);
    }
}
