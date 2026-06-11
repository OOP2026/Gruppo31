package implementazionedao;

import dao.TirocinioDAO;
import database_connection.ConnessioneDatabase;
import java.sql.*;
import java.util.Date;

/**
 * Implementazione concreta dell'interfaccia TirocinioDAO per PostgreSQL.
 * Si occupa di gestire tutte le operazioni sul database relative ai tirocini:
 * la pubblicazione di nuove offerte da parte dei docenti, le candidature degli studenti
 * e la successiva valutazione (approvazione/rifiuto) delle richieste.
 */
public class TirocinioPostgresDao implements TirocinioDAO {

    /**
     * Inserisce un nuovo argomento di tirocinio nel database.
     * Corrisponde all'azione 'aggiungiTirocinio' del Docente nel Model.
     *
     * @param id        l'identificativo numerico univoco del tirocinio
     * @param argomento la descrizione o il titolo dell'attività proposta
     * @throws SQLException in caso di errore di connessione o se l'ID è già esistente (violazione Primary Key)
     */
    @Override
    public void aggiungiTirocinioDB(int id, String argomento) throws SQLException {
        String query = "INSERT INTO tirocinio (id, argomento) VALUES (?, ?)";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            pstmt.setString(2, argomento);

            pstmt.executeUpdate();
        }
    }

    /**
     * Registra nel database la richiesta di uno studente per un determinato tirocinio.
     * Corrisponde all'azione 'richiediTirocinio' dello Studente nel Model.
     * Crea un record nella tabella di associazione 'richiesta_tirocinio'.
     *
     * @param data  la data in cui lo studente invia la candidatura
     * @param userS lo username dello studente (user Studente)
     * @param ssnD  il codice fiscale del docente relatore (SSN Docente)
     * @param idT   l'identificativo del tirocinio scelto (ID Tirocinio)
     * @throws SQLException in caso di problemi con il database o chiavi esterne non valide
     */
    @Override
    public void richiediTirocinioDB(Date data, String userS, String ssnD, int idT) throws SQLException {
        String query = "INSERT INTO richiesta_tirocinio (data_richiesta, studente_username, docente_ssn, tirocinio_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Conversione della data da java.util a java.sql
            pstmt.setDate(1, new java.sql.Date(data.getTime()));
            pstmt.setString(2, userS);
            pstmt.setString(3, ssnD);
            pstmt.setInt(4, idT);

            pstmt.executeUpdate();
        }
    }

    /**
     * Aggiorna lo stato di una richiesta di tirocinio (es. da IN_ATTESA ad ACCETTATO).
     * Corrisponde all'azione 'valutaRichiesta' del Docente nel Model.
     * Utilizza una subquery per tradurre la matricola fornita dal docente nello username dello studente.
     *
     * @param matricola la matricola dello studente candidato
     * @param idT       l'identificativo del tirocinio per cui ci si è candidati
     * @param stato     il nuovo stato assegnato (es. ACCETTATO, RIFIUTATO)
     * @throws SQLException se la richiesta non viene trovata o c'è un errore SQL
     */
    @Override
    public void valutaRichiestaDB(String matricola, int idT, String stato) throws SQLException {
        String query = "UPDATE richiesta_tirocinio SET stato = ? WHERE studente_username = (SELECT username FROM utente WHERE matricola = ?) AND tirocinio_id = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, stato);
            pstmt.setString(2, matricola);
            pstmt.setInt(3, idT);

            // Verifichiamo se l'update ha effettivamente modificato una riga
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Richiesta non trovata! Controlla la matricola e l'ID del tirocinio.");
            }
        }
    }
}