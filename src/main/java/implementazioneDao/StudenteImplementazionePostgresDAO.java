package implementazioneDao;

import dao.StudenteDAO;
import database_connection.ConnessioneDatabase;

import java.sql.*;
import java.util.ArrayList;

public class StudenteImplementazionePostgresDAO implements StudenteDAO {

    private Connection connection;

    public StudenteImplementazionePostgresDAO() throws SQLException {
        connection = ConnessioneDatabase.getInstance().connection;
    }

    @Override
    public void inserisciStudenteDB(String username, String password,
                                    String email, String nome,
                                    String cognome, String matricola) throws Exception {
        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO studente (username, password, email, nome, cognome, matricola) " +
                        "VALUES (?, ?, ?, ?, ?, ?)"
        );
        ps.setString(1, username);
        ps.setString(2, password);
        ps.setString(3, email);
        ps.setString(4, nome);
        ps.setString(5, cognome);
        ps.setString(6, matricola);
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public ArrayList<String[]> leggiStudentiDB() throws Exception {
        ArrayList<String[]> studenti = new ArrayList<>();
        PreparedStatement ps = connection.prepareStatement(
                "SELECT username, password, email, nome, cognome, matricola FROM studente"
        );
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            studenti.add(new String[]{
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("nome"),
                    rs.getString("cognome"),
                    rs.getString("matricola")
            });
        }
        rs.close();
        ps.close();
        return studenti;
    }
}