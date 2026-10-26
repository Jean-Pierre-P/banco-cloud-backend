package cl.duoc.bancocloud.notificaciones.config;

import cl.duoc.bancocloud.notificaciones.config.MessagingProperties.QueueDef;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ÚNICA clase que declara la topología RabbitMQ de este servicio.
 * Por cada caso de uso hay 4 beans: Queue, DLQ, Binding y Binding de la DLQ.
 * Ningún servicio ni consumidor crea colas: solo las usan.
 *
 * Flujo de un mensaje fallido:
 *   cola principal --(basicNack requeue=false)--> DLX --(routing key = nombre DLQ)--> DLQ
 */
@Configuration
public class RabbitConfig {

    private final MessagingProperties props;

    public RabbitConfig(MessagingProperties props) {
        this.props = props;
    }

    // ---------- Exchanges ----------
    @Bean
    DirectExchange chequesExchange() {
        return ExchangeBuilder.directExchange(props.exchange()).durable(true).build();
    }

    @Bean
    DirectExchange deadLetterExchange() {
        return ExchangeBuilder.directExchange(props.deadLetterExchange()).durable(true).build();
    }

    // ---------- Caso de uso 1: cheque emitido ----------
    @Bean
    Queue chequeEmitidoQueue() { return colaConDlq(props.queues().chequeEmitido()); }

    @Bean
    Queue chequeEmitidoDlq() { return dlq(props.queues().chequeEmitido()); }

    @Bean
    Binding chequeEmitidoBinding() { return enlazar(chequeEmitidoQueue(), props.queues().chequeEmitido()); }

    @Bean
    Binding chequeEmitidoDlqBinding() { return enlazarDlq(chequeEmitidoDlq(), props.queues().chequeEmitido()); }

    // ---------- Caso de uso 2: estado de cheque cambiado ----------
    @Bean
    Queue chequeEstadoQueue() { return colaConDlq(props.queues().chequeEstado()); }

    @Bean
    Queue chequeEstadoDlq() { return dlq(props.queues().chequeEstado()); }

    @Bean
    Binding chequeEstadoBinding() { return enlazar(chequeEstadoQueue(), props.queues().chequeEstado()); }

    @Bean
    Binding chequeEstadoDlqBinding() { return enlazarDlq(chequeEstadoDlq(), props.queues().chequeEstado()); }

    // ---------- Caso de uso 3: cheque devuelto ----------
    @Bean
    Queue chequeDevueltoQueue() { return colaConDlq(props.queues().chequeDevuelto()); }

    @Bean
    Queue chequeDevueltoDlq() { return dlq(props.queues().chequeDevuelto()); }

    @Bean
    Binding chequeDevueltoBinding() { return enlazar(chequeDevueltoQueue(), props.queues().chequeDevuelto()); }

    @Bean
    Binding chequeDevueltoDlqBinding() { return enlazarDlq(chequeDevueltoDlq(), props.queues().chequeDevuelto()); }

    // ---------- Converter JSON ----------
    @Bean
    MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    // ---------- Helpers ----------
    private Queue colaConDlq(QueueDef def) {
        return QueueBuilder.durable(def.name())
                .deadLetterExchange(props.deadLetterExchange())
                .deadLetterRoutingKey(def.dlq())
                .build();
    }

    private Queue dlq(QueueDef def) {
        return QueueBuilder.durable(def.dlq()).build();
    }

    private Binding enlazar(Queue queue, QueueDef def) {
        return BindingBuilder.bind(queue).to(chequesExchange()).with(def.routingKey());
    }

    private Binding enlazarDlq(Queue dlq, QueueDef def) {
        return BindingBuilder.bind(dlq).to(deadLetterExchange()).with(def.dlq());
    }
}
