package database_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe di utilità per gestire la connessione al database PostgreSQL.
 * Il suo unico scopo è fornirci un "ponte" (Connection) verso il database,
 * tenendo separate le credenziali dalla logica del programma.
 */
public class ConnessioneDatabase {

    private static final String URL = "jdbc:postgresql://localhost:5432/gestione_lauree";
    private static final String USER = "postgres";

    /**
     * Costruttore privato.
     * Essendo una classe con solo metodi statici, non ha senso istanziarla
     * con "new ConnessioneDatabase()". Quindi lo blocchiamo per avere codice pulito.
     */
    private ConnessioneDatabase() {}

    /**
     * Stabilisce la connessione col DB pescando la password dalle variabili di sistema.
     * Questa è una chicca di sicurezza: non scriviamo la password in chiaro nel codice!
     * * @return L'oggetto Connection per lanciare le query
     * @throws SQLException Se il DB è spento, irraggiungibile o manca la password
     */
    public static Connection getConnection() throws SQLException {
        String password = System.getenv("DB_PASSWORD");
        if (password == null || password.trim().isEmpty()) {
            throw new SQLException("Errore di sicurezza: Variabile d'ambiente DB_PASSWORD non configurata.");
        }
        return DriverManager.getConnection(URL, USER, password);
    }
}