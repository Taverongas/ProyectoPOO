package poo.tienda.service;

import poo.tienda.dao.impl.UsuarioDBDAO;
import poo.tienda.modelo.Usuario;

import java.util.List;

public class UsuarioService {

    private final UsuarioDBDAO usuarioDAO;

    public UsuarioService(UsuarioDBDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public void registrarUsuario(Usuario u) {
        usuarioDAO.save(u);
    }

    public List<Usuario> listarTodos() {
        return usuarioDAO.findAll();
    }

    public Usuario buscarPorId(int id) {
        return usuarioDAO.findById(id);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioDAO.findByEmail(email);
    }

    public Usuario login(String email, String password) {
        Usuario u = usuarioDAO.findByEmail(email);
        if (u != null && u.getPassword().equals(password) && u.isActivo()) {
            return u;
        }
        return null;
    }
}