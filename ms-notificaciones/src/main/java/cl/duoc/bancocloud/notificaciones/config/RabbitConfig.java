package cl.duoc.bancocloud.notificaciones.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Declara exchange, DLX, colas, DLQs y bindings del dominio "cheques".
 * Los nombres vienen de application.yml (app.messaging.*).
 */
@Configuration
public class RabbitConfig {

    @Value("${app.messaging.exchange.name}") private String exchangeName;
    @Value("${app.messaging.dlx.name}") private String dlxName;

    @Value("${app.messaging.queues.cheque-emitido.name}") private String emitidoQueue;
    @Value("${app.messaging.queues.cheque-emitido.dlq}") private String emitidoDlq;
    @Value("${app.messaging.queues.cheque-emitido.routing-key}") private String emitidoKey;

    @Value("${app.messaging.queues.cheque-estado.name}") private String estadoQueue;
    @Value("${app.messaging.queues.cheque-estado.dlq}") private String estadoDlq;
    @Value("${app.messaging.queues.cheque-estado.routing-key}") private String estadoKey;

    @Value("${app.messaging.queues.cheque-devuelto.name}") private String devueltoQueue;
    @Value("${app.messaging.queues.cheque-devuelto.dlq}") private String devueltoDlq;
    @Value("${app.messaging.queues.cheque-devuelto.routing-key}") private String devueltoKey;

    // ---------- Exchanges ----------
    @Bean
    DirectExchange chequesExchange() {
        return ExchangeBuilder.directExchange(exchangeName).durable(true).build();
    }

    @Bean
    DirectExchange chequesDlx() {
        return ExchangeBuilder.directExchange(dlxName).durable(true).build();
    }

    // ---------- Colas principales (con DLX configurado) ----------
    @Bean
    Queue chequeEmitidoQueue() {
        return QueueBuilder.durable(emitidoQueue)
                .deadLetterExchange(dlxName).deadLetterRoutingKey(emitidoDlq).build();
    }

    @Bean
    Queue chequeEstadoQueue() {
        return QueueBuilder.durable(estadoQueue)
                .deadLetterExchange(dlxName).deadLetterRoutingKey(estadoDlq).build();
    }

    @Bean
    Queue chequeDevueltoQueue() {
        return QueueBuilder.durable(devueltoQueue)
                .deadLetterExchange(dlxName).deadLetterRoutingKey(devueltoDlq).build();
    }

    // ---------- DLQs ----------
    @Bean
    Queue chequeEmitidoDlq() {
        return QueueBuilder.durable(emitidoDlq).build();
    }

    @Bean
    Queue chequeEstadoDlq() {
        return QueueBuilder.durable(estadoDlq).build();
    }

    @Bean
    Queue chequeDevueltoDlq() {
        return QueueBuilder.durable(devueltoDlq).build();
    }

    // ---------- Bindings cola principal <- exchange ----------
    @Bean
    Binding bindingEmitido() {
        return BindingBuilder.bind(chequeEmitidoQueue()).to(chequesExchange()).with(emitidoKey);
    }

    @Bean
    Binding bindingEstado() {
        return BindingBuilder.bind(chequeEstadoQueue()).to(chequesExchange()).with(estadoKey);
    }

    @Bean
    Binding bindingDevuelto() {
        return BindingBuilder.bind(chequeDevueltoQueue()).to(chequesExchange()).with(devueltoKey);
    }

    // ---------- Bindings DLQ <- DLX ----------
    @Bean
    Binding bindingEmitidoDlq() {
        return BindingBuilder.bind(chequeEmitidoDlq()).to(chequesDlx()).with(emitidoDlq);
    }

    @Bean
    Binding bindingEstadoDlq() {
        return BindingBuilder.bind(chequeEstadoDlq()).to(chequesDlx()).with(estadoDlq);
    }

    @Bean
    Binding bindingDevueltoDlq() {
        return BindingBuilder.bind(chequeDevueltoDlq()).to(chequesDlx()).with(devueltoDlq);
    }

    // ---------- Convertidor JSON ----------
    @Bean
    MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}