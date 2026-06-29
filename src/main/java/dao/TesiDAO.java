package dao;
import java.sql.SQLException;
import java.util.Date;

public interface TesiDAO {
    void caricaTesiDB(String titolo, String percorso, String usernameStudente) throws SQLException;
    void prenotaSedutaDB(Date data, String usernameStudente, String codiceSeduta) throws SQLException;
    void valutaTesiDB(String matricola, String stato) throws SQLException;
    java.util.List<String[]> getTesiPerDocenteDB(String ssnDocente) throws SQLException;
}