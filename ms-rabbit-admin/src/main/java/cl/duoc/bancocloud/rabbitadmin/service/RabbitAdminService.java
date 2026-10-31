package cl.duoc.bancocloud.rabbitadmin.service;

import cl.duoc.bancocloud.rabbitadmin.dto.BindingRequest;
import cl.duoc.bancocloud.rabbitadmin.dto.ExchangeRequest;
import cl.duoc.bancocloud.rabbitadmin.dto.QueueRequest;
import cl.duoc.bancocloud.rabbitadmin.exception.RecursoNoEncontradoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Encapsula toda la administración de colas, exchanges y bindings de RabbitMQ.
 * Los controladores solo llaman a estos métodos y no conocen la librería de RabbitMQ.
 */
@Service
public class RabbitAdminService {

    private static final Logger log = LoggerFactory.getLogger(RabbitAdminService.class);

    private final AmqpAdmin amqpAdmin;

    public RabbitAdminService(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }

    public String crearCola(QueueRequest request) {
        boolean tieneExchange = request.getExchange() != null && !request.getExchange().isBlank();
        boolean tieneRoutingKey = request.getRoutingKey() != null && !request.getRoutingKey().isBlank();

        if (tieneRoutingKey && !tieneExchange) {
            throw new IllegalArgumentException("Se indicó routingKey sin exchange");
        }

        Map<String, Object> args = null;
        if (request.getDeadLetterExchange() != null && !request.getDeadLetterExchange().isBlank()) {
            args = new HashMap<>();
            args.put("x-dead-letter-exchange", request.getDeadLetterExchange());
        }

        Queue queue = new Queue(request.getName(), request.isDurable(), false, request.isAutoDelete(), args);
        String resultName = amqpAdmin.declareQueue(queue);
        log.info("Cola creada exitosamente en RabbitMQ: {}", resultName);

        if (tieneExchange && tieneRoutingKey) {
            amqpAdmin.declareBinding(new Binding(
                    request.getName(), Binding.DestinationType.QUEUE,
                    request.getExchange(), request.getRoutingKey(), null));
            log.info("Binding creado: exchange [{}] -> cola [{}] con routingKey [{}]",
                    request.getExchange(), request.getName(), request.getRoutingKey());
        }
        return resultName;
    }

    public void eliminarCola(String nombreCola) {
        boolean eliminada = amqpAdmin.deleteQueue(nombreCola);
        if (!eliminada) {
            throw new RecursoNoEncontradoException("La cola no existe: " + nombreCola);
        }
        log.info("Cola eliminada de RabbitMQ: {}", nombreCola);
    }

    public void crearExchange(ExchangeRequest request) {
        Exchange exchange;
        switch (request.getType().toLowerCase()) {
            case "direct":
                exchange = new DirectExchange(request.getName(), request.isDurable(), false);
                break;
            case "topic":
                exchange = new TopicExchange(request.getName(), request.isDurable(), false);
                break;
            case "fanout":
                exchange = new FanoutExchange(request.getName(), request.isDurable(), false);
                break;
            case "headers":
                exchange = new HeadersExchange(request.getName(), request.isDurable(), false);
                break;
            default:
                throw new IllegalArgumentException("Tipo de exchange no soportado: " + request.getType());
        }
        amqpAdmin.declareExchange(exchange);
        log.info("Exchange [{}] de tipo [{}] creado exitosamente.", request.getName(), request.getType());
    }

    public void eliminarExchange(String nombreExchange) {
        boolean eliminado = amqpAdmin.deleteExchange(nombreExchange);
        if (!eliminado) {
            throw new RecursoNoEncontradoException("El exchange no existe: " + nombreExchange);
        }
        log.info("Exchange eliminado de RabbitMQ: {}", nombreExchange);
    }

    public void crearBinding(BindingRequest request) {
        Binding binding = new Binding(
                request.getQueueName(),
                Binding.DestinationType.QUEUE,
                request.getExchangeName(),
                request.getRoutingKey(),
                null
        );
        amqpAdmin.declareBinding(binding);
        log.info("Binding creado entre Exchange [{}] y Cola [{}] con RoutingKey [{}]",
                request.getExchangeName(), request.getQueueName(), request.getRoutingKey());
    }
}