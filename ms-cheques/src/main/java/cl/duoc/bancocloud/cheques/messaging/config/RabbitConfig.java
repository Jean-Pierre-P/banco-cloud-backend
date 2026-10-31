package cl.duoc.bancocloud.cheques.messaging.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de mensajería del productor. Aquí NO hay lógica de negocio.
 * Las colas, DLQ y bindings los declara el microservicio consumidor (ms-notificaciones);
 * el productor solo necesita que exista el exchange.
 */
@Configuration
public class RabbitConfig {

    @Bean
    DirectExchange chequesExchange(MessagingProperties props) {
        return ExchangeBuilder.directExchange(props.exchange().principal()).durable(true).build();
    }

    /** Los mensajes viajan como JSON (RabbitTemplate usa este converter automáticamente). */
    @Bean
    MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}