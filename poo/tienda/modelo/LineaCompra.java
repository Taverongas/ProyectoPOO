package poo.tienda.modelo;

public class LineaCompra {
    private Producto producto;
    private int cantidad;
    private double precioUnitario;

    public LineaCompra(Producto producto, int cantidad, double precioUnitario) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public double getSubtotal() { return cantidad * precioUnitario; }
}