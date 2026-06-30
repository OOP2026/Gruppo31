package dao;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Interfaccia per gestire le proposte di tirocinio dei docenti e
 * le relative richieste fatte dagli studenti.
 */
public interface TirocinioDAO {
    void aggiungiTirocinioDB(int id, String argomento, String ssnDocente) throws SQLException;
    void aggiungiTirocinioEsternoDB(int id, String argomento, String azienda, String referenteAziendale, String ssnDocente) throws SQLException;
    void richiediTirocinioDB(LocalDate data, String usernameStudente, String ssnDocente, int idTirocinio) throws SQLException;
    void valutaRichiestaDB(String matricola, int idTirocinio, String stato) throws SQLException;
    List<String[]> getTirociniDisponibiliDB() throws SQLException;
    List<String[]> getRichiesteStudenteDB(String username) throws SQLException;
    List<String[]> getRichiestePerDocenteDB(String ssnDocente) throws SQLException;
    boolean haTirocinioApprovatoDB(String username) throws SQLException;
}