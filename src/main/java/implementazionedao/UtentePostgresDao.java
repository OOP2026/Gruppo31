package implementazionedao;

import dao.UtenteDAO;
import database_connection.ConnessioneDatabase;
import model.DatiRegistrazione;
import java.sql.*;
import java.util.ArrayList;

/**
 * Gestisce l'accesso e la registrazione degli utenti nel DB.
 */
public class UtentePostgresDao implements UtenteDAO {

    /**
     * Verifica le credenziali al login e, se corrette, popola la lista userData
     * con tutti i dati anagrafici e il ruolo per istanziare l'utente corretto.
     * * @return true se username e password matchano, false altrimenti
     */
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

    /**
     * Inserisce un nuovo utente nel sistema.
     * Gestisce i campi null (es. un prof non ha matricola, uno studente non ha l'SSN).
     */
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

            // Se è un docente non ha matricola, se è studente non ha SSN
            if (dati.getMatricola() == null || dati.getMatricola().trim().isEmpty()) pstmt.setNull(7, java.sql.Types.VARCHAR);
            else pstmt.setString(7, dati.getMatricola());

            if (dati.getSsn() == null || dati.getSsn().trim().isEmpty()) pstmt.setNull(8, java.sql.Types.VARCHAR);
            else pstmt.setString(8, dati.getSsn());

            pstmt.executeUpdate();
        }
    }
}