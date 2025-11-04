package poo.tienda.dao.impl;

import poo.tienda.db.ConnectionManager;
import poo.tienda.modelo.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDBDAO {

    public List<Usuario> findAll() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, email, password, activo, admin FROM usuarios";

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

    public Usuario findById(int id) {
        String sql = "SELECT id, nombre, email, password, activo, admin FROM usuarios WHERE id = ?";
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

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

    public Usuario findByEmail(String email) {
        String sql = "SELECT id, nombre, email, password, activo, admin FROM usuarios WHERE email = ?";
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);

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

    /**
     * Guarda el usuario:
     * - Si no existe (por id) => INSERT
     * - Si existe           => UPDATE
     */
    public void save(Usuario u) {
        Usuario existente = findById(u.getId());
        if (existente == null) {
            insertar(u);
        } else {
            actualizar(u);
        }
    }

    private void insertar(Usuario u) {
        String sql = "INSERT INTO usuarios (nombre, email, password, activo, admin) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPassword());
            ps.setBoolean(4, u.isActivo());
            ps.setBoolean(5, u.isAdmin());

            ps.executeUpdate();

            // Si quieres, puedes guardar el id generado en el objeto
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int nuevoId = rs.getInt(1);
                    // Si tu clase Usuario tiene setId, puedes descomentar:
                    // u.setId(nuevoId);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void actualizar(Usuario u) {
        String sql = "UPDATE usuarios " +
                "SET nombre = ?, email = ?, password = ?, activo = ?, admin = ? " +
                "WHERE id = ?";

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPassword());
            ps.setBoolean(4, u.isActivo());
            ps.setBoolean(5, u.isAdmin());
            ps.setInt(6, u.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Usuario mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nombre = rs.getString("nombre");
        String email = rs.getString("email");
        String password = rs.getString("password");
        boolean activo = rs.getBoolean("activo");
        boolean admin = rs.getBoolean("admin");

        Usuario u = new Usuario(id, nombre, email, password);
        u.setActivo(activo);
        u.setAdmin(admin);
        return u;
    }
}