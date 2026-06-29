package implementazionedao;
import dao.UtenteDAO;
import database_connection.ConnessioneDatabase;
import model.DatiRegistrazione;
import java.sql.*;
import java.util.ArrayList;

public class UtentePostgresDao implements UtenteDAO {
    @Override
    public boolean loginDB(String username, String password, ArrayList<String> userData) throws SQLException {
        String query = "SELECT * FROM utente WHERE username = ? AND password = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    userData.add(rs.getString("ruolo"));
                    userData.add(rs.getString("email"));
                    userData.add(rs.getString("nome"));
                    userData.add(rs.getString("cognome"));
                    userData.add(rs.getString("matricola"));
                    userData.add(rs.getString("ssn"));
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void registraUtenteDB(DatiRegistrazione dati) throws SQLException {
        String query = "INSERT INTO utente (username, password, email, nome, cognome, ruolo, matricola, ssn) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, dati.getUsername());
            pstmt.setString(2, dati.getPassword());
            pstmt.setString(3, dati.getEmail());
            pstmt.setString(4, dati.getNome());
            pstmt.setString(5, dati.getCognome());
            pstmt.setString(6, dati.getRuolo());

            if (dati.getMatricola() == null || dati.getMatricola().trim().isEmpty()) pstmt.setNull(7, java.sql.Types.VARCHAR);
            else pstmt.setString(7, dati.getMatricola());

            if (dati.getSsn() == null || dati.getSsn().trim().isEmpty()) pstmt.setNull(8, java.sql.Types.VARCHAR);
            else pstmt.setString(8, dati.getSsn());
            pstmt.executeUpdate();
        }
    }
}