package database_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnessioneDatabase {
    private static final String URL = "jdbc:postgresql://localhost:5432/gestione_lauree";
    private static final String USER = "postgres";

    private ConnessioneDatabase() {}

    public static Connection getConnection() throws SQLException {
        String password = System.getenv("DB_PASSWORD");
        if (password == null || password.trim().isEmpty()) {
            throw new SQLException("Errore di sicurezza: Variabile d'ambiente DB_PASSWORD non configurata.");
        }
        return DriverManager.getConnection(URL, USER, password);
    }
}