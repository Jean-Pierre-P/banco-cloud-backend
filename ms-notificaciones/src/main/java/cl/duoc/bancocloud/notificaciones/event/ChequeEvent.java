package cl.duoc.bancocloud.notificaciones.event;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Copia PROPIA del contrato del mensaje (no se comparte código con ms-cheques).
 * Mientras el JSON tenga los mismos campos, los servicios siguen desacoplados.
 */
public record ChequeEvent(Long chequeId, String clienteId, String beneficiario, BigDecimal monto,
                          LocalDate fecha, String estado, String motivo, String tipoEvento) {
}
