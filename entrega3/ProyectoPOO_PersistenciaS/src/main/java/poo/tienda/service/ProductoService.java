package poo.tienda.service;

import poo.tienda.dao.impl.ProductoDBDAO;
import poo.tienda.modelo.Producto;

import java.util.List;

public class ProductoService {

    private final ProductoDBDAO productoDAO;

    public ProductoService(ProductoDBDAO productoDAO) {
        this.productoDAO = productoDAO;
    }

    public List<Producto> listarTodos() {
        return productoDAO.findAll();
    }

    public Producto buscarPorId(int id) {
        return productoDAO.findById(id);
    }

    public void registrarProducto(Producto p) {
        productoDAO.save(p);
    }

    public void eliminarProducto(int id) {
        productoDAO.delete(id);
    }
}