package dao;
import java.sql.SQLException;
import java.util.Date;

public interface SedutaDAO {
    void inserisciSedutaDB(Date data, String ora, String luogo, String codice) throws SQLException;
    void aggiungiDocenteACommissioneDB(String ssnDocente, String codiceSeduta) throws SQLException;
    boolean verificaDocenteValidoPerCommissione(String ssnDocente, String codiceSeduta) throws SQLException;
    java.util.List<String[]> getStudentiPerSedutaDB(String codiceSeduta) throws SQLException;
    java.util.List<String[]> getSeduteDisponibiliDB() throws SQLException;
    java.util.List<String[]> getTuttiDocentiDB() throws SQLException;
    java.util.List<String[]> getDocentiPerCommissioneDB(String codiceSeduta) throws SQLException;
}