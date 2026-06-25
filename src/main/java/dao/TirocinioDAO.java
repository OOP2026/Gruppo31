package dao;

import java.sql.SQLException;
import java.util.Date;

/**
 * Interfaccia che definisce il "contratto" per le operazioni sul database
 * relative alla gestione dei tirocini.
 * Isola la logica di business (Controller) dalle vere e proprie query SQL,
 * definendo i metodi per la pubblicazione, la richiesta e la valutazione dei tirocini.
 */
public interface TirocinioDAO {

    /**
     * Inserisce una nuova proposta di tirocinio all'interno del database.
     * Operazione tipicamente eseguita da un Docente.
     *
     * @param id        l'identificativo numerico univoco del nuovo tirocinio
     * @param argomento la descrizione o il tema dell'attività proposta
     * @throws SQLException in caso di errori (es. se esiste già un tirocinio con lo stesso ID)
     */
    void aggiungiTirocinioDB(int id, String argomento, String ssnDocente) throws SQLException;
    /**
     * Registra la candidatura di uno studente per un determinato tirocinio.
     * Salva nel database il collegamento tra lo studente, il professore responsabile
     * e l'offerta di tirocinio selezionata.
     *
     * @param data             la data in cui viene sottomessa la richiesta
     * @param usernameStudente lo username dello studente che si candida
     * @param ssnDocente       il codice fiscale del docente relatore/supervisore
     * @param idTirocinio      l'identificativo del tirocinio scelto
     * @throws SQLException in caso di errori di persistenza o violazioni di chiavi esterne
     */
    void richiediTirocinioDB(Date data, String usernameStudente, String ssnDocente, int idTirocinio) throws SQLException;

    /**
     * Aggiorna lo stato di una candidatura a un tirocinio.
     * Operazione eseguita dal Docente per approvare o rifiutare la richiesta di uno studente.
     *
     * @param matricola   la matricola dello studente candidato
     * @param idTirocinio l'identificativo del tirocinio per il quale si sta valutando la richiesta
     * @param stato       il nuovo stato da assegnare (es. ACCETTATO, RIFIUTATO)
     * @throws SQLException se la richiesta non viene trovata nel database
     */
    void valutaRichiestaDB(String matricola, int idTirocinio, String stato) throws SQLException;

    void aggiungiTirocinioEsternoDB(int id, String argomento, String azienda, String referenteAziendale, String ssnDocente) throws SQLException;
    java.util.List<String[]> getTirociniDisponibiliDB() throws SQLException;
    java.util.List<String[]> getRichiesteStudenteDB(String username) throws SQLException;
    java.util.List<String[]> getRichiestePerDocenteDB(String ssnDocente) throws SQLException;
}