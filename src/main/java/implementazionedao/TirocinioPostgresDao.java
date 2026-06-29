package implementazionedao;
import dao.TirocinioDAO;
import database_connection.ConnessioneDatabase;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TirocinioPostgresDao implements TirocinioDAO {
    @Override
    public void aggiungiTirocinioDB(int id, String argomento, String ssnDocente) throws SQLException {
        String query = "INSERT INTO tirocinio (id, argomento, is_esterno, docente_ssn) VALUES (?, ?, false, ?)";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, argomento);
            pstmt.setString(3, ssnDocente);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void aggiungiTirocinioEsternoDB(int id, String argomento, String azienda, String referenteAziendale, String ssnDocente) throws SQLException {
        String query = "INSERT INTO tirocinio (id, argomento, azienda, referente_aziendale, is_esterno, docente_ssn) VALUES (?, ?, ?, ?, true, ?)";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, argomento);
            pstmt.setString(3, azienda);
            pstmt.setString(4, referenteAziendale);
            pstmt.setString(5, ssnDocente);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void richiediTirocinioDB(LocalDate data, String userS, String ssnD, int idT) throws SQLException {
        String query = "INSERT INTO richiesta_tirocinio (data_richiesta, studente_username, docente_ssn, tirocinio_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDate(1, java.sql.Date.valueOf(data));
            pstmt.setString(2, userS);
            pstmt.setString(3, ssnD);
            pstmt.setInt(4, idT);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void valutaRichiestaDB(String matricola, int idT, String stato) throws SQLException {
        String query = "UPDATE richiesta_tirocinio SET stato = ? WHERE studente_username = (SELECT username FROM utente WHERE matricola = ?) AND tirocinio_id = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, stato);
            pstmt.setString(2, matricola);
            pstmt.setInt(3, idT);
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<String[]> getTirociniDisponibiliDB() throws SQLException {
        List<String[]> risultati = new ArrayList<>();
        String query = "SELECT t.id, t.argomento, t.is_esterno, t.azienda, t.docente_ssn, u.nome, u.cognome FROM tirocinio t JOIN utente u ON t.docente_ssn = u.ssn";
        try (Connection conn = ConnessioneDatabase.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String tipo = rs.getBoolean("is_esterno") ? "Esterno (" + rs.getString("azienda") + ")" : "Interno";
                String prof = rs.getString("nome") + " " + rs.getString("cognome");
                risultati.add(new String[]{String.valueOf(rs.getInt("id")), rs.getString("argomento"), tipo, rs.getString("docente_ssn"), prof});
            }
        }
        return risultati;
    }

    @Override
    public List<String[]> getRichiesteStudenteDB(String username) throws SQLException {
        List<String[]> risultati = new ArrayList<>();
        String query = "SELECT t.argomento, u.nome || ' ' || u.cognome AS relatore, r.stato FROM richiesta_tirocinio r JOIN tirocinio t ON r.tirocinio_id = t.id JOIN utente u ON r.docente_ssn = u.ssn WHERE r.studente_username = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) risultati.add(new String[]{rs.getString("argomento"), rs.getString("relatore"), rs.getString("stato")});
            }
        }
        return risultati;
    }

    @Override
    public List<String[]> getRichiestePerDocenteDB(String ssnDocente) throws SQLException {
        List<String[]> risultati = new ArrayList<>();
        String query = "SELECT u.matricola, u.nome || ' ' || u.cognome AS studente, t.id, t.argomento, r.stato FROM richiesta_tirocinio r JOIN utente u ON r.studente_username = u.username JOIN tirocinio t ON r.tirocinio_id = t.id WHERE r.docente_ssn = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, ssnDocente);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) risultati.add(new String[]{rs.getString("matricola"), rs.getString("studente"), String.valueOf(rs.getInt("id")), rs.getString("argomento"), rs.getString("stato")});
            }
        }
        return risultati;
    }

    @Override
    public boolean haTirocinioApprovatoDB(String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM richiesta_tirocinio WHERE studente_username = ? AND stato = 'ACCETTATO'";
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