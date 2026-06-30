package dao;
import java.sql.SQLException;
import java.util.ArrayList;
import model.DatiRegistrazione;

/**
 * Interfaccia per la registrazione e l'autenticazione degli utenti.
 */
public interface UtenteDAO {
    boolean loginDB(String username, String password, ArrayList<String> userData) throws SQLException;
    void registraUtenteDB(DatiRegistrazione dati) throws SQLException;
}