package cl.duoc.bancocloud.cheques.model;
import jakarta.persistence.*;
import java.util.Objects;
@Entity
@Table(name = "USUARIOS_CLIENTES")
public class UsuarioCliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String rut;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private boolean accesoAutorizado;

    public UsuarioCliente() {
    }

    public UsuarioCliente(Long id, String rut, String email, boolean accesoAutorizado) {
        this.id = id;
        this.rut = rut;
        this.email = email;
        this.accesoAutorizado = accesoAutorizado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAccesoAutorizado() {
        return accesoAutorizado;
    }

    public void setAccesoAutorizado(boolean accesoAutorizado) {
        this.accesoAutorizado = accesoAutorizado;
    }

    public static UsuarioClienteBuilder builder() {
        return new UsuarioClienteBuilder();
    }

    public static class UsuarioClienteBuilder {
        private Long id;
        private String rut;
        private String email;
        private boolean accesoAutorizado;

        public UsuarioClienteBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UsuarioClienteBuilder rut(String rut) {
            this.rut = rut;
            return this;
        }

        public UsuarioClienteBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UsuarioClienteBuilder accesoAutorizado(boolean accesoAutorizado) {
            this.accesoAutorizado = accesoAutorizado;
            return this;
        }

        public UsuarioCliente build() {
            return new UsuarioCliente(id, rut, email, accesoAutorizado);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof UsuarioCliente)) {
            return false;
        }
        UsuarioCliente that = (UsuarioCliente) object;
        return accesoAutorizado == that.accesoAutorizado
                && Objects.equals(id, that.id)
                && Objects.equals(rut, that.rut)
                && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rut, email, accesoAutorizado);
    }

    @Override
    public String toString() {
        return "UsuarioCliente(id=" + id + ", rut=" + rut + ", email=" + email
                + ", accesoAutorizado=" + accesoAutorizado + ")";
    }
}