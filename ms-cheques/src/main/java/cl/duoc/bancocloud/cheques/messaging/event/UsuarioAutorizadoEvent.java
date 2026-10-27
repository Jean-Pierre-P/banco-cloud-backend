package cl.duoc.bancocloud.cheques.messaging.event;

public class UsuarioAutorizadoEvent {
    private Long idUsuario;
    private String email;

    public UsuarioAutorizadoEvent() {}

    public UsuarioAutorizadoEvent(Long idUsuario, String email) {
        this.idUsuario = idUsuario;
        this.email = email;
    }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}