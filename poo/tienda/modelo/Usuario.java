package poo.tienda.modelo;

public class Usuario {
    private static int sec = 1;
    protected int id;
    protected String nombre;
    protected String email;
    protected String password;
    protected boolean activo = true;

    public Usuario(int id, String nombre, String email, String password) {
        this.id = (id == 0) ? sec++ : id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
    }

    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean a) { this.activo = a; }
    public boolean esAdmin() { return false; }
}