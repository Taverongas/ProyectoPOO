package poo.tienda.modelo;

public class Fabrica {
    private int id;
    private String pais;
    private String ciudad;
    private int capacidad;
    private String nivelAutomatizacion;

    public Fabrica(int id, String pais, String ciudad, int capacidad, String nivelAutomatizacion) {
        this.id = id;
        this.pais = pais;
        this.ciudad = ciudad;
        this.capacidad = capacidad;
        this.nivelAutomatizacion = nivelAutomatizacion;
    }
}