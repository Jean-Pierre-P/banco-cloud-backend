package cl.duoc.bancocloud.rabbitadmin.controller;

import cl.duoc.bancocloud.rabbitadmin.dto.BindingRequest;
import cl.duoc.bancocloud.rabbitadmin.dto.ExchangeRequest;
import cl.duoc.bancocloud.rabbitadmin.dto.QueueRequest;
import cl.duoc.bancocloud.rabbitadmin.service.RabbitAdminService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/rabbit-admin")
@RequiredArgsConstructor
public class RabbitAdminController {

    private final RabbitAdminService rabbitAdminService;

    @PostMapping("/queues")
    public ResponseEntity<Map<String, String>> createQueue(@Valid @RequestBody QueueRequest request) {
        String queueName = rabbitAdminService.crearCola(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("mensaje", "Cola creada correctamente", "cola", queueName));
    }

    @DeleteMapping("/queues/{name}")
    public ResponseEntity<Map<String, String>> deleteQueue(@PathVariable @NotBlank String name) {
        rabbitAdminService.eliminarCola(name);
        return ResponseEntity.ok(Map.of("mensaje", "Cola eliminada correctamente", "cola", name));
    }

    @PostMapping("/exchanges")
    public ResponseEntity<Map<String, String>> createExchange(@Valid @RequestBody ExchangeRequest request) {
        rabbitAdminService.crearExchange(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("mensaje", "Exchange creado correctamente", "exchange", request.getName()));
    }

    @DeleteMapping("/exchanges/{name}")
    public ResponseEntity<Map<String, String>> deleteExchange(@PathVariable @NotBlank String name) {
        rabbitAdminService.eliminarExchange(name);
        return ResponseEntity.ok(Map.of("mensaje", "Exchange eliminado correctamente", "exchange", name));
    }

    @PostMapping("/bindings")
    public ResponseEntity<Map<String, String>> createBinding(@Valid @RequestBody BindingRequest request) {
        rabbitAdminService.crearBinding(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("mensaje", "Binding creado exitosamente"));
    }
}