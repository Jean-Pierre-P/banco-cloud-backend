package cl.duoc.bancocloud.cheques.exception;

public class ChequeNoEncontradoException extends RuntimeException {
    public ChequeNoEncontradoException(Long id) {
        super("No existe el cheque con id " + id);
    }
}
