package cl.duoc.bancocloud.cheques.messaging.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Nombres leídos de application.yml (prefijo app.messaging). */
@ConfigurationProperties(prefix = "app.messaging")
public record MessagingProperties(String exchange, RoutingKeys routingKeys) {

    public record RoutingKeys(String chequeEmitido, String chequeEstado, String chequeDevuelto) {
    }
}
