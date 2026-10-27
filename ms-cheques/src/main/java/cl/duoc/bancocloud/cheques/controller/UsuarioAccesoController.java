package cl.duoc.bancocloud.cheques.controller;

import cl.duoc.bancocloud.cheques.model.UsuarioCliente;
import cl.duoc.bancocloud.cheques.repository.UsuarioClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioAccesoController {

    private final UsuarioClienteRepository usuarioRepository;
    // Opcional: Inyecta tu ChequeEventProducer para enviar el mensaje a RabbitMQ

    @PatchMapping("/{id}/autorizar")
    public ResponseEntity<UsuarioCliente> autorizarAcceso(@PathVariable Long id) {
        UsuarioCliente cliente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        cliente.setAccesoAutorizado(true);
        UsuarioCliente actualizado = usuarioRepository.save(cliente);

        // Aquí llamas a tu producer para enviar a la cola banco.usuarios.acceso.queue
        // chequeEventProducer.enviarEventoAutorizacion(actualizado);

        return ResponseEntity.ok(actualizado);
    }
}