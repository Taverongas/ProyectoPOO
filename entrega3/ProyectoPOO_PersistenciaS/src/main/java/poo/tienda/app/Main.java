package poo.tienda.app;

import poo.tienda.dao.impl.CompraDBDAO;
import poo.tienda.dao.impl.ProductoDBDAO;
import poo.tienda.dao.impl.UsuarioDBDAO;
import poo.tienda.modelo.*;
import poo.tienda.service.CompraService;
import poo.tienda.service.ProductoService;
import poo.tienda.service.UsuarioService;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    private static final String ADMIN_EMAIL = "admin@eia.edu.co";
    private static final String ADMIN_PASS  = "admin";

    private static final UsuarioService usuarioService =
            new UsuarioService(new UsuarioDBDAO());
    private static final ProductoService productoService =
            new ProductoService(new ProductoDBDAO());
    private static final CompraService compraService =
            new CompraService(new CompraDBDAO());


    private static Usuario sesion = null;
    private static Carrito carrito = new Carrito();

    public static void main(String[] args) {

        cargarProductosDemo();

        crearAdminPorDefecto();

        while (true) {
            if (sesion == null) {
                menuSinSesion();
            } else {
                menuConSesion();
            }
        }
    }



    private static void menuSinSesion() {
        System.out.println("\n=== TIENDA SAKURA ===");
        System.out.println("1. Registrarse");
        System.out.println("2. Iniciar sesión");
        System.out.println("3. Listar productos");
        System.out.println("0. Salir");

        int op = leerEntero("Opción: ");
        switch (op) {
            case 1 -> registrarUsuario();
            case 2 -> iniciarSesion();
            case 3 -> listarProductos();
            case 0 -> salir();
            default -> System.out.println("Opción inválida.");
        }
    }

    private static void menuConSesion() {
        System.out.println("\n=== MENÚ PRINCIPAL (Sesión: " + sesion.getNombre() + ") ===");

        if (esAdmin()) {
            System.out.println(">> Modo ADMIN <<");
            System.out.println("1. Listar productos");
            System.out.println("2. Agregar producto al carrito");
            System.out.println("3. Ver carrito");
            System.out.println("4. Confirmar compra");
            System.out.println("5. Cerrar sesión");
            System.out.println("6. Crear producto (ADMIN)");
            System.out.println("7. Listar usuarios (ADMIN)");
            System.out.println("8. Cambiar rol admin (ADMIN)");
            System.out.println("0. Salir");
        } else {
            System.out.println("1. Listar productos");
            System.out.println("2. Agregar producto al carrito");
            System.out.println("3. Ver carrito");
            System.out.println("4. Confirmar compra");
            System.out.println("5. Cerrar sesión");
            System.out.println("0. Salir");
        }

        int op = leerEntero("Opción: ");

        if (esAdmin()) {
            switch (op) {
                case 1 -> listarProductos();
                case 2 -> agregarProductoAlCarrito();
                case 3 -> verCarrito();
                case 4 -> confirmarCompra();
                case 5 -> cerrarSesion();
                case 6 -> crearProductoAdmin();
                case 7 -> listarUsuariosAdmin();
                case 8 -> cambiarRolAdmin();
                case 0 -> salir();
                default -> System.out.println("Opción inválida.");
            }
        } else {
            switch (op) {
                case 1 -> listarProductos();
                case 2 -> agregarProductoAlCarrito();
                case 3 -> verCarrito();
                case 4 -> confirmarCompra();
                case 5 -> cerrarSesion();
                case 0 -> salir();
                default -> System.out.println("Opción inválida.");
            }
        }
    }



    private static void registrarUsuario() {
        System.out.println("\n=== Registro de usuario ===");
        String nombre = leerTexto("Nombre: ");
        String email = leerTexto("Email: ");
        String password = leerTexto("Contraseña: ");

        // Validación básica de email
        if (email.isBlank() || !email.contains("@")) {
            System.out.println("Email inválido.");
            return;
        }

        if (usuarioService.buscarPorEmail(email) != null) {
            System.out.println("Ya existe un usuario con ese email.");
            return;
        }

        int id = usuarioService.listarTodos().size() + 1;
        Usuario u = new Usuario(id, nombre, email, password);
        usuarioService.registrarUsuario(u);

        System.out.println("Usuario registrado con id " + id);
    }

    private static void iniciarSesion() {
        System.out.println("\n=== Iniciar sesión ===");
        String email = leerTexto("Email: ");
        String password = leerTexto("Contraseña: ");

        Usuario u = usuarioService.login(email, password);
        if (u == null) {
            System.out.println("Credenciales inválidas o usuario inactivo.");
        } else {
            sesion = u;
            carrito = new Carrito();
            System.out.println("Bienvenido, " + u.getNombre() + "!");
        }
    }

    private static void cerrarSesion() {
        sesion = null;
        carrito = new Carrito();
        System.out.println("Sesión cerrada.");
    }

    private static void crearAdminPorDefecto() {
        Usuario existente = usuarioService.buscarPorEmail(ADMIN_EMAIL);
        if (existente == null) {
            int id = usuarioService.listarTodos().size() + 1;
            Usuario admin = new Usuario(id, "Admin", ADMIN_EMAIL, ADMIN_PASS);
            admin.setAdmin(true);
            usuarioService.registrarUsuario(admin);
            System.out.println("Admin creado por defecto: " + ADMIN_EMAIL + " / " + ADMIN_PASS);
        } else if (!existente.isAdmin()) {
            existente.setAdmin(true);
            usuarioService.registrarUsuario(existente);
        }
    }

    private static boolean esAdmin() {
        return sesion != null && sesion.isAdmin();
    }

    private static void listarUsuariosAdmin() {
        System.out.println("\n=== Usuarios registrados ===");
        List<Usuario> usuarios = usuarioService.listarTodos();
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios.");
            return;
        }
        for (Usuario u : usuarios) {
            String rol = u.isAdmin() ? "ADMIN" : "USUARIO";
            System.out.println(u.getId() + " - " + u.getNombre()
                    + " (" + u.getEmail() + ") - " + rol);
        }
    }

    private static void cambiarRolAdmin() {
        if (!esAdmin()) {
            System.out.println("No tienes permisos para esta acción.");
            return;
        }
        listarUsuariosAdmin();
        int id = leerEntero("\nID del usuario a cambiar rol: ");
        Usuario u = usuarioService.buscarPorId(id);
        if (u == null) {
            System.out.println("No existe usuario con ese ID.");
            return;
        }
        if (u.getId() == sesion.getId()) {
            System.out.println("No puedes cambiar tu propio rol.");
            return;
        }

        boolean nuevoRol = !u.isAdmin();
        u.setAdmin(nuevoRol);
        usuarioService.registrarUsuario(u);

        System.out.println("Ahora el usuario " + u.getNombre()
                + " es: " + (nuevoRol ? "ADMIN" : "USUARIO"));
    }



    private static void cargarProductosDemo() {
        List<Producto> productos = productoService.listarTodos();
        if (!productos.isEmpty()) {
            return; // ya hay productos guardados en la BD
        }

        System.out.println("Cargando productos demo...");

        Categoria labiales = new Categoria(1, "Labiales", "Labiales de lujo asequible");
        Categoria sombras = new Categoria(2, "Sombras", "Paletas de sombras Glow Up");

        Producto p1 = new Producto(1, "Labial Sakura Red",
                "Rojo intenso de larga duración",
                new Date(), labiales, 35_000, 100);

        Producto p2 = new Producto(2, "Paleta Consejo Sombrío",
                "Sombras oscuras y misteriosas",
                new Date(), sombras, 80_000, 50);

        productoService.registrarProducto(p1);
        productoService.registrarProducto(p2);
    }

    private static void listarProductos() {
        System.out.println("\n=== Productos disponibles ===");
        List<Producto> productos = productoService.listarTodos();
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }
        for (Producto p : productos) {
            String nombreCat = (p.getCategoria() != null) ? p.getCategoria().getNombre() : "N/A";
            System.out.println(p.getId() + " - " + p.getNombre()
                    + " | $" + p.getPrecio()
                    + " | Stock: " + p.getStock()
                    + " | Categoría: " + nombreCat);
        }
    }

    private static void agregarProductoAlCarrito() {
        listarProductos();
        int idProd = leerEntero("ID del producto a agregar: ");
        int cant = leerEntero("Cantidad: ");

        Producto p = productoService.buscarPorId(idProd);
        if (p == null) {
            System.out.println("No existe un producto con ese ID.");
            return;
        }
        if (cant <= 0) {
            System.out.println("La cantidad debe ser positiva.");
            return;
        }
        if (cant > p.getStock()) {
            System.out.println("No hay suficiente stock. Disponible: " + p.getStock());
            return;
        }

        carrito.agregar(p, cant);
        System.out.println("Producto agregado al carrito.");
    }

    private static void crearProductoAdmin() {
        if (!esAdmin()) {
            System.out.println("No tienes permisos para esta acción.");
            return;
        }
        System.out.println("\n=== Crear producto (ADMIN) ===");
        String nombre = leerTexto("Nombre: ");
        String descripcion = leerTexto("Descripción: ");
        double precio = Double.parseDouble(leerTexto("Precio: "));
        int stock = leerEntero("Stock: ");
        String nombreCat = leerTexto("Nombre categoría: ");
        String descCat = leerTexto("Descripción categoría: ");

        int idProducto = productoService.listarTodos().size() + 1;
        int idCategoria = idProducto;

        Categoria cat = new Categoria(idCategoria, nombreCat, descCat);
        Producto p = new Producto(idProducto, nombre, descripcion, new Date(), cat, precio, stock);
        productoService.registrarProducto(p);

        System.out.println("Producto creado con id " + idProducto);
    }

    private static void verCarrito() {
        System.out.println("\n=== Carrito ===");
        if (carrito.estaVacio()) {
            System.out.println("El carrito está vacío.");
            return;
        }
        for (LineaCarrito l : carrito.getLineas()) {
            System.out.println(l.getProducto().getId() + " - " + l.getProducto().getNombre()
                    + " x" + l.getCantidad()
                    + " = $" + l.getSubtotal());
        }
        System.out.println("Total: $" + carrito.total());
    }



    private static void confirmarCompra() {
        if (sesion == null) {
            System.out.println("Debes iniciar sesión para comprar.");
            return;
        }
        if (carrito.estaVacio()) {
            System.out.println("El carrito está vacío.");
            return;
        }

        System.out.println("\n=== Confirmar compra ===");

        // ======= VALIDAR MÉTODO DE PAGO =======
        String tipoPago;
        while (true) {
            System.out.println("Métodos de pago permitidos:");
            System.out.println("1. Efectivo");
            System.out.println("2. Transferencia");
            int op = leerEntero("Opción: ");

            if (op == 1) {
                tipoPago = "Efectivo";
                break;
            } else if (op == 2) {
                tipoPago = "Transferencia";
                break;
            } else {
                System.out.println("Método de pago no válido. Solo se permite Efectivo o Transferencia.");
            }
        }
        // =====================================

        MetodoPago mp = new MetodoPago(1, tipoPago, sesion.getNombre(), "N/A");

        Cliente cliente = new Cliente(
                sesion.getId(),
                sesion.getNombre(),
                sesion.getEmail(),
                sesion.getPassword(),
                "",
                ""
        );

        int idCompra = compraService.listarTodas().size() + 1;
        Compra compra = new Compra(idCompra, cliente, new Date(), "PAGADA", mp);

        for (LineaCarrito lc : carrito.getLineas()) {
            LineaCompra linea = new LineaCompra(
                    lc.getProducto(),
                    lc.getCantidad(),
                    lc.getProducto().getPrecio()
            );
            compra.agregarLinea(linea);
        }

        compraService.registrarCompra(compra);

        // Actualizar stock en BD
        for (LineaCarrito lc : carrito.getLineas()) {
            Producto p = lc.getProducto();
            p.setStock(p.getStock() - lc.getCantidad());
            productoService.registrarProducto(p);
        }

        carrito.vaciar();
        System.out.println("Compra registrada con id " + compra.getId());
    }


    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = sc.nextLine();
            try {
                return Integer.parseInt(linea.trim());
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un número válido.");
            }
        }
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }

    private static void salir() {
        System.out.println("¡Hasta luego!");
        System.exit(0);
    }
}