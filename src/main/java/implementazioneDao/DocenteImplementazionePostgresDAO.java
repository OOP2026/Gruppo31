package implementazioneDao;

import dao.DocenteDAO;
import database_connection.ConnessioneDatabase;

import java.sql.*;
import java.util.ArrayList;

public class DocenteImplementazionePostgresDAO implements DocenteDAO {

    private Connection connection;

    public DocenteImplementazionePostgresDAO() throws SQLException {
        connection = ConnessioneDatabase.getInstance().connection;
    }

    @Override
    public void inserisciDocenteDB(String username, String password,
                                   String email, String nome,
                                   String cognome, String ssn) throws Exception {
        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO docente (username, password, email, nome, cognome, ssn) " +
                        "VALUES (?, ?, ?, ?, ?, ?)"
        );
        ps.setString(1, username);
        ps.setString(2, password);
        ps.setString(3, email);
        ps.setString(4, nome);
        ps.setString(5, cognome);
        ps.setString(6, ssn);
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public ArrayList<String[]> leggiDocentiDB() throws Exception {
        ArrayList<String[]> docenti = new ArrayList<>();
        PreparedStatement ps = connection.prepareStatement(
                "SELECT username, password, email, nome, cognome, ssn FROM docente"
        );
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            docenti.add(new String[]{
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("nome"),
                    rs.getString("cognome"),
                    rs.getString("ssn")
            });
        }
        rs.close();
        ps.close();
        return docenti;
    }
}