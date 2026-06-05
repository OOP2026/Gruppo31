package database_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnessioneDatabase {

    private static final String URL = "jdbc:postgresql://localhost:5432/gestione_lauree";
    private static final String USER = "postgres";

    private ConnessioneDatabase() {}

    public static Connection getConnection() throws SQLException {

        // Legge la password dalle variabili d'ambiente
        String password = System.getenv("DB_PASSWORD");

        // Se la variabile d'ambiente non c'è o è vuota...
        if (password == null || password.trim().isEmpty()) {
            // 2. LANCIAMO SUBITO L'ERRORE (Nessun piano B insicuro)
            throw new SQLException("Errore di sicurezza: Variabile d'ambiente DB_PASSWORD non configurata. Impossibile connettersi al database.");
        }

        // Se ha trovato la variabile d'ambiente, si connette in modo sicuro
        return DriverManager.getConnection(URL, USER, password);
    }
}