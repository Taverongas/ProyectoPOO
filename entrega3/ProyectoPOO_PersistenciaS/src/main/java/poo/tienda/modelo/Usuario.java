package poo.tienda.modelo;

import java.io.Serializable;

public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;
    private String email;
    private String password;
    private boolean activo;
    private boolean admin;  // << NUEVO

    public Usuario(int id, String nombre, String email, String password) {
        this.id = id;  // 0 si es nuevo
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.activo = true;
        this.admin = false; // por defecto NO es admin
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public boolean isActivo() { return activo; }
    public boolean isAdmin() { return admin; }      // << NUEVO

    public void setActivo(boolean activo) { this.activo = activo; }
    public void setAdmin(boolean admin) { this.admin = admin; }   // << NUEVO
}