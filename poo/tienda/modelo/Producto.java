package poo.tienda.modelo;

import java.util.Date;

public class Producto {
    private int id;
    private String nombre;
    private String descripcion;
    private Date fechaActualizacion;
    private Categoria categoria;
    private double precio;
    private int stock;

    public Producto(int id, String nombre, String descripcion, Date fechaActualizacion,
                    Categoria categoria, double precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaActualizacion = fechaActualizacion;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    @Override
    public String toString() {
        String cat = (categoria == null ? "(sin categoría)" : categoria.getNombre());
        return id + " | " + nombre + " | " + cat + " | $" + precio + " | stock=" + stock;
    }
}