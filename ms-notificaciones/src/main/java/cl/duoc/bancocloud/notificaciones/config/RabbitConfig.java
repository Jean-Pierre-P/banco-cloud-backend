package cl.duoc.bancocloud.notificaciones.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    private final MessagingProperties properties;

    // Constructor manual en lugar de @RequiredArgsConstructor
    public RabbitConfig(MessagingProperties properties) {
        this.properties = properties;
    }

    // Convertidor JSON para que reciba correctamente el UsuarioAutorizadoEvent y ChequeEvent
    @Bean
    @SuppressWarnings("removal")
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // --- EXCHANGES ---
    @Bean
    public DirectExchange principalExchange() {
        return new DirectExchange(properties.getExchanges().getPrincipal(), true, false);
    }

    // --- DEAD LETTER QUEUE (DLQ) ---
    @Bean
    public Queue dlqQueue() {
        return QueueBuilder.durable(properties.getQueues().getDlq()).build();
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(dlqQueue())
                .to(principalExchange())
                .with(properties.getRoutingKeys().getDlq());
    }

    // --- COLAS DE NEGOCIO ---
    
    // 1. Cola de Emisión (con desvío a DLQ en caso de fallo)
    @Bean
    public Queue emisionQueue() {
        return QueueBuilder.durable(properties.getQueues().getEmision())
                .withArgument("x-dead-letter-exchange", properties.getExchanges().getPrincipal())
                .withArgument("x-dead-letter-routing-key", properties.getRoutingKeys().getDlq())
                .build();
    }

    @Bean
    public Binding emisionBinding() {
        return BindingBuilder.bind(emisionQueue())
                .to(principalExchange())
                .with(properties.getRoutingKeys().getEmision());
    }

    // 2. Cola de Acceso Transversal (con desvío a DLQ en caso de fallo)
    @Bean
    public Queue accesoQueue() {
        return QueueBuilder.durable(properties.getQueues().getAcceso())
                .withArgument("x-dead-letter-exchange", properties.getExchanges().getPrincipal())
                .withArgument("x-dead-letter-routing-key", properties.getRoutingKeys().getDlq())
                .build();
    }

    @Bean
    public Binding accesoBinding() {
        return BindingBuilder.bind(accesoQueue())
                .to(principalExchange())
                .with(properties.getRoutingKeys().getAcceso());
    }
}