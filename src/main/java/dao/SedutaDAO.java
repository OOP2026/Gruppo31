package dao;

import java.sql.SQLException;
import java.util.Date;

/**
 * Interfaccia che definisce il "contratto" per le operazioni sul database
 * relative alle sedute di laurea.
 * Utilizzare un'interfaccia permette di disaccoppiare la logica dell'applicazione
 * (il Controller) dal motore di database fisico (PostgreSQL), garantendo una
 * maggiore flessibilità dell'intera architettura.
 */
public interface SedutaDAO {

    /**
     * Registra una nuova seduta di laurea all'interno del database.
     *
     * @param data   la data ufficiale della seduta
     * @param ora    l'orario di inizio delle discussioni
     * @param luogo  il luogo o l'aula (fisica o virtuale) in cui si terrà l'evento
     * @param codice il codice identificativo univoco della seduta
     * @throws SQLException in caso di errori di scrittura o vincoli violati sul database
     */
    void inserisciSedutaDB(Date data, String ora, String luogo, String codice) throws SQLException;

    /**
     * Associa un docente a una specifica seduta, inserendolo nella commissione esaminatrice.
     * Questa operazione va a popolare la tabella di collegamento (join table) nel database relazionale.
     *
     * @param ssnDocente   il codice fiscale (SSN) del professore
     * @param codiceSeduta il codice della seduta a cui assegnarlo
     * @throws SQLException in caso di errori (es. violazione della chiave esterna se il docente non esiste)
     */
    void aggiungiDocenteACommissioneDB(String ssnDocente, String codiceSeduta) throws SQLException;

    boolean verificaDocenteValidoPerCommissione(String ssnDocente, String codiceSeduta) throws SQLException;

    java.util.List<String[]> getStudentiPerSedutaDB(String codiceSeduta) throws SQLException;
}