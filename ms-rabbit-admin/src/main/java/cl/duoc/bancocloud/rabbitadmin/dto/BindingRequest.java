package cl.duoc.bancocloud.rabbitadmin.dto;

import jakarta.validation.constraints.NotBlank;

public record BindingRequest(
        @NotBlank(message = "La cola es obligatoria") String queue,
        @NotBlank(message = "El exchange es obligatorio") String exchange,
        String routingKey) {          // puede ir vacío (ej: exchange FANOUT)
}
