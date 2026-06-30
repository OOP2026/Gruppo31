package dao;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Interfaccia per la gestione delle sedute e delle commissioni.
 */
public interface SedutaDAO {
    void inserisciSedutaDB(LocalDate data, String ora, String luogo, String codice) throws SQLException;
    void aggiungiDocenteACommissioneDB(String ssnDocente, String codiceSeduta) throws SQLException;
    boolean verificaDocenteValidoPerCommissione(String ssnDocente, String codiceSeduta) throws SQLException;
    List<String[]> getStudentiPerSedutaDB(String codiceSeduta) throws SQLException;
    List<String[]> getSeduteDisponibiliDB() throws SQLException;
    List<String[]> getTuttiDocentiDB() throws SQLException;
    List<String[]> getDocentiPerCommissioneDB(String codiceSeduta) throws SQLException;
    boolean esisteDocenteDB(String ssnDocente) throws SQLException;
}