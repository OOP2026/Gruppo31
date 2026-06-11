package dao;

import java.sql.SQLException;
import java.util.Date;

/**
 * Interfaccia che definisce il "contratto" per le operazioni sul database
 * relative all'elaborato finale (Tesi) e al processo di prenotazione della laurea.
 * Espone i metodi necessari senza svelare i dettagli della query SQL sottostante.
 */
public interface TesiDAO {

    /**
     * Salva nel database i riferimenti fisici e logici dell'elaborato caricato dallo studente.
     *
     * @param titolo           il titolo scelto per la tesi
     * @param percorso         il path di salvataggio del file (es. C:/Documenti/Tesi.pdf)
     * @param usernameStudente l'identificativo univoco dello studente che effettua l'upload
     * @throws SQLException in caso di errori di scrittura sul database
     */
    void caricaTesiDB(String titolo, String percorso, String usernameStudente) throws SQLException;

    /**
     * Registra la prenotazione di uno studente per una determinata seduta di laurea.
     * Collega logicamente lo studente, la seduta e l'ultima tesi caricata a sistema.
     *
     * @param data             la data in cui viene effettuata l'operazione di prenotazione
     * @param usernameStudente lo username dello studente candidato
     * @param codiceSeduta     il codice identificativo della seduta scelta
     * @throws SQLException in caso di problemi di persistenza (es. seduta inesistente)
     */
    void prenotaSedutaDB(Date data, String usernameStudente, String codiceSeduta) throws SQLException;

    /**
     * Aggiorna lo stato di valutazione dell'elaborato da parte del docente (es. ACCETTATO o RIFIUTATO).
     *
     * @param matricola la matricola dello studente di cui si sta valutando la tesi
     * @param stato     il nuovo stato da assegnare all'elaborato
     * @throws SQLException se lo studente o la tesi non vengono trovati nel database
     */
    void valutaTesiDB(String matricola, String stato) throws SQLException;
}