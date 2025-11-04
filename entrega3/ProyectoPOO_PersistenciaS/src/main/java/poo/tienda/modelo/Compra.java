package poo.tienda.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Compra implements Serializable {
    private static final long serialVersionUID = 1L;

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

    public int getId() { return id; }
    public Date getFecha() { return fecha; }
    public String getEstado() { return estado; }
    public Cliente getCliente() { return cliente; }
    public MetodoPago getMetodoPago() { return metodoPago; }

    public void setEstado(String estado) { this.estado = estado; }

    public void agregarLinea(LineaCompra l) { lineas.add(l); }
    public List<LineaCompra> getLineas() { return lineas; }

    public double calcularTotal() {
        double t = 0;
        for (LineaCompra l : lineas) t += l.getSubtotal();
        return t;
    }

    @Override
    public String toString() {
        return "Compra#" + id + " (" + estado + ") " + fecha + " total=$" + calcularTotal();
    }
}