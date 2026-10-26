package cl.duoc.bancocloud.cheques.dto;

import cl.duoc.bancocloud.cheques.model.Cheque;
import cl.duoc.bancocloud.cheques.model.EstadoCheque;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ChequeResponse(Long id, String clienteId, String beneficiario, BigDecimal monto,
                             LocalDate fecha, EstadoCheque estado, String motivoDevolucion) {

    public static ChequeResponse of(Cheque c) {
        return new ChequeResponse(c.getId(), c.getClienteId(), c.getBeneficiario(), c.getMonto(),
                c.getFecha(), c.getEstado(), c.getMotivoDevolucion());
    }
}
