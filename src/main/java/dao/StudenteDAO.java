package dao;

import java.util.ArrayList;

public interface StudenteDAO {
    void inserisciStudenteDB(String username, String password,
                             String email, String nome,
                             String cognome, String matricola) throws Exception;

    ArrayList<String[]> leggiStudentiDB() throws Exception;
}