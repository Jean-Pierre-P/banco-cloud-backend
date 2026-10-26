package cl.duoc.bancocloud.cheques.messaging.event;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Mensaje que se publica en RabbitMQ. tipoEvento: EMITIDO | ESTADO_CAMBIADO | DEVUELTO */
public record ChequeEvent(Long chequeId, String clienteId, String beneficiario, BigDecimal monto,
                          LocalDate fecha, String estado, String motivo, String tipoEvento) {
}
