package poo.tienda.dao.impl;

import poo.tienda.modelo.Producto;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoFileDAO {

    private final File archivo;
    private List<Producto> datos = new ArrayList<>();

    public ProductoFileDAO(String rutaArchivo) {
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
            datos = (List<Producto>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            datos = new ArrayList<>();
        }
    }

    private void guardar() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(datos);
        } catch (IOException e) {
            throw new RuntimeException("Error guardando productos", e);
        }
    }

    public List<Producto> findAll() {
        return new ArrayList<>(datos);
    }

    public Producto findById(int id) {
        return datos.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void save(Producto p) {
        Producto existente = findById(p.getId());
        if (existente != null) {
            datos.remove(existente);
        }
        datos.add(p);
        guardar();
    }

    public void delete(int id) {
        datos.removeIf(p -> p.getId() == id);
        guardar();
    }
}