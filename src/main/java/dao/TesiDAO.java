package dao;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface TesiDAO {
    void caricaTesiDB(String titolo, String percorso, String usernameStudente) throws SQLException;
    void prenotaSedutaDB(LocalDate data, String usernameStudente, String codiceSeduta) throws SQLException;
    void valutaTesiDB(String matricola, String stato) throws SQLException;
    List<String[]> getTesiPerDocenteDB(String ssnDocente) throws SQLException;
    boolean haTesiInAttesaDB(String username) throws SQLException; // NUOVO METODO DI CONTROLLO
}