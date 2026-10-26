package cl.duoc.bancocloud.rabbitadmin.service;

import cl.duoc.bancocloud.rabbitadmin.dto.QueueRequest;
import cl.duoc.bancocloud.rabbitadmin.exception.RecursoNoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RabbitAdminServiceTest {

    @Mock
    AmqpAdmin amqpAdmin;

    @InjectMocks
    RabbitAdminService service;

    @Test
    void crearCola_valida_declaraLaCola() {
        service.crearCola(new QueueRequest("cola.prueba", true, false, null, null, null));
        verify(amqpAdmin).declareQueue(any(Queue.class));
    }

    @Test
    void crearCola_routingKeySinExchange_lanzaError() {
        QueueRequest req = new QueueRequest("cola.prueba", true, false, null, "rk", null);
        assertThrows(IllegalArgumentException.class, () -> service.crearCola(req));
        verifyNoInteractions(amqpAdmin);
    }

    @Test
    void eliminarCola_inexistente_lanzaNoEncontrado() {
        when(amqpAdmin.deleteQueue("no.existe")).thenReturn(false);
        assertThrows(RecursoNoEncontradoException.class, () -> service.eliminarCola("no.existe"));
    }
}
