package implementazioneDao;

import dao.TirocinioDAO;
import database_connection.ConnessioneDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class TirocinioPostgresDao implements TirocinioDAO {

    @Override
    public void aggiungiTirocinioDB(int id, String argomento) throws SQLException {
        String query = "INSERT INTO tirocinio (id, argomento) VALUES (?, ?)";
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, argomento);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void richiediTirocinioDB(Date data, String usernameStudente, String ssnDocente, int idTirocinio) throws SQLException {
        String query = "INSERT INTO richiesta_tirocinio (data_richiesta, studente_username, docente_ssn, tirocinio_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDate(1, new java.sql.Date(data.getTime()));
            pstmt.setString(2, usernameStudente);
            pstmt.setString(3, ssnDocente);
            pstmt.setInt(4, idTirocinio);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void valutaRichiestaDB(String matricola, int idTirocinio, String stato) throws SQLException {
        // Aggiungiamo il controllo sull'ID tirocinio nella query
        String query = "UPDATE richiesta_tirocinio SET stato = ? " +
                "WHERE studente_username = (SELECT username FROM utente WHERE matricola = ?) " +
                "AND tirocinio_id = ?";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, stato);
            pstmt.setString(2, matricola);
            pstmt.setInt(3, idTirocinio); // Impostiamo il terzo parametro

            int righeModificate = pstmt.executeUpdate();

            if (righeModificate == 0) {
                throw new SQLException("Nessuna richiesta trovata per matricola " + matricola + " e tirocinio ID " + idTirocinio);
            }
        }
    }
}