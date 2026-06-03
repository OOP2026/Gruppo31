package dao;

import java.util.ArrayList;
import java.util.Date;

public interface SedutaDiLaureaDAO {
    void inserisciSedutaDB(String codice, Date data,
                           String ora, String luogo) throws Exception;

    ArrayList<String[]> leggiSeduteDB() throws Exception;
}