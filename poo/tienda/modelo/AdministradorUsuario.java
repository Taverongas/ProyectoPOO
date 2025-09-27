package poo.tienda.modelo;

public class AdministradorUsuario extends Usuario {
    private int nivelAcceso;

    public AdministradorUsuario(String nombre, String email, String password, int nivelAcceso) {
        super(0, nombre, email, password);
        this.nivelAcceso = nivelAcceso;
    }

    public void suspenderUsuario(Usuario u) { u.setActivo(false); }
    public void reactivarUsuario(Usuario u) { u.setActivo(true); }

    @Override public boolean esAdmin() { return true; }
}