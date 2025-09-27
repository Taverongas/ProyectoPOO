package poo.tienda.app;

import poo.tienda.modelo.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static List<Usuario> usuarios = new ArrayList<>();
    static List<Producto> productos = new ArrayList<>();
    static List<Compra> compras = new ArrayList<>();
    static Usuario sesion = null;
    static Carrito carrito = new Carrito();

    public static void main(String[] args) {
        usuarios.add(new AdministradorUsuario("Admin", "admin@tienda.com", "admin", 10));
        ConcejoSombrio concejo = new ConcejoSombrio(1, "Sigma");
        Duena duena = new Duena("Sakura", "sakura@tienda.com", "clave", "CLAVE-001", new Date());
        RegistroEsclavos registro = new RegistroEsclavos(2, 128);

        Categoria c1 = new Categoria(1, "Labiales", "Gama labios");
        Categoria c2 = new Categoria(2, "Rostro", "Bases y polvos");
        productos.add(new Producto(1, "Labial Rojo", "Clásico", new Date(), c1, 25000, 50));
        productos.add(new Producto(2, "Base Mate", "Larga duración", new Date(), c2, 48000, 30));

        int op;
        do {
            menu();
            op = leerInt("Opción: ");
            switch (op) {
                case 1 -> registrarCliente();
                case 2 -> login();
                case 3 -> altaProducto();
                case 4 -> listarProductos();
                case 5 -> agregarAlCarrito();
                case 6 -> verCarrito();
                case 7 -> registrarCompra();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida");
            }
        } while (op != 0);
    }

    static void menu() {
        System.out.println("\n=== MENÚ TIENDA ===");
        System.out.println("Usuario: " + (sesion == null ? "(no autenticado)" :
                sesion.getNombre() + (sesion.esAdmin() ? " [admin]" : "")));
        System.out.println("1. Registrar usuario (Cliente)");
        System.out.println("2. Log-in");
        System.out.println("3. Alta de producto (solo admin)");
        System.out.println("4. Listar productos");
        System.out.println("5. Agregar al carrito");
        System.out.println("6. Ver carrito");
        System.out.println("7. Registrar compra");
        System.out.println("0. Salir");
    }

    static void registrarCliente() {
        System.out.print("Nombre: "); String n = sc.nextLine();
        System.out.print("Email: "); String e = sc.nextLine();
        System.out.print("Password: "); String p = sc.nextLine();
        System.out.print("Dirección: "); String d = sc.nextLine();
        System.out.print("Teléfono: "); String t = sc.nextLine();
        usuarios.add(new Cliente(0, n, e, p, d, t));
        System.out.println("Usuario registrado");
    }

    static void login() {
        System.out.print("Email: "); String e = sc.nextLine();
        System.out.print("Password: "); String p = sc.nextLine();
        for (Usuario u : usuarios) {
            if (u.getEmail().equals(e) && u.getPassword().equals(p) && u.isActivo()) {
                sesion = u;
                System.out.println("Bienvenido " + u.getNombre());
                return;
            }
        }
        System.out.println("Credenciales inválidas o usuario inactivo");
    }

    static void altaProducto() {
        if (sesion == null || !sesion.esAdmin()) {
            System.out.println("Debe iniciar sesión como admin");
            return;
        }
        int id = productos.size() + 1;
        System.out.print("Nombre: "); String nombre = sc.nextLine();
        System.out.print("Descripción: "); String desc = sc.nextLine();
        System.out.print("Categoría texto: "); String cn = sc.nextLine();
        Categoria cat = new Categoria(0, cn, "");
        double precio = leerDouble("Precio: ");
        int stock = leerInt("Stock: ");
        productos.add(new Producto(id, nombre, desc, new Date(), cat, precio, stock));
        System.out.println("Producto agregado");
    }

    static void listarProductos() {
        if (productos.isEmpty()) { System.out.println("(sin productos)"); return; }
        for (Producto p : productos) System.out.println(p);
    }

    static void agregarAlCarrito() {
        listarProductos();
        if (productos.isEmpty()) return;
        int id = leerInt("ID producto: ");
        int cant = leerInt("Cantidad: ");
        for (Producto p : productos) {
            if (p.getId() == id) {
                if (cant <= 0 || cant > p.getStock()) { System.out.println("Cantidad inválida/stock insuficiente"); return; }
                carrito.agregar(p, cant);
                System.out.println("Agregado al carrito");
                return;
            }
        }
        System.out.println("No existe ese producto");
    }

    static void verCarrito() { carrito.ver(); }

    static void registrarCompra() {
        if (!(sesion instanceof Cliente)) { System.out.println("Debe estar logueado como Cliente"); return; }
        if (carrito.estaVacio()) { System.out.println("Carrito vacío"); return; }
        MetodoPago mp = new MetodoPago(1, "EFECTIVO", "N/A", "N/A");
        Compra compra = new Compra(compras.size() + 1, (Cliente) sesion, new Date(), "CREADA", mp);
        for (LineaCarrito lc : carrito.getLineas()) {
            Producto p = lc.getProducto();
            if (lc.getCantidad() > p.getStock()) { System.out.println("Stock insuficiente para " + p.getNombre()); return; }
        }
        for (LineaCarrito lc : carrito.getLineas()) {
            Producto p = lc.getProducto();
            p.setStock(p.getStock() - lc.getCantidad());
            compra.agregarLinea(new LineaCompra(p, lc.getCantidad(), p.getPrecio()));
        }
        compras.add(compra);
        carrito.vaciar();
        System.out.println("Compra registrada. Total: $" + compra.calcularTotal());
    }

    static int leerInt(String pr) { System.out.print(pr); return Integer.parseInt(sc.nextLine().trim()); }
    static double leerDouble(String pr) { System.out.print(pr); return Double.parseDouble(sc.nextLine().trim()); }
}



