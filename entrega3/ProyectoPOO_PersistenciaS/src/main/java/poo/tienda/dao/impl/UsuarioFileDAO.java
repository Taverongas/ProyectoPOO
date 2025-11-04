package poo.tienda.dao.impl;

import poo.tienda.modelo.Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioFileDAO {

    private final File archivo;
    private List<Usuario> datos = new ArrayList<>();

    public UsuarioFileDAO(String rutaArchivo) {
        this.archivo = new File(rutaArchivo);
        cargar();
    }

    @SuppressWarnings("unchecked")
    private void cargar() {
        if (!archivo.exists()) {
            datos = new ArrayList<>();
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            datos = (List<Usuario>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            datos = new ArrayList<>();
        }
    }

    private void guardar() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(datos);
        } catch (IOException e) {
            throw new RuntimeException("Error guardando usuarios", e);
        }
    }

    public List<Usuario> findAll() {
        return new ArrayList<>(datos);
    }

    public Usuario findById(int id) {
        return datos.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Usuario findByEmail(String email) {
        return datos.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    public void save(Usuario u) {

        Usuario existente = findById(u.getId());
        if (existente != null) {
            datos.remove(existente);
        }
        datos.add(u);
        guardar();
    }

    public void delete(int id) {
        datos.removeIf(u -> u.getId() == id);
        guardar();
    }
}