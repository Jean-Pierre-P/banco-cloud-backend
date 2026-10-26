package cl.duoc.bancocloud.cheques.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cheques")
public class Cheque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** "sub" del JWT del cliente que emitió el cheque */
    @Column(nullable = false)
    private String clienteId;

    @Column(nullable = false)
    private String beneficiario;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false)
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCheque estado = EstadoCheque.EMITIDO;

    private String motivoDevolucion;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }
    public String getBeneficiario() { return beneficiario; }
    public void setBeneficiario(String beneficiario) { this.beneficiario = beneficiario; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public EstadoCheque getEstado() { return estado; }
    public void setEstado(EstadoCheque estado) { this.estado = estado; }
    public String getMotivoDevolucion() { return motivoDevolucion; }
    public void setMotivoDevolucion(String motivoDevolucion) { this.motivoDevolucion = motivoDevolucion; }
}
