package implementazionedao;

import dao.SedutaDAO;
import database_connection.ConnessioneDatabase;
import java.sql.*;
import java.util.Date;

/**
 * Implementazione concreta dell'interfaccia SedutaDAO per PostgreSQL.
 * Questa classe si occupa di tradurre le azioni sulle sedute di laurea
 * (es. creazione, assegnazione commissione) in vere e proprie query SQL
 * da inviare al database.
 */
public class SedutaPostgresDao implements SedutaDAO {

    /**
     * Inserisce una nuova seduta di laurea all'interno del database.
     * Corrisponde all'azione 'inserisciSeduta' del Coordinatore nel Model.
     *
     * @param data   la data della seduta (java.util.Date, convertita in java.sql.Date per il DB)
     * @param ora    l'orario di inizio
     * @param luogo  l'aula o il luogo fisico/virtuale
     * @param codice il codice identificativo univoco della seduta (chiave primaria)
     * @throws SQLException se c'è un errore di connessione o un vincolo violato nel DB
     */
    @Override
    public void inserisciSedutaDB(Date data, String ora, String luogo, String codice) throws SQLException {
        String query = "INSERT INTO seduta_laurea (codice, data_seduta, ora, luogo) VALUES (?, ?, ?, ?)";

        // Utilizziamo il try-with-resources per garantire la chiusura sicura di Connection e PreparedStatement
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, codice);
            // Conversione necessaria per adattare la data di Java al formato richiesto da SQL
            pstmt.setDate(2, new java.sql.Date(data.getTime()));
            pstmt.setString(3, ora);
            pstmt.setString(4, luogo);

            pstmt.executeUpdate();
        }
    }

    /**
     * Associa un professore a una specifica seduta di laurea inserendolo nella commissione.
     * Corrisponde all'azione 'aggiungiDocenteACommissione' del Coordinatore nel Model.
     * Popola la tabella associativa (o di giunzione) 'commissione' nel database.
     *
     * @param ssnD il codice fiscale (SSN) del docente
     * @param codS il codice identificativo della seduta di laurea
     * @throws SQLException se il docente o la seduta non esistono (violazione di chiave esterna)
     */
    @Override
    public void aggiungiDocenteACommissioneDB(String ssnD, String codS) throws SQLException {
        String query = "INSERT INTO commissione (seduta_codice, docente_ssn) VALUES (?, ?)";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, codS);
            pstmt.setString(2, ssnD);

            pstmt.executeUpdate();
        }
    }

    @Override
    public boolean verificaDocenteValidoPerCommissione(String ssnDocente, String codiceSeduta) throws SQLException {
        // Conta quante tesi APPROVATE ci sono in questa seduta in cui il docente fa da relatore (tramite la richiesta di tirocinio approvata)
        String query = "SELECT COUNT(*) FROM prenotazione_laurea pl " +
                "JOIN tesi t ON pl.studente_username = t.studente_username " +
                "JOIN richiesta_tirocinio rt ON t.studente_username = rt.studente_username " +
                "WHERE pl.seduta_codice = ? AND rt.docente_ssn = ? AND t.stato = 'ACCETTATO' AND rt.stato = 'ACCETTATO'";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, codiceSeduta);
            pstmt.setString(2, ssnDocente);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Ritorna true se il docente ha almeno uno studente valido
                }
            }
        }
        return false;
    }

    @Override
    public java.util.List<String[]> getStudentiPerSedutaDB(String codiceSeduta) throws SQLException {
        java.util.List<String[]> risultati = new java.util.ArrayList<>();
        String query = "SELECT u.matricola, u.nome, u.cognome, t.titolo " +
                "FROM prenotazione_laurea p " +
                "JOIN utente u ON p.studente_username = u.username " +
                "JOIN tesi t ON p.tesi_id = t.id " +
                "WHERE p.seduta_codice = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, codiceSeduta);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    risultati.add(new String[]{rs.getString("matricola"), rs.getString("nome"), rs.getString("cognome"), rs.getString("titolo")});
                }
            }
        }
        return risultati;
    }
}