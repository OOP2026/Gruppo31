package implementazionedao;

import dao.TesiDAO;
import database_connection.ConnessioneDatabase;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe concreta che implementa l'interfaccia TesiDAO per database PostgreSQL.
 * Gestisce tutte le operazioni CRUD (Create, Read, Update, Delete) relative
 * alle tesi e alle prenotazioni di laurea degli studenti.
 */
public class TesiPostgresDao implements TesiDAO {

    /**
     * Salva il record della tesi appena caricata dallo studente.
     * Di default, il DB imposterà lo stato su 'IN_ATTESA' (gestito solitamente a livello di schema SQL).
     * * @param titolo Il titolo della tesi
     * @param percorso Path del file PDF
     * @param usernameStudente Lo username dello studente che fa l'upload
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
     * Inserisce la prenotazione di uno studente a una seduta.
     * Nota furba: tramite sub-query va a pescare automaticamente l'ID dell'ultima tesi
     * caricata da quello studente, così non c'è bisogno di passarlo da fuori.
     * * @param data La data in cui viene effettuata la prenotazione
     * @param usernameStudente Lo username dello studente
     * @param codiceSeduta Il codice della seduta a cui si sta prenotando
     */
    @Override
    public void prenotaSedutaDB(LocalDate data, String usernameStudente, String codiceSeduta) throws SQLException {
        String query = "INSERT INTO prenotazione_laurea (data_prenotazione, studente_username, seduta_codice, tesi_id) " +
                "VALUES (?, ?, ?, (SELECT id FROM tesi WHERE studente_username = ? ORDER BY id DESC LIMIT 1))";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDate(1, java.sql.Date.valueOf(data));
            pstmt.setString(2, usernameStudente);
            pstmt.setString(3, codiceSeduta);
            pstmt.setString(4, usernameStudente); // Parametro per la sub-query
            pstmt.executeUpdate();
        }
    }

    /**
     * Aggiorna lo stato della tesi (ACCETTATO o RIFIUTATO) a seguito della valutazione del prof.
     * Si va a cercare la tesi più recente (MAX(id)) di quello studente per sicurezza.
     * * @param matricola La matricola dello studente valutato
     * @param stato "ACCETTATO" o "RIFIUTATO"
     */
    @Override
    public void valutaTesiDB(String matricola, String stato) throws SQLException {
        String query = "UPDATE tesi SET stato = ? WHERE studente_username = (SELECT username FROM utente WHERE matricola = ?) " +
                "AND id IN (SELECT MAX(id) FROM tesi WHERE studente_username = (SELECT username FROM utente WHERE matricola = ?))";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, stato);
            pstmt.setString(2, matricola);
            pstmt.setString(3, matricola);

            // Se ritorna 0 vuol dire che lo studente non ha caricato nessuna tesi
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Tesi non trovata per matricola: " + matricola);
            }
        }
    }

    /**
     * Recupera tutte le tesi associate agli studenti di cui il professore fa da relatore.
     * La JOIN qui unisce la tesi all'utente e alla richiesta di tirocinio approvata.
     * * @param ssnDocente L'SSN del professore loggato
     * @return Una lista di array di stringhe pronti per riempire la JTable nell'interfaccia
     */
    @Override
    public List<String[]> getTesiPerDocenteDB(String ssnDocente) throws SQLException {
        List<String[]> risultati = new ArrayList<>();
        String query = "SELECT u.matricola, u.nome || ' ' || u.cognome AS studente, t.titolo, t.percorso_file, t.stato " +
                "FROM tesi t JOIN utente u ON t.studente_username = u.username " +
                "JOIN richiesta_tirocinio rt ON u.username = rt.studente_username " +
                "WHERE rt.docente_ssn = ? AND rt.stato = 'ACCETTATO' AND t.id IN (SELECT MAX(id) FROM tesi GROUP BY studente_username)";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, ssnDocente);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    risultati.add(new String[]{
                            rs.getString("matricola"),
                            rs.getString("studente"),
                            rs.getString("titolo"),
                            rs.getString("percorso_file"),
                            rs.getString("stato")
                    });
                }
            }
        }
        return risultati;
    }

    /**
     * Metodo di controllo per evitare che uno studente carichi duemila tesi.
     * Controlla se c'è già una tesi in stato "IN_ATTESA".
     * * @param username Lo username dello studente
     * @return true se ha già una tesi da valutare, false se può procedere
     */
    @Override
    public boolean haTesiInAttesaDB(String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM tesi WHERE studente_username = ? AND stato = 'IN_ATTESA'";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }
}