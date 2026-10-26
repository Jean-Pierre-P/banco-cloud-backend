package cl.duoc.bancocloud.rabbitadmin.service;

import cl.duoc.bancocloud.rabbitadmin.dto.*;
import cl.duoc.bancocloud.rabbitadmin.exception.RecursoNoEncontradoException;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.QueueInformation;
import org.springframework.stereotype.Service;

/**
 * Encapsula TODA la interacción con RabbitMQ. Los controladores solo llaman a estos métodos
 * y no conocen la librería de RabbitMQ.
 */
@Service
public class RabbitAdminService {

    private final AmqpAdmin amqpAdmin;

    public RabbitAdminService(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }

    // ---------- Colas ----------
    public void crearCola(QueueRequest req) {
        boolean tieneDlx = req.deadLetterExchange() != null && !req.deadLetterExchange().isBlank();
        boolean tieneDlk = req.deadLetterRoutingKey() != null && !req.deadLetterRoutingKey().isBlank();
        if (tieneDlk && !tieneDlx) {
            throw new IllegalArgumentException("Si indica deadLetterRoutingKey debe indicar también deadLetterExchange");
        }

        boolean durable = req.durable() == null || req.durable();
        QueueBuilder builder = durable ? QueueBuilder.durable(req.name()) : QueueBuilder.nonDurable(req.name());
        if (Boolean.TRUE.equals(req.autoDelete())) {
            builder.autoDelete();
        }
        if (tieneDlx) {
            builder.deadLetterExchange(req.deadLetterExchange());
        }
        if (tieneDlk) {
            builder.deadLetterRoutingKey(req.deadLetterRoutingKey());
        }
        if (req.messageTtlMs() != null) {
            builder.ttl(req.messageTtlMs().intValue());
        }
        Queue queue = builder.build();
        amqpAdmin.declareQueue(queue);
    }

    public QueueInfoResponse obtenerCola(String name) {
        QueueInformation info = amqpAdmin.getQueueInfo(name);
        if (info == null) {
            throw new RecursoNoEncontradoException("No existe la cola '" + name + "'");
        }
        return new QueueInfoResponse(info.getName(), info.getMessageCount(), info.getConsumerCount());
    }

    public void eliminarCola(String name) {
        if (!amqpAdmin.deleteQueue(name)) {
            throw new RecursoNoEncontradoException("No existe la cola '" + name + "'");
        }
    }

    // ---------- Exchanges ----------
    public void crearExchange(ExchangeRequest req) {
        ExchangeBuilder builder = switch (req.type()) {
            case DIRECT -> ExchangeBuilder.directExchange(req.name());
            case TOPIC -> ExchangeBuilder.topicExchange(req.name());
            case FANOUT -> ExchangeBuilder.fanoutExchange(req.name());
        };
        builder.durable(req.durable() == null || req.durable());
        if (Boolean.TRUE.equals(req.autoDelete())) {
            builder.autoDelete();
        }
        amqpAdmin.declareExchange(builder.build());
    }

    public void eliminarExchange(String name) {
        if (!amqpAdmin.deleteExchange(name)) {
            throw new RecursoNoEncontradoException("No existe el exchange '" + name + "'");
        }
    }

    // ---------- Bindings ----------
    public void crearBinding(BindingRequest req) {
        amqpAdmin.declareBinding(construir(req.queue(), req.exchange(), req.routingKey()));
    }

    public void eliminarBinding(String queue, String exchange, String routingKey) {
        if (queue == null || queue.isBlank() || exchange == null || exchange.isBlank()) {
            throw new IllegalArgumentException("Los parámetros 'queue' y 'exchange' son obligatorios");
        }
        amqpAdmin.removeBinding(construir(queue, exchange, routingKey));
    }

    private Binding construir(String queue, String exchange, String routingKey) {
        return new Binding(queue, Binding.DestinationType.QUEUE, exchange,
                routingKey == null ? "" : routingKey, null);
    }
}
