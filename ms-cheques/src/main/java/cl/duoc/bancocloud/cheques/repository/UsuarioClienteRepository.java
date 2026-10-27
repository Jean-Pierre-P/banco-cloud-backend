package cl.duoc.bancocloud.cheques.repository;

import cl.duoc.bancocloud.cheques.model.UsuarioCliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioClienteRepository extends JpaRepository<UsuarioCliente, Long> {
}