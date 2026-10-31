package cl.duoc.bancocloud.cheques.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record EmitirChequeRequest(
        @NotBlank(message = "El beneficiario es obligatorio") String beneficiario,
        @NotNull(message = "El monto es obligatorio")
        @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
        @DecimalMax(value = "99999999.99", message = "El monto excede el máximo permitido (99.999.999,99)")
        @Digits(integer = 8, fraction = 2, message = "El monto admite hasta 8 enteros y 2 decimales") BigDecimal monto,
        @NotNull(message = "La fecha es obligatoria")
        @FutureOrPresent(message = "La fecha no puede ser pasada") LocalDate fecha) {
}