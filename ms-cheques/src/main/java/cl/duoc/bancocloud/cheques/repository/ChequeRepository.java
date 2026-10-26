package cl.duoc.bancocloud.cheques.repository;

import cl.duoc.bancocloud.cheques.model.Cheque;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChequeRepository extends JpaRepository<Cheque, Long> {
    List<Cheque> findByClienteId(String clienteId);
}
