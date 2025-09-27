package poo.tienda.modelo;

public class DesarrolladorProducto extends Usuario {
    private String especialidad;

    public DesarrolladorProducto(String nombre, String email, String password, String especialidad) {
        super(0, nombre, email, password);
        this.especialidad = especialidad;
    }

    public void disenarProducto() { }
}