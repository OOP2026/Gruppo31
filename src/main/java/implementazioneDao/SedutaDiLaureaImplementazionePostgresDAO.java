package implementazioneDao;

import dao.SedutaDiLaureaDAO;
import database_connection.ConnessioneDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class SedutaDiLaureaImplementazionePostgresDAO implements SedutaDiLaureaDAO {

    private Connection connection;

    public SedutaDiLaureaImplementazionePostgresDAO() throws SQLException {
        connection = ConnessioneDatabase.getInstance().connection;
    }

    @Override
    public void inserisciSedutaDB(String codice, Date data,
                                  String ora, String luogo) throws Exception {
        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO seduta_di_laurea (codice, data, ora, luogo) " +
                        "VALUES (?, ?, ?, ?)"
        );
        ps.setString(1, codice);
        ps.setDate(2, new java.sql.Date(data.getTime()));
        ps.setString(3, ora);
        ps.setString(4, luogo);
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public ArrayList<String[]> leggiSeduteDB() throws Exception {
        ArrayList<String[]> sedute = new ArrayList<>();
        PreparedStatement ps = connection.prepareStatement(
                "SELECT codice, data, ora, luogo FROM seduta_di_laurea"
        );
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            sedute.add(new String[]{
                    rs.getString("codice"),
                    rs.getString("data"),
                    rs.getString("ora"),
                    rs.getString("luogo")
            });
        }
        rs.close();
        ps.close();
        return sedute;
    }
}