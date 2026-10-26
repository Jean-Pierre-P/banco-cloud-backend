package cl.duoc.bancocloud.cheques.controller;

import cl.duoc.bancocloud.cheques.dto.*;
import cl.duoc.bancocloud.cheques.service.ChequeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cheques")
public class ChequeController {

    private final ChequeService service;

    public ChequeController(ChequeService service) {
        this.service = service;
    }

    // ---------- Usuario (cliente) ----------
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public ChequeResponse emitir(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody EmitirChequeRequest req) {
        return ChequeResponse.of(service.emitir(jwt.getSubject(), req));
    }

    @GetMapping("/mios")
    @PreAuthorize("hasRole('USER')")
    public List<ChequeResponse> misCheques(@AuthenticationPrincipal Jwt jwt) {
        return service.consultarPropios(jwt.getSubject()).stream().map(ChequeResponse::of).toList();
    }

    // ---------- Admin (empleado) ----------
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<ChequeResponse> listarTodos() {
        return service.listarTodos().stream().map(ChequeResponse::of).toList();
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ChequeResponse cambiarEstado(@PathVariable Long id, @Valid @RequestBody CambiarEstadoRequest req) {
        return ChequeResponse.of(service.cambiarEstado(id, req.estado()));
    }

    @PostMapping("/{id}/devolucion")
    @PreAuthorize("hasRole('ADMIN')")
    public ChequeResponse registrarDevolucion(@PathVariable Long id, @Valid @RequestBody DevolucionRequest req) {
        return ChequeResponse.of(service.registrarDevolucion(id, req.motivo()));
    }
}
