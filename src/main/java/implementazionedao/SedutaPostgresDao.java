package implementazionedao;

import dao.SedutaDAO;
import database_connection.ConnessioneDatabase;
import java.sql.*;
import java.util.Date;

/**
 * Implementazione concreta dell'interfaccia SedutaDAO per PostgreSQL.
 * Questa classe si occupa di tradurre le azioni sulle sedute di laurea
 * (es. creazione, assegnazione commissione) in vere e proprie query SQL
 * da inviare al database.
 */
public class SedutaPostgresDao implements SedutaDAO {

    /**
     * Inserisce una nuova seduta di laurea all'interno del database.
     * Corrisponde all'azione 'inserisciSeduta' del Coordinatore nel Model.
     *
     * @param data   la data della seduta (java.util.Date, convertita in java.sql.Date per il DB)
     * @param ora    l'orario di inizio
     * @param luogo  l'aula o il luogo fisico/virtuale
     * @param codice il codice identificativo univoco della seduta (chiave primaria)
     * @throws SQLException se c'è un errore di connessione o un vincolo violato nel DB
     */
    @Override
    public void inserisciSedutaDB(Date data, String ora, String luogo, String codice) throws SQLException {
        String query = "INSERT INTO seduta_laurea (codice, data_seduta, ora, luogo) VALUES (?, ?, ?, ?)";

        // Utilizziamo il try-with-resources per garantire la chiusura sicura di Connection e PreparedStatement
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, codice);
            // Conversione necessaria per adattare la data di Java al formato richiesto da SQL
            pstmt.setDate(2, new java.sql.Date(data.getTime()));
            pstmt.setString(3, ora);
            pstmt.setString(4, luogo);

            pstmt.executeUpdate();
        }
    }

    /**
     * Associa un professore a una specifica seduta di laurea inserendolo nella commissione.
     * Corrisponde all'azione 'aggiungiDocenteACommissione' del Coordinatore nel Model.
     * Popola la tabella associativa (o di giunzione) 'commissione' nel database.
     *
     * @param ssnD il codice fiscale (SSN) del docente
     * @param codS il codice identificativo della seduta di laurea
     * @throws SQLException se il docente o la seduta non esistono (violazione di chiave esterna)
     */
    @Override
    public void aggiungiDocenteACommissioneDB(String ssnD, String codS) throws SQLException {
        String query = "INSERT INTO commissione (seduta_codice, docente_ssn) VALUES (?, ?)";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, codS);
            pstmt.setString(2, ssnD);

            pstmt.executeUpdate();
        }
    }
}