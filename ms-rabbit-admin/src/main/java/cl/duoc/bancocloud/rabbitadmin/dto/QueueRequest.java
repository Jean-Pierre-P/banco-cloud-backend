package cl.duoc.bancocloud.rabbitadmin.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record QueueRequest(
        @NotBlank(message = "El nombre de la cola es obligatorio")
        @Size(max = 255, message = "El nombre no puede superar 255 caracteres")
        @Pattern(regexp = "^[A-Za-z0-9._-]*$", message = "El nombre solo admite letras, números, punto, guion y guion bajo")
        String name,
        Boolean durable,                 // si es null se asume true
        Boolean autoDelete,              // si es null se asume false
        String deadLetterExchange,
        String deadLetterRoutingKey,
        @Min(value = 1, message = "El TTL debe ser mayor a 0 ms") Long messageTtlMs) {
}
