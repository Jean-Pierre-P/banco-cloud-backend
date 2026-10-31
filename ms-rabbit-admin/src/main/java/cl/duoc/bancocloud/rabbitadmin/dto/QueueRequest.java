package cl.duoc.bancocloud.rabbitadmin.dto;

import jakarta.validation.constraints.NotBlank;

public class QueueRequest {

    @NotBlank(message = "El nombre de la cola es obligatorio")
    private String name;

    private boolean durable = true;

    private boolean autoDelete = false;

    /** Exchange al que se enlaza la cola (opcional, requerido si se envía routingKey). */
    private String exchange;

    /** Routing key del binding (opcional, requiere exchange). */
    private String routingKey;

    /** Exchange de mensajes no entregados (DLX) para esta cola (opcional). */
    private String deadLetterExchange;

    public QueueRequest() {}

    public QueueRequest(String name, boolean durable, boolean autoDelete,
                        String exchange, String routingKey, String deadLetterExchange) {
        this.name = name;
        this.durable = durable;
        this.autoDelete = autoDelete;
        this.exchange = exchange;
        this.routingKey = routingKey;
        this.deadLetterExchange = deadLetterExchange;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isDurable() { return durable; }
    public void setDurable(boolean durable) { this.durable = durable; }

    public boolean isAutoDelete() { return autoDelete; }
    public void setAutoDelete(boolean autoDelete) { this.autoDelete = autoDelete; }

    public String getExchange() { return exchange; }
    public void setExchange(String exchange) { this.exchange = exchange; }

    public String getRoutingKey() { return routingKey; }
    public void setRoutingKey(String routingKey) { this.routingKey = routingKey; }

    public String getDeadLetterExchange() { return deadLetterExchange; }
    public void setDeadLetterExchange(String deadLetterExchange) { this.deadLetterExchange = deadLetterExchange; }
}