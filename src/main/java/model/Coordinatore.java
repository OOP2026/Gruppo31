package model;

import java.util.Date;

/**
 * Rappresenta il Coordinatore del corso di laurea.
 * Essendo a tutti gli effetti un professore, eredita dalla classe Docente,
 * ma possiede poteri aggiuntivi per la gestione burocratica (es. organizzare le sedute).
 */
public class Coordinatore extends Docente {

    /**
     * Nome o codice identificativo del corso di laurea gestito dal coordinatore.
     */
    protected String nCorsoLaurea;

    /**
     * Costruttore della classe Coordinatore.
     * Sfrutta il costruttore della superclasse (Docente) tramite 'super'
     * per inizializzare tutte le credenziali e i dati anagrafici di base.
     *
     * @param username l'username per il login
     * @param password la password di accesso
     * @param email    l'indirizzo email accademico
     * @param nome     il nome del coordinatore
     * @param cognome  il cognome del coordinatore
     * @param ssn      il codice fiscale o numero di previdenza sociale (Social Security Number)
     */
    public Coordinatore(String username, String password, String email, String nome, String cognome, String ssn) {
        super(username, password, email, nome, cognome, ssn);
    }

    /**
     * Crea e pianifica una nuova seduta di laurea.
     * Questo metodo si occupa di istanziare l'oggetto in memoria RAM.
     * Il salvataggio permanente sul database viene invece gestito dal Controller.
     *
     * @param data   la data in cui si terrà la seduta
     * @param ora    l'orario di inizio
     * @param luogo  l'aula o l'edificio assegnato
     * @param codice il codice univoco della seduta (es. SED-01)
     * @return l'oggetto SedutaDiLaurea appena creato
     */
    @SuppressWarnings("java:S106")
    public SedutaDiLaurea inserisciSeduta(Date data, String ora, String luogo, String codice) {
        // Log di sistema per tracciare la creazione in memoria
        System.out.println("Nuova seduta creata il " + data + " in " + luogo);

        // Costruiamo e restituiamo la nuova seduta
        return new SedutaDiLaurea(data, ora, luogo, codice);
    }

    /**
     * Inserisce un professore (Docente) all'interno della commissione d'esame
     * per una specifica seduta di laurea.
     *
     * @param docente il professore da aggiungere alla commissione
     * @param seduta  la seduta di laurea di riferimento
     */
    @SuppressWarnings("java:S106")
    public void aggiungiDocenteACommissione(Docente docente, SedutaDiLaurea seduta) {
        // Deleghiamo all'oggetto 'seduta' l'inserimento del docente nella sua lista interna
        seduta.aggiungiMembroCommissione(docente);

        // Feedback visivo in console per confermare l'avvenuta associazione tra docente e seduta
        System.out.println("Docente con SSN: " + docente.getSsn() + " aggiunto alla commissione della seduta: " + seduta.getCodice());
    }
}