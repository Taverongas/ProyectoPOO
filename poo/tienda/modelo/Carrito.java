package poo.tienda.modelo;

import java.util.ArrayList;
import java.util.List;

public class Carrito {
    private int id;
    private String fechaCreacion = "";
    private List<LineaCarrito> lineas = new ArrayList<>();

    public void agregar(Producto p, int cantidad) { lineas.add(new LineaCarrito(p, cantidad)); }
    public void quitar(int productoId) { lineas.removeIf(l -> l.getProducto().getId() == productoId); }
    public void vaciar() { lineas.clear(); }
    public List<LineaCarrito> getLineas() { return lineas; }
    public boolean estaVacio() { return lineas.isEmpty(); }
    public double total() {
        double t = 0;
        for (LineaCarrito l : lineas) t += l.getSubtotal();
        return t;
    }

    public void ver() {
        if (lineas.isEmpty()) { System.out.println("(carrito vacío)"); return; }
        for (LineaCarrito l : lineas)
            System.out.println(l.getProducto().getNombre() + " x" + l.getCantidad() + " = $" + l.getSubtotal());
        System.out.println("TOTAL: $" + total());
    }
}