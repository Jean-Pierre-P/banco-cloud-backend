package cl.duoc.bancocloud.notificaciones.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Nombres de la topología leídos de application.yml (prefijo app.messaging). */
@ConfigurationProperties(prefix = "app.messaging")
public record MessagingProperties(String exchange, String deadLetterExchange, Queues queues) {

    public record Queues(QueueDef chequeEmitido, QueueDef chequeEstado, QueueDef chequeDevuelto) {
    }

    public record QueueDef(String name, String routingKey, String dlq) {
    }
}
