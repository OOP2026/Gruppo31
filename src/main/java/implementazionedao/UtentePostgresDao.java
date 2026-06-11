package implementazionedao;

import dao.UtenteDAO;
import database_connection.ConnessioneDatabase;
import java.sql.*;
import java.util.ArrayList;

/**
 * Implementazione concreta dell'interfaccia UtenteDAO per PostgreSQL.
 * Questa classe gestisce l'autenticazione degli utenti (Studenti, Docenti, Coordinatori)
 * e il recupero delle loro informazioni anagrafiche di base dal database al momento del login.
 */
public class UtentePostgresDao implements UtenteDAO {

    /**
     * Verifica le credenziali di accesso e recupera i dati dell'utente.
     * Se username e password combaciano, estrae i dati dal database e li inserisce
     * nell'ArrayList passato come parametro, in modo che il Controller possa poi
     * istanziare l'oggetto corretto (Studente o Docente) in RAM.
     *
     * @param username l'identificativo inserito dall'utente nel form di login
     * @param password la chiave di accesso inserita
     * @param userData una lista vuota fornita dal Controller che verrà popolata con i dati estratti
     * @return true se il login ha successo (credenziali corrette), false se fallisce
     * @throws SQLException in caso di problemi di comunicazione con il database
     */
    @Override
    public boolean loginDB(String username, String password, ArrayList<String> userData) throws SQLException {
        // Query di selezione per verificare l'esistenza dell'utente con le credenziali fornite
        String query = "SELECT * FROM utente WHERE username = ? AND password = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            // Eseguiamo la query e gestiamo il ResultSet con un altro try-with-resources
            try (ResultSet rs = pstmt.executeQuery()) {

                // Se rs.next() è vero, significa che abbiamo trovato una corrispondenza
                if (rs.next()) {
                    // Popoliamo l'ArrayList rispettando un ordine preciso che il Controller si aspetta.
                    // A seconda del ruolo, 'matricola' o 'ssn' potrebbero essere null nel DB,
                    // ma verranno comunque estratti e gestiti correttamente dal Controller.
                    userData.add(rs.getString("ruolo"));
                    userData.add(rs.getString("email"));
                    userData.add(rs.getString("nome"));
                    userData.add(rs.getString("cognome"));
                    userData.add(rs.getString("matricola"));
                    userData.add(rs.getString("ssn"));

                    return true; // Login effettuato con successo
                }
            }
        }

        // Se arriviamo qui, rs.next() era falso: credenziali errate o utente inesistente
        return false;
    }
}