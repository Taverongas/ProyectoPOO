package poo.tienda.modelo;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario {
    private String direccion;
    private String telefono;
    private List<MetodoPago> metodoPago = new ArrayList<>();

    public Cliente(int id, String nombre, String email, String password, String direccion, String telefono) {
        super(id, nombre, email, password);
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public Carrito getCarritoActivo() { return new Carrito(); }
    public String getNombre() { return nombre; }
}