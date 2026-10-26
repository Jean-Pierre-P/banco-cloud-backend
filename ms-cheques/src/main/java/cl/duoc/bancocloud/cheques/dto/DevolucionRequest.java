package cl.duoc.bancocloud.cheques.dto;

import jakarta.validation.constraints.NotBlank;

public record DevolucionRequest(
        @NotBlank(message = "El motivo de la devolución es obligatorio") String motivo) {
}
