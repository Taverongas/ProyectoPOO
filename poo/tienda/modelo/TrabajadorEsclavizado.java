package poo.tienda.modelo;

import java.util.Date;

public class TrabajadorEsclavizado {
    private int id;
    private String nombre;
    private String pais;
    private int edad;
    private Date fechaCaptura;
    private String salud;
    private Fabrica asignada;

    public TrabajadorEsclavizado(int id, String nombre, String pais, int edad, Date fechaCaptura, String salud, Fabrica asignada) {
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
        this.edad = edad;
        this.fechaCaptura = fechaCaptura;
        this.salud = salud;
        this.asignada = asignada;
    }
}