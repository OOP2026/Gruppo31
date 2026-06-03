package dao;

import java.util.ArrayList;

public interface TesiDAO {
    void inserisciTesiDB(String titolo, String percorsoFile,
                         String matricolaStudente) throws Exception;

    ArrayList<String[]> leggiTesiDB() throws Exception;
}