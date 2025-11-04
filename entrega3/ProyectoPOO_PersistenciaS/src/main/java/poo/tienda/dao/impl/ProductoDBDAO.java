package poo.tienda.dao.impl;

import poo.tienda.db.ConnectionManager;
import poo.tienda.modelo.Categoria;
import poo.tienda.modelo.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDBDAO {

    public List<Producto> findAll() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion, fecha_actualizacion, " +
                "categoria_nombre, categoria_descripcion, precio, stock " +
                "FROM productos";

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public Producto findById(int idBuscado) {
        String sql = "SELECT id, nombre, descripcion, fecha_actualizacion, " +
                "categoria_nombre, categoria_descripcion, precio, stock " +
                "FROM productos WHERE id = ?";

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idBuscado);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void save(Producto p) {
        Producto existente = findById(p.getId());
        if (existente == null) {
            insertar(p);
        } else {
            actualizar(p);
        }
    }

    private void insertar(Producto p) {
        String sql = "INSERT INTO productos " +
                "(id, nombre, descripcion, fecha_actualizacion, " +
                " categoria_nombre, categoria_descripcion, precio, stock) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, p.getId());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion());
            ps.setTimestamp(4, new Timestamp(p.getFechaActualizacion().getTime()));
            ps.setString(5, p.getCategoria().getNombre());
            ps.setString(6, p.getCategoria().getDescripcion());
            ps.setDouble(7, p.getPrecio());
            ps.setInt(8, p.getStock());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void actualizar(Producto p) {
        String sql = "UPDATE productos SET " +
                "nombre = ?, descripcion = ?, fecha_actualizacion = ?, " +
                "categoria_nombre = ?, categoria_descripcion = ?, " +
                "precio = ?, stock = ? " +
                "WHERE id = ?";

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setTimestamp(3, new Timestamp(p.getFechaActualizacion().getTime()));
            ps.setString(4, p.getCategoria().getNombre());
            ps.setString(5, p.getCategoria().getDescripcion());
            ps.setDouble(6, p.getPrecio());
            ps.setInt(7, p.getStock());
            ps.setInt(8, p.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Producto mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nombre = rs.getString("nombre");
        String descripcion = rs.getString("descripcion");
        Timestamp ts = rs.getTimestamp("fecha_actualizacion");
        java.util.Date fecha = (ts != null) ? new java.util.Date(ts.getTime()) : new java.util.Date();
        String catNombre = rs.getString("categoria_nombre");
        String catDesc = rs.getString("categoria_descripcion");
        double precio = rs.getDouble("precio");
        int stock = rs.getInt("stock");

        Categoria categoria = new Categoria(id, catNombre, catDesc);
        return new Producto(id, nombre, descripcion, fecha, categoria, precio, stock);
    }
}