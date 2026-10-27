package cl.duoc.bancocloud.rabbitadmin.dto;

import jakarta.validation.constraints.NotBlank;

public class QueueRequest {

    @NotBlank(message = "El nombre de la cola es obligatorio")
    private String name;

    private boolean durable = true;

    // Constructores manuales
    public QueueRequest() {}

    // Getters y Setters manuales
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDurable() {
        return durable;
    }

    public void setDurable(boolean durable) {
        this.durable = durable;
    }
}