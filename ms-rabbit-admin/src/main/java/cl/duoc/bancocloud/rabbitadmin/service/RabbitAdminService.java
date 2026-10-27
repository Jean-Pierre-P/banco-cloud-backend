package cl.duoc.bancocloud.rabbitadmin.service;

import cl.duoc.bancocloud.rabbitadmin.dto.BindingRequest;
import cl.duoc.bancocloud.rabbitadmin.dto.ExchangeRequest;
import cl.duoc.bancocloud.rabbitadmin.dto.QueueRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.stereotype.Service;

@Service
public class RabbitAdminService {

    // Logger nativo que reemplaza a @Slf4j
    private static final Logger log = LoggerFactory.getLogger(RabbitAdminService.class);

    private final AmqpAdmin amqpAdmin;

    // Constructor manual que reemplaza a @RequiredArgsConstructor
    public RabbitAdminService(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }

    public String crearCola(QueueRequest request) {
        Queue queue = new Queue(request.getName(), request.isDurable(), false, false);
        String resultName = amqpAdmin.declareQueue(queue);
        log.info("Cola creada exitosamente en RabbitMQ: {}", resultName);
        return resultName;
    }

    public void eliminarCola(String nombreCola) {
        boolean eliminada = amqpAdmin.deleteQueue(nombreCola);
        if (!eliminada) {
            throw new IllegalArgumentException("No se pudo eliminar la cola o no existe: " + nombreCola);
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
            throw new IllegalArgumentException("No se pudo eliminar el exchange o no existe: " + nombreExchange);
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