package implementazioneDao;
import dao.UtenteDAO;
import database_connection.ConnessioneDatabase;
import java.sql.*;
import java.util.ArrayList;

public class UtentePostgresDao implements UtenteDAO {
    @Override
    public boolean loginDB(String username, String password, ArrayList<String> userData) throws SQLException {
        String query = "SELECT * FROM utente WHERE username = ? AND password = ?";
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
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
                    userData.add(rs.getString("corso_laurea"));
                    return true;
                }
            }
        }
        return false;
    }
}