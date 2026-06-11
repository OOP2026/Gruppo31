package dao;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interfaccia che definisce il "contratto" per le operazioni sul database
 * relative agli utenti del sistema.
 * Si concentra principalmente sul processo di autenticazione (Login) e
 * sull'estrazione dei dati anagrafici di base.
 */
public interface UtenteDAO {

    /**
     * Verifica le credenziali di accesso di un utente nel database.
     * In caso di successo, popola l'ArrayList passato come parametro con i
     * dati dell'utente (ruolo, email, nome, cognome, matricola/ssn), fungendo
     * di fatto da parametro di "output" per il Controller.
     *
     * @param username l'identificativo digitato dall'utente
     * @param password la chiave d'accesso digitata dall'utente
     * @param userData una lista vuota che verrà riempita con i dati estratti dal DB
     * @return true se username e password sono corretti, false altrimenti
     * @throws SQLException in caso di problemi di comunicazione con il database
     */
    boolean loginDB(String username, String password, ArrayList<String> userData) throws SQLException;
}