package dao;

import java.sql.SQLException;
import java.util.Date;

public interface SedutaDAO {
    void inserisciSedutaDB(Date data, String ora, String luogo, String codice) throws SQLException;
    void aggiungiDocenteACommissioneDB(String ssnDocente, String codiceSeduta) throws SQLException;
}