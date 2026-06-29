package implementazionedao;
import dao.SedutaDAO;
import database_connection.ConnessioneDatabase;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SedutaPostgresDao implements SedutaDAO {
    @Override
    public void inserisciSedutaDB(LocalDate data, String ora, String luogo, String codice) throws SQLException {
        String query = "INSERT INTO seduta_laurea (codice, data_seduta, ora, luogo) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, codice);
            pstmt.setDate(2, java.sql.Date.valueOf(data));
            pstmt.setString(3, ora);
            pstmt.setString(4, luogo);
            pstmt.executeUpdate();
        }
    }

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
        String query = "SELECT COUNT(*) FROM prenotazione_laurea pl JOIN tesi t ON pl.studente_username = t.studente_username JOIN richiesta_tirocinio rt ON t.studente_username = rt.studente_username WHERE pl.seduta_codice = ? AND rt.docente_ssn = ? AND t.stato = 'ACCETTATO' AND rt.stato = 'ACCETTATO'";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, codiceSeduta);
            pstmt.setString(2, ssnDocente);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    @Override
    public List<String[]> getStudentiPerSedutaDB(String codiceSeduta) throws SQLException {
        List<String[]> risultati = new ArrayList<>();
        String query = "SELECT u.matricola, u.nome, u.cognome, t.titolo FROM prenotazione_laurea p JOIN utente u ON p.studente_username = u.username JOIN tesi t ON p.tesi_id = t.id WHERE p.seduta_codice = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, codiceSeduta);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) risultati.add(new String[]{rs.getString("matricola"), rs.getString("nome"), rs.getString("cognome"), rs.getString("titolo")});
            }
        }
        return risultati;
    }

    @Override
    public List<String[]> getSeduteDisponibiliDB() throws SQLException {
        List<String[]> risultati = new ArrayList<>();
        String query = "SELECT codice, data_seduta, ora, luogo FROM seduta_laurea";
        try (Connection conn = ConnessioneDatabase.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) risultati.add(new String[]{rs.getString("codice"), rs.getDate("data_seduta").toString(), rs.getString("ora"), rs.getString("luogo")});
        }
        return risultati;
    }

    @Override
    public List<String[]> getTuttiDocentiDB() throws SQLException {
        List<String[]> risultati = new ArrayList<>();
        // FIX SONAR & CASE-SENSITIVE: Usiamo UPPER per evitare disallineamenti tra 'Docente' e 'DOCENTE'
        String query = "SELECT ssn, nome, cognome FROM utente WHERE UPPER(ruolo) = 'DOCENTE' OR UPPER(ruolo) = 'COORDINATORE'";
        try (Connection conn = ConnessioneDatabase.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                risultati.add(new String[]{rs.getString("ssn"), rs.getString("nome"), rs.getString("cognome")});
            }
        }
        return risultati;
    }

    @Override
    public List<String[]> getDocentiPerCommissioneDB(String codiceSeduta) throws SQLException {
        List<String[]> risultati = new ArrayList<>();
        String query = "SELECT u.ssn, u.nome, u.cognome FROM commissione c JOIN utente u ON c.docente_ssn = u.ssn WHERE c.seduta_codice = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, codiceSeduta);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    risultati.add(new String[]{rs.getString("ssn"), rs.getString("nome"), rs.getString("cognome")});
                }
            }
        }
        return risultati;
    }

    // --- NUOVO METODO IMPLEMENTATO ---
    @Override
    public boolean esisteDocenteDB(String ssnDocente) throws SQLException {
        String query = "SELECT COUNT(*) FROM utente WHERE ssn = ? AND (UPPER(ruolo) = 'DOCENTE' OR UPPER(ruolo) = 'COORDINATORE')";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, ssnDocente);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }
}