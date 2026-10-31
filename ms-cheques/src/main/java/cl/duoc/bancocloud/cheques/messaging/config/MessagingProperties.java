package cl.duoc.bancocloud.cheques.messaging.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.rabbitmq")
public class MessagingProperties {

    private Exchanges exchanges = new Exchanges();
    private Queues queues = new Queues();
    private RoutingKeys routingKeys = new RoutingKeys();

    // Getters y Setters estándar
    public Exchanges getExchanges() { return exchanges; }
    public void setExchanges(Exchanges exchanges) { this.exchanges = exchanges; }
    public Queues getQueues() { return queues; }
    public void setQueues(Queues queues) { this.queues = queues; }
    public RoutingKeys getRoutingKeys() { return routingKeys; }
    public void setRoutingKeys(RoutingKeys routingKeys) { this.routingKeys = routingKeys; }

    // Métodos fluidos (compatibles con .exchange() y .routingKeys())
    public Exchanges exchange() { return exchanges; }
    public RoutingKeys routingKeys() { return routingKeys; }
    public Queues queues() { return queues; }

    public static class Exchanges {
        private String principal;
        public String getPrincipal() { return principal; }
        public void setPrincipal(String principal) { this.principal = principal; }
        public String principal() { return principal; }
    }

    public static class Queues {
        private String emision;
        private String acceso;
        public String getEmision() { return emision; }
        public void setEmision(String emision) { this.emision = emision; }
        public String getAcceso() { return acceso; }
        public void setAcceso(String acceso) { this.acceso = acceso; }
        public String emision() { return emision; }
        public String acceso() { return acceso; }
    }

    public static class RoutingKeys {
        private String emision;
        private String acceso;
        private String chequeEmitido;
        private String chequeEstado;
        private String chequeDevuelto;

        public String getEmision() { return emision; }
        public void setEmision(String emision) { this.emision = emision; }
        public String getAcceso() { return acceso; }
        public void setAcceso(String acceso) { this.acceso = acceso; }

        public String getChequeEmitido() { return chequeEmitido; }
        public void setChequeEmitido(String chequeEmitido) { this.chequeEmitido = chequeEmitido; }
        public String getChequeEstado() { return chequeEstado; }
        public void setChequeEstado(String chequeEstado) { this.chequeEstado = chequeEstado; }
        public String getChequeDevuelto() { return chequeDevuelto; }
        public void setChequeDevuelto(String chequeDevuelto) { this.chequeDevuelto = chequeDevuelto; }

        public String emision() { return emision; }
        public String acceso() { return acceso; }
        public String chequeEmitido() { return chequeEmitido; }
        public String chequeEstado() { return chequeEstado; }
        public String chequeDevuelto() { return chequeDevuelto; }
    }
}