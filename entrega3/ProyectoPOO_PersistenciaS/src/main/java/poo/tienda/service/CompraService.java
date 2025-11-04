package poo.tienda.service;

import poo.tienda.dao.impl.CompraDBDAO;
import poo.tienda.modelo.Compra;

import java.util.List;

public class CompraService {

    private final CompraDBDAO compraDAO;

    public CompraService(CompraDBDAO compraDAO) {
        this.compraDAO = compraDAO;
    }

    public List<Compra> listarTodas() {
        return compraDAO.findAll();
    }

    public Compra buscarPorId(int id) {
        return compraDAO.findById(id);
    }

    public void registrarCompra(Compra c) {
        compraDAO.save(c);
    }

    public void eliminarCompra(int id) {
        compraDAO.delete(id);
    }
}