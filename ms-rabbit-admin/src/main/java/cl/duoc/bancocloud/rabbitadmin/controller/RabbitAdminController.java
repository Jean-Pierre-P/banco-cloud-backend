package cl.duoc.bancocloud.rabbitadmin.controller;

import cl.duoc.bancocloud.rabbitadmin.dto.*;
import cl.duoc.bancocloud.rabbitadmin.service.RabbitAdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/** API REST de administración de RabbitMQ. Solo usuarios con rol ADMIN. */
@RestController
@RequestMapping("/api/rabbit")
@PreAuthorize("hasRole('ADMIN')")
public class RabbitAdminController {

    private final RabbitAdminService service;

    public RabbitAdminController(RabbitAdminService service) {
        this.service = service;
    }

    // ---------- /queues ----------
    @PostMapping("/queues")
    @ResponseStatus(HttpStatus.CREATED)
    public void crearCola(@Valid @RequestBody QueueRequest req) {
        service.crearCola(req);
    }

    @GetMapping("/queues/{name}")
    public QueueInfoResponse obtenerCola(@PathVariable String name) {
        return service.obtenerCola(name);
    }

    @DeleteMapping("/queues/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarCola(@PathVariable String name) {
        service.eliminarCola(name);
    }

    // ---------- /exchanges ----------
    @PostMapping("/exchanges")
    @ResponseStatus(HttpStatus.CREATED)
    public void crearExchange(@Valid @RequestBody ExchangeRequest req) {
        service.crearExchange(req);
    }

    @DeleteMapping("/exchanges/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarExchange(@PathVariable String name) {
        service.eliminarExchange(name);
    }

    // ---------- /bindings ----------
    @PostMapping("/bindings")
    @ResponseStatus(HttpStatus.CREATED)
    public void crearBinding(@Valid @RequestBody BindingRequest req) {
        service.crearBinding(req);
    }

    @DeleteMapping("/bindings")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarBinding(@RequestParam String queue, @RequestParam String exchange,
                                @RequestParam(defaultValue = "") String routingKey) {
        service.eliminarBinding(queue, exchange, routingKey);
    }
}
