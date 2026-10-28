package cl.duoc.bancocloud.notificaciones.event;

public class UsuarioAutorizadoEvent {
    private Long idUsuario;
    private String email;

    public UsuarioAutorizadoEvent() {}

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}