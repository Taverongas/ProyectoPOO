package poo.tienda.dao.impl;

import poo.tienda.db.ConnectionManager;
import poo.tienda.modelo.Cliente;
import poo.tienda.modelo.Compra;
import poo.tienda.modelo.LineaCompra;
import poo.tienda.modelo.MetodoPago;
import poo.tienda.modelo.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CompraDBDAO {

    public List<Compra> findAll() {
        List<Compra> lista = new ArrayList<>();
        String sql = "SELECT id, cliente_nombre, cliente_email, fecha, estado, metodo_pago FROM compras";

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String clienteNombre = rs.getString("cliente_nombre");
                String clienteEmail = rs.getString("cliente_email");
                Timestamp tsFecha = rs.getTimestamp("fecha");
                Date fecha = tsFecha != null ? new Date(tsFecha.getTime()) : new Date();
                String estado = rs.getString("estado");
                String metodoPagoStr = rs.getString("metodo_pago");

                Cliente cliente = new Cliente(id, clienteNombre, clienteEmail, "", "", "");
                MetodoPago mp = new MetodoPago(1, metodoPagoStr, clienteNombre, "");
                Compra compra = new Compra(id, cliente, fecha, estado, mp);

                lista.add(compra);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public Compra findById(int idBuscado) {
        String sqlCompra = "SELECT id, cliente_nombre, cliente_email, fecha, estado, metodo_pago " +
                "FROM compras WHERE id = ?";

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlCompra)) {

            ps.setInt(1, idBuscado);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                int id = rs.getInt("id");
                String clienteNombre = rs.getString("cliente_nombre");
                String clienteEmail = rs.getString("cliente_email");
                Timestamp tsFecha = rs.getTimestamp("fecha");
                Date fecha = tsFecha != null ? new Date(tsFecha.getTime()) : new Date();
                String estado = rs.getString("estado");
                String metodoPagoStr = rs.getString("metodo_pago");

                Cliente cliente = new Cliente(id, clienteNombre, clienteEmail, "", "", "");
                MetodoPago mp = new MetodoPago(1, metodoPagoStr, clienteNombre, "");
                Compra compra = new Compra(id, cliente, fecha, estado, mp);

                // Cargar líneas de compra
                String sqlLineas = "SELECT producto_id, producto_nombre, cantidad, precio_unitario " +
                        "FROM lineas_compra WHERE compra_id = ?";

                try (PreparedStatement psLineas = con.prepareStatement(sqlLineas)) {
                    psLineas.setInt(1, id);

                    try (ResultSet rsL = psLineas.executeQuery()) {
                        while (rsL.next()) {
                            int prodId = rsL.getInt("producto_id");
                            String prodNombre = rsL.getString("producto_nombre");
                            int cantidad = rsL.getInt("cantidad");
                            double precioUnit = rsL.getDouble("precio_unitario");

                            Producto producto = new Producto(prodId, prodNombre, "", new Date(), null, precioUnit, 0);
                            LineaCompra lc = new LineaCompra(producto, cantidad, precioUnit);
                            compra.agregarLinea(lc);
                        }
                    }
                }

                return compra;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void save(Compra c) {
        Compra existente = findById(c.getId());
        if (existente == null) {
            insertar(c);
        } else {
            actualizar(c);
        }
    }

    private void insertar(Compra c) {
        String sqlCompra = "INSERT INTO compras (id, cliente_nombre, cliente_email, fecha, estado, metodo_pago) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlCompra)) {

            ps.setInt(1, c.getId());
            ps.setString(2, c.getCliente().getNombre());
            ps.setString(3, c.getCliente().getEmail());
            ps.setTimestamp(4, new Timestamp(c.getFecha().getTime()));
            ps.setString(5, c.getEstado());
            ps.setString(6, c.getMetodoPago().getTipo());

            ps.executeUpdate();

            insertarLineas(con, c);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void actualizar(Compra c) {
        String sqlCompra = "UPDATE compras SET cliente_nombre = ?, cliente_email = ?, " +
                "fecha = ?, estado = ?, metodo_pago = ? WHERE id = ?";

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlCompra)) {

            ps.setString(1, c.getCliente().getNombre());
            ps.setString(2, c.getCliente().getEmail());
            ps.setTimestamp(3, new Timestamp(c.getFecha().getTime()));
            ps.setString(4, c.getEstado());
            ps.setString(5, c.getMetodoPago().getTipo());
            ps.setInt(6, c.getId());

            ps.executeUpdate();

            borrarLineas(con, c.getId());
            insertarLineas(con, c);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertarLineas(Connection con, Compra c) throws SQLException {
        String sqlLinea = "INSERT INTO lineas_compra " +
                "(compra_id, producto_id, producto_nombre, cantidad, precio_unitario) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement psLinea = con.prepareStatement(sqlLinea)) {
            for (LineaCompra lc : c.getLineas()) {
                psLinea.setInt(1, c.getId());
                psLinea.setInt(2, lc.getProducto().getId());
                psLinea.setString(3, lc.getProducto().getNombre());
                psLinea.setInt(4, lc.getCantidad());
                psLinea.setDouble(5, lc.getPrecioUnitario());
                psLinea.addBatch();
            }
            psLinea.executeBatch();
        }
    }

    private void borrarLineas(Connection con, int compraId) throws SQLException {
        String sql = "DELETE FROM lineas_compra WHERE compra_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, compraId);
            ps.executeUpdate();
        }
    }

    public void delete(int id) {
        try (Connection con = ConnectionManager.getConnection()) {
            borrarLineas(con, id);
            String sql = "DELETE FROM compras WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}