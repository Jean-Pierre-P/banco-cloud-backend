package cl.duoc.bancocloud.rabbitadmin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ExchangeRequest(
        @NotBlank(message = "El nombre del exchange es obligatorio")
        @Size(max = 255, message = "El nombre no puede superar 255 caracteres")
        @Pattern(regexp = "^[A-Za-z0-9._-]*$", message = "El nombre solo admite letras, números, punto, guion y guion bajo")
        String name,
        @NotNull(message = "El tipo es obligatorio (DIRECT, TOPIC o FANOUT)") TipoExchange type,
        Boolean durable,
        Boolean autoDelete) {
}
