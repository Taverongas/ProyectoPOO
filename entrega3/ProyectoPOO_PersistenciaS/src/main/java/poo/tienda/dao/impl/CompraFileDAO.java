package poo.tienda.dao.impl;

import poo.tienda.modelo.Compra;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CompraFileDAO {

    private final File archivo;
    private List<Compra> datos = new ArrayList<>();

    public CompraFileDAO(String rutaArchivo) {
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
            datos = (List<Compra>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            datos = new ArrayList<>();
        }
    }

    private void guardar() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(datos);
        } catch (IOException e) {
            throw new RuntimeException("Error guardando compras", e);
        }
    }

    public List<Compra> findAll() {
        return new ArrayList<>(datos);
    }

    public Compra findById(int id) {
        return datos.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void save(Compra c) {
        Compra existente = findById(c.getId());
        if (existente != null) {
            datos.remove(existente);
        }
        datos.add(c);
        guardar();
    }

    public void delete(int id) {
        datos.removeIf(c -> c.getId() == id);
        guardar();
    }
}