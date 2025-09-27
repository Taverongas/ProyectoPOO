package poo.tienda.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Compra {
    private int id;
    private Date fecha;
    private String estado;
    private List<LineaCompra> lineas = new ArrayList<>();
    private Cliente cliente;
    private MetodoPago metodoPago;

    public Compra(int id, Cliente cliente, Date fecha, String estado, MetodoPago metodoPago) {
        this.id = id;
        this.cliente = cliente;
        this.fecha = fecha;
        this.estado = estado;
        this.metodoPago = metodoPago;
    }

    public void agregarLinea(LineaCompra l) { lineas.add(l); }
    public double calcularTotal() {
        double t = 0;
        for (LineaCompra l : lineas) t += l.getSubtotal();
        return t;
    }

    @Override
    public String toString() {
        return "Compra#" + id + " (" + estado + ") " + fecha;
    }
}