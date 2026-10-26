package cl.duoc.bancocloud.cheques.dto;

import cl.duoc.bancocloud.cheques.model.EstadoCheque;
import jakarta.validation.constraints.NotNull;

public record CambiarEstadoRequest(
        @NotNull(message = "El estado es obligatorio") EstadoCheque estado) {
}
