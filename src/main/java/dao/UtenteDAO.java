package dao;

import java.sql.SQLException;
import java.util.ArrayList;

public interface UtenteDAO {
    boolean loginDB(String username, String password, ArrayList<String> userData) throws SQLException;
}