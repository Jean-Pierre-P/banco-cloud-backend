package cl.duoc.bancocloud.cheques.controller;

import cl.duoc.bancocloud.cheques.model.UsuarioCliente;
import cl.duoc.bancocloud.cheques.repository.UsuarioClienteRepository;
import cl.duoc.bancocloud.cheques.messaging.producer.UsuarioEventProducer;
import cl.duoc.bancocloud.cheques.messaging.event.UsuarioAutorizadoEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioAccesoController {

    private final UsuarioClienteRepository usuarioRepository;
    private final UsuarioEventProducer usuarioEventProducer;

    public UsuarioAccesoController(UsuarioClienteRepository usuarioRepository, UsuarioEventProducer usuarioEventProducer) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioEventProducer = usuarioEventProducer;
    }

    @PatchMapping("/{id}/autorizar")
    public ResponseEntity<UsuarioCliente> autorizarAcceso(@PathVariable Long id) {
        UsuarioCliente cliente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        cliente.setAccesoAutorizado(true);
        UsuarioCliente actualizado = usuarioRepository.save(cliente);

        usuarioEventProducer.enviarEventoAutorizacion(
            new UsuarioAutorizadoEvent(actualizado.getId(), actualizado.getEmail())
        );

        return ResponseEntity.ok(actualizado);
    }
}