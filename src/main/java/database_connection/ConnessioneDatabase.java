package database_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe di utilità per la gestione della connessione al database PostgreSQL.
 * Fornisce un punto di accesso centralizzato per ottenere una connessione attiva,
 * isolando le credenziali e la stringa di connessione (URL) dal resto della logica applicativa.
 */
public class ConnessioneDatabase {

    // URL di connessione standard per PostgreSQL in esecuzione in locale
    private static final String URL = "jdbc:postgresql://localhost:5432/gestione_lauree";

    // Nome utente di default del database
    private static final String USER = "postgres";

    /**
     * Costruttore privato intenzionale.
     * Essendo una classe di sola utilità che espone metodi statici,
     * impediamo l'istanziazione di oggetti di questo tipo (es. new ConnessioneDatabase())
     * per mantenere il codice pulito ed evitare sprechi di memoria.
     */
    private ConnessioneDatabase() {}

    /**
     * Stabilisce e restituisce una connessione attiva con il database.
     * Implementa una best practice di sicurezza: la password non è scritta "in chiaro"
     * nel codice sorgente, ma viene recuperata dinamicamente dalle variabili d'ambiente
     * del sistema operativo (DB_PASSWORD).
     *
     * @return un oggetto Connection pronto per eseguire query SQL
     * @throws SQLException se la connessione fallisce o se la variabile d'ambiente manca
     */
    public static Connection getConnection() throws SQLException {
        // Recupero sicuro della password di sistema
        String password = System.getenv("DB_PASSWORD");

        // Controllo di sicurezza: impediamo l'accesso se la configurazione dell'ambiente è assente
        if (password == null || password.trim().isEmpty()) {
            throw new SQLException("Errore di sicurezza: Variabile d'ambiente DB_PASSWORD non configurata.");
        }

        // Il DriverManager si occupa di caricare il driver JDBC e stabilire il ponte con PostgreSQL
        return DriverManager.getConnection(URL, USER, password);
    }
}