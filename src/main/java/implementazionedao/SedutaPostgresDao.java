package implementazionedao;
import dao.SedutaDAO;
import database_connection.ConnessioneDatabase;
import java.sql.*;
import java.util.Date;

public class SedutaPostgresDao implements SedutaDAO {
    @Override
    public void inserisciSedutaDB(Date data, String ora, String luogo, String codice) throws SQLException {
        String query = "INSERT INTO seduta_laurea (codice, data_seduta, ora, luogo) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, codice);
            pstmt.setDate(2, new java.sql.Date(data.getTime()));
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
}