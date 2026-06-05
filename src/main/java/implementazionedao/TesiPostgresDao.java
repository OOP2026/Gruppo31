package implementazionedao;
import dao.TesiDAO;
import database_connection.ConnessioneDatabase;
import java.sql.*;
import java.util.Date;

public class TesiPostgresDao implements TesiDAO {
    @Override
    public void caricaTesiDB(String t, String p, String user) throws SQLException {
        String query = "INSERT INTO tesi (titolo, percorso_file, studente_username) VALUES (?, ?, ?)";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, t); pstmt.setString(2, p); pstmt.setString(3, user);
            pstmt.executeUpdate();
        }
    }
    @Override
    public void prenotaSedutaDB(Date d, String user, String cod) throws SQLException {
        String query = "INSERT INTO prenotazione_laurea (data_prenotazione, studente_username, seduta_codice, tesi_id) VALUES (?, ?, ?, (SELECT id FROM tesi WHERE studente_username = ? ORDER BY id DESC LIMIT 1))";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDate(1, new java.sql.Date(d.getTime()));
            pstmt.setString(2, user); pstmt.setString(3, cod); pstmt.setString(4, user);
            pstmt.executeUpdate();
        }
    }
    @Override
    public void valutaTesiDB(String mat, String stato) throws SQLException {
        String query = "UPDATE tesi SET stato = ? WHERE studente_username = (SELECT username FROM utente WHERE matricola = ?)";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, stato); pstmt.setString(2, mat);
            if (pstmt.executeUpdate() == 0) throw new SQLException("Tesi non trovata!");
        }
    }
}