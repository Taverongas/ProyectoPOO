package poo.tienda.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static final String URL  = "jdbc:postgresql://localhost:5432/TiendaPOO";
    private static final String USER = "postgres";      // OJO: con 's' al final
    private static final String PASS = "jetara0811";    // la que ya pusimos en ALTER USER

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("No se encontró el driver de PostgreSQL en el classpath");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}