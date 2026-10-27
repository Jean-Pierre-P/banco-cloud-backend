package cl.duoc.bancocloud.rabbitadmin.dto;

import jakarta.validation.constraints.NotBlank;

public class ExchangeRequest {

    @NotBlank(message = "El nombre del exchange es obligatorio")
    private String name;

    @NotBlank(message = "El tipo de exchange es obligatorio")
    private String type;

    private boolean durable = true;

    // Constructores manuales
    public ExchangeRequest() {}

    // Getters y Setters manuales
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isDurable() {
        return durable;
    }

    public void setDurable(boolean durable) {
        this.durable = durable;
    }
}