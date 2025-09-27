package poo.tienda.modelo;

public class AdministradorContenido extends Usuario {
    private String permisosEdicion;

    public AdministradorContenido(String nombre, String email, String password, String permisosEdicion) {
        super(0, nombre, email, password);
        this.permisosEdicion = permisosEdicion;
    }

    public void publicarProducto(Producto p) { }
    public void editarProducto(Producto p) { }

    @Override public boolean esAdmin() { return true; }
}