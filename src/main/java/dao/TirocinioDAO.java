package dao;
import java.sql.SQLException;
import java.util.Date;

public interface TirocinioDAO {
    void aggiungiTirocinioDB(int id, String argomento, String ssnDocente) throws SQLException;
    void aggiungiTirocinioEsternoDB(int id, String argomento, String azienda, String referenteAziendale, String ssnDocente) throws SQLException;
    void richiediTirocinioDB(Date data, String usernameStudente, String ssnDocente, int idTirocinio) throws SQLException;
    void valutaRichiestaDB(String matricola, int idTirocinio, String stato) throws SQLException;
    java.util.List<String[]> getTirociniDisponibiliDB() throws SQLException;
    java.util.List<String[]> getRichiesteStudenteDB(String username) throws SQLException;
    java.util.List<String[]> getRichiestePerDocenteDB(String ssnDocente) throws SQLException;
    boolean haTirocinioApprovatoDB(String username) throws SQLException; // NUOVO
}