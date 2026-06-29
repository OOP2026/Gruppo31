package dao;
import java.sql.SQLException;
import java.util.ArrayList;
import model.DatiRegistrazione;

public interface UtenteDAO {
    boolean loginDB(String username, String password, ArrayList<String> userData) throws SQLException;
    void registraUtenteDB(DatiRegistrazione dati) throws SQLException;
}