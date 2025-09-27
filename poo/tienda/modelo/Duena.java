package poo.tienda.modelo;

import java.util.Date;

public class Duena extends Usuario {
    private String claveMaestra;
    private Date fechaCoronacion;

    public Duena(String nombre, String email, String password, String claveMaestra, Date fechaCoronacion) {
        super(0, nombre, email, password);
        this.claveMaestra = claveMaestra;
        this.fechaCoronacion = fechaCoronacion;
    }

    public boolean accesoTotal() { return true; }
    @Override public boolean esAdmin() { return true; }
}