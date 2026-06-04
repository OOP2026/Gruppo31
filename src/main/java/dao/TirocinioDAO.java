package dao;
import java.sql.SQLException;
import java.util.Date;

public interface TirocinioDAO {
    void aggiungiTirocinioDB(int id, String argomento) throws SQLException;
    void richiediTirocinioDB(Date data, String usernameStudente, String ssnDocente, int idTirocinio) throws SQLException;
    void valutaRichiestaDB(String matricola, int idTirocinio, String stato) throws SQLException;}