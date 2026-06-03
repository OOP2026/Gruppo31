package implementazioneDao;

import dao.TesiDAO;
import database_connection.ConnessioneDatabase;

import java.sql.*;
import java.util.ArrayList;

public class TesiImplementazionePostgresDAO implements TesiDAO {

    private Connection connection;

    public TesiImplementazionePostgresDAO() throws SQLException {
        connection = ConnessioneDatabase.getInstance().connection;
    }

    @Override
    public void inserisciTesiDB(String titolo, String percorsoFile,
                                String matricolaStudente) throws Exception {
        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO tesi (titolo, percorso_file, matricola_studente) " +
                        "VALUES (?, ?, ?)"
        );
        ps.setString(1, titolo);
        ps.setString(2, percorsoFile);
        ps.setString(3, matricolaStudente);
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public ArrayList<String[]> leggiTesiDB() throws Exception {
        ArrayList<String[]> tesi = new ArrayList<>();
        PreparedStatement ps = connection.prepareStatement(
                "SELECT titolo, percorso_file, matricola_studente FROM tesi"
        );
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            tesi.add(new String[]{
                    rs.getString("titolo"),
                    rs.getString("percorso_file"),
                    rs.getString("matricola_studente")
            });
        }
        rs.close();
        ps.close();
        return tesi;
    }
}