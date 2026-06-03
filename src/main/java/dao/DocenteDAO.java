package dao;

import java.util.ArrayList;

public interface DocenteDAO {
    void inserisciDocenteDB(String username, String password,
                            String email, String nome,
                            String cognome, String ssn) throws Exception;

    ArrayList<String[]> leggiDocentiDB() throws Exception;
}