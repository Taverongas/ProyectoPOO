package poo.tienda.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RegistroEsclavos {
    private int id;
    private Date ultimoAcceso;
    private int nivelCifrado;
    private List<TrabajadorEsclavizado> lista = new ArrayList<>();

    public RegistroEsclavos(int id, int nivelCifrado) {
        this.id = id;
        this.nivelCifrado = nivelCifrado;
        this.ultimoAcceso = new Date();
    }

    public void agregar(TrabajadorEsclavizado t) {
        lista.add(t);
        ultimoAcceso = new Date();
    }
}