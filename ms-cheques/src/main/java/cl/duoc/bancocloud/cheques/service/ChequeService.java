package cl.duoc.bancocloud.cheques.service;

import cl.duoc.bancocloud.cheques.dto.EmitirChequeRequest;
import cl.duoc.bancocloud.cheques.exception.ChequeNoEncontradoException;
import cl.duoc.bancocloud.cheques.messaging.producer.ChequeEventProducer;
import cl.duoc.bancocloud.cheques.model.Cheque;
import cl.duoc.bancocloud.cheques.model.EstadoCheque;
import cl.duoc.bancocloud.cheques.repository.ChequeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ChequeService {

    private final ChequeRepository repository;
    private final ChequeEventProducer producer;

    public ChequeService(ChequeRepository repository, ChequeEventProducer producer) {
        this.repository = repository;
        this.producer = producer;
    }

    @Transactional
    public Cheque emitir(String clienteId, EmitirChequeRequest req) {
        Cheque c = new Cheque();
        c.setClienteId(clienteId);
        c.setBeneficiario(req.beneficiario());
        c.setMonto(req.monto());
        c.setFecha(req.fecha());
        c.setEstado(EstadoCheque.EMITIDO);
        Cheque guardado = repository.save(c);
        producer.publicarEmitido(guardado);   // tarea asíncrona: notificar al cliente
        return guardado;
    }

    @Transactional(readOnly = true)
    public List<Cheque> consultarPropios(String clienteId) {
        return repository.findByClienteId(clienteId);
    }

    @Transactional(readOnly = true)
    public List<Cheque> listarTodos() {
        return repository.findAll();
    }

    @Transactional
    public Cheque cambiarEstado(Long id, EstadoCheque nuevo) {
        if (nuevo == EstadoCheque.DEVUELTO) {
            throw new IllegalArgumentException("Para devolver un cheque use el endpoint de devolución");
        }
        Cheque c = buscar(id);
        validarModificable(c);
        c.setEstado(nuevo);
        Cheque guardado = repository.save(c);
        producer.publicarEstadoCambiado(guardado);
        return guardado;
    }

    @Transactional
    public Cheque registrarDevolucion(Long id, String motivo) {
        Cheque c = buscar(id);
        validarModificable(c);
        c.setEstado(EstadoCheque.DEVUELTO);
        c.setMotivoDevolucion(motivo);
        Cheque guardado = repository.save(c);
        producer.publicarDevuelto(guardado);
        return guardado;
    }

    private Cheque buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new ChequeNoEncontradoException(id));
    }

    private void validarModificable(Cheque c) {
        if (c.getEstado() == EstadoCheque.ANULADO || c.getEstado() == EstadoCheque.DEVUELTO) {
            throw new IllegalStateException("El cheque " + c.getId() + " ya está " + c.getEstado() + " y no puede modificarse");
        }
    }
}
