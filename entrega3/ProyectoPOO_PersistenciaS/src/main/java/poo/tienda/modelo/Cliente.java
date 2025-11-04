package poo.tienda.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private String direccion;
    private String telefono;
    private List<MetodoPago> metodosPago = new ArrayList<>();

    public Cliente(int id, String nombre, String email, String password,
                   String direccion, String telefono) {
        super(id, nombre, email, password);
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public List<MetodoPago> getMetodosPago() { return metodosPago; }

    public void agregarMetodoPago(MetodoPago mp) {
        metodosPago.add(mp);
    }

    public Carrito getCarritoActivo() {
        return new Carrito();
    }
}
