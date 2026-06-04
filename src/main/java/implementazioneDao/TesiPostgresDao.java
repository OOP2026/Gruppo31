package implementazioneDao;

import dao.TesiDAO;
import database_connection.ConnessioneDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class TesiPostgresDao implements TesiDAO {

    @Override
    public void caricaTesiDB(String titolo, String percorso, String usernameStudente) throws SQLException {
        String query = "INSERT INTO tesi (titolo, percorso_file, studente_username) VALUES (?, ?, ?)";
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, titolo);
            pstmt.setString(2, percorso);
            pstmt.setString(3, usernameStudente);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void prenotaSedutaDB(Date data, String usernameStudente, String codiceSeduta) throws SQLException {
        // Usa una subquery per prendere l'ID dell'ultima tesi caricata dallo studente
        String query = "INSERT INTO prenotazione_laurea (data_prenotazione, studente_username, seduta_codice, tesi_id) " +
                "VALUES (?, ?, ?, (SELECT id FROM tesi WHERE studente_username = ? ORDER BY id DESC LIMIT 1))";
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDate(1, new java.sql.Date(data.getTime()));
            pstmt.setString(2, usernameStudente);
            pstmt.setString(3, codiceSeduta);
            pstmt.setString(4, usernameStudente);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void valutaTesiDB(String matricola, String stato) throws SQLException {
        String query = "UPDATE tesi SET stato = ? WHERE studente_username = (SELECT username FROM utente WHERE matricola = ?)";
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, stato);
            pstmt.setString(2, matricola);

            // Eseguiamo e contiamo le righe modificate
            int righeModificate = pstmt.executeUpdate();

            // Se ha aggiornato 0 righe, blocchiamo tutto e lanciamo l'errore
            if (righeModificate == 0) {
                throw new SQLException("Nessuna tesi trovata nel sistema per la matricola: " + matricola);
            }
        }
    }
}