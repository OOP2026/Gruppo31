package implementazionedao;
import dao.TesiDAO;
import database_connection.ConnessioneDatabase;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TesiPostgresDao implements TesiDAO {
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

    @Override
    public void prenotaSedutaDB(LocalDate data, String usernameStudente, String codiceSeduta) throws SQLException {
        String query = "INSERT INTO prenotazione_laurea (data_prenotazione, studente_username, seduta_codice, tesi_id) VALUES (?, ?, ?, (SELECT id FROM tesi WHERE studente_username = ? ORDER BY id DESC LIMIT 1))";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDate(1, java.sql.Date.valueOf(data));
            pstmt.setString(2, usernameStudente);
            pstmt.setString(3, codiceSeduta);
            pstmt.setString(4, usernameStudente);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void valutaTesiDB(String matricola, String stato) throws SQLException {
        String query = "UPDATE tesi SET stato = ? WHERE studente_username = (SELECT username FROM utente WHERE matricola = ?) AND id IN (SELECT MAX(id) FROM tesi WHERE studente_username = (SELECT username FROM utente WHERE matricola = ?))";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, stato);
            pstmt.setString(2, matricola);
            pstmt.setString(3, matricola);
            if (pstmt.executeUpdate() == 0) throw new SQLException("Tesi non trovata per matricola: " + matricola);
        }
    }

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
                while (rs.next()) risultati.add(new String[]{rs.getString("matricola"), rs.getString("studente"), rs.getString("titolo"), rs.getString("percorso_file"), rs.getString("stato")});
            }
        }
        return risultati;
    }

    // --- NUOVO METODO PER IL BLOCCO DELLE TESI MULTIPLE ---
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