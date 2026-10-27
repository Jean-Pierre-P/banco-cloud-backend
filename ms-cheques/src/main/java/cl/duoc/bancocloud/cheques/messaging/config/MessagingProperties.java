package cl.duoc.bancocloud.cheques.messaging.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.rabbitmq")
public class MessagingProperties {
    
    private Exchanges exchanges = new Exchanges();
    private Queues queues = new Queues();
    private RoutingKeys routingKeys = new RoutingKeys();

    public Exchanges getExchanges() { return exchanges; }
    public void setExchanges(Exchanges exchanges) { this.exchanges = exchanges; }
    public Queues getQueues() { return queues; }
    public void setQueues(Queues queues) { this.queues = queues; }
    public RoutingKeys getRoutingKeys() { return routingKeys; }
    public void setRoutingKeys(RoutingKeys routingKeys) { this.routingKeys = routingKeys; }

    public static class Exchanges {
        private String principal;
        public String getPrincipal() { return principal; }
        public void setPrincipal(String principal) { this.principal = principal; }
    }

    public static class Queues {
        private String emision;
        private String acceso;
        public String getEmision() { return emision; }
        public void setEmision(String emision) { this.emision = emision; }
        public String getAcceso() { return acceso; }
        public void setAcceso(String acceso) { this.acceso = acceso; }
    }

    public static class RoutingKeys {
        private String emision;
        private String acceso;
        public String getEmision() { return emision; }
        public void setEmision(String emision) { this.emision = emision; }
        public String getAcceso() { return acceso; }
        public void setAcceso(String acceso) { this.acceso = acceso; }
    }
}