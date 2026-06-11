package implementazionedao;

import dao.TesiDAO;
import database_connection.ConnessioneDatabase;
import java.sql.*;
import java.util.Date;

/**
 * Implementazione concreta dell'interfaccia TesiDAO per PostgreSQL.
 * Gestisce le operazioni di persistenza legate all'elaborato finale:
 * caricamento del file, prenotazione alla seduta e valutazione da parte del relatore.
 */
public class TesiPostgresDao implements TesiDAO {

    /**
     * Salva i dati relativi all'elaborato caricato dallo studente nel database.
     * Corrisponde all'azione 'caricaTesi' della classe Studente.
     *
     * @param titolo           il titolo della tesi
     * @param percorso         il percorso fisico del file caricato
     * @param usernameStudente lo username univoco dello studente autore
     * @throws SQLException in caso di errore di comunicazione con il database
     */
    @Override
    public void caricaTesiDB(String titolo, String percorso, String usernameStudente) throws SQLException {
        String query = "INSERT INTO tesi (titolo, percorso_file, studente_username) VALUES (?, ?, ?)";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, titolo);
            pstmt.setString(2, percorso);
            pstmt.setString(3, usernameStudente);

            pstmt.executeUpdate();
        }
    }

    /**
     * Registra la prenotazione di uno studente a una specifica seduta di laurea.
     * Corrisponde all'azione 'prenotaSedutaLaurea' della classe Studente.
     * La query utilizza una subquery intelligente per trovare e associare automaticamente
     * l'ID dell'ultima tesi caricata a sistema da quello specifico studente.
     *
     * @param data             la data in cui viene effettuata la prenotazione
     * @param usernameStudente lo username dello studente che si prenota
     * @param codiceSeduta     il codice identificativo della seduta scelta
     * @throws SQLException in caso di errori (es. tesi inesistente o vincoli violati)
     */
    @Override
    public void prenotaSedutaDB(Date data, String usernameStudente, String codiceSeduta) throws SQLException {
        // La subquery seleziona l'ultima tesi inserita (ORDER BY id DESC LIMIT 1) per quello studente
        String query = "INSERT INTO prenotazione_laurea (data_prenotazione, studente_username, seduta_codice, tesi_id) VALUES (?, ?, ?, (SELECT id FROM tesi WHERE studente_username = ? ORDER BY id DESC LIMIT 1))";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, new java.sql.Date(data.getTime()));
            pstmt.setString(2, usernameStudente);
            pstmt.setString(3, codiceSeduta);
            pstmt.setString(4, usernameStudente); // Parametro per la subquery

            pstmt.executeUpdate();
        }
    }

    /**
     * Aggiorna lo stato di valutazione della tesi nel database.
     * Corrisponde all'azione 'valutaTesi' effettuata dal Docente/Relatore.
     * Poiché il docente conosce la matricola, la query fa una join logica per risalire
     * allo username dell'utente a partire proprio dalla matricola.
     *
     * @param matricola la matricola dello studente di cui si valuta la tesi
     * @param stato     il nuovo esito (es. ACCETTATO, RIFIUTATO)
     * @throws SQLException se la matricola non esiste o se nessuna tesi viene trovata
     */
    @Override
    public void valutaTesiDB(String matricola, String stato) throws SQLException {
        String query = "UPDATE tesi SET stato = ? WHERE studente_username = (SELECT username FROM utente WHERE matricola = ?)";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, stato);
            pstmt.setString(2, matricola);

            // Se executeUpdate restituisce 0, significa che nessuna riga è stata modificata
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Tesi non trovata per matricola: " + matricola);
            }
        }
    }
}