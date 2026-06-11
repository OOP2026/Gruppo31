package model;

/**
 * Rappresenta un Professore all'interno del sistema.
 * Eredita i dati di accesso e anagrafici dalla classe base Utente, ma aggiunge
 * i permessi specifici per gestire e valutare il lavoro degli studenti (tesi e tirocini).
 */
public class Docente extends Utente {

    /**
     * Codice Fiscale o identificativo univoco del docente (Social Security Number).
     * È fondamentale per associare il docente corretto alle commissioni di laurea
     * e alle richieste di tirocinio nel database.
     */
    private String ssn;

    /**
     * Costruttore della classe Docente.
     * * @param username l'username per l'accesso al sistema
     * @param password la password dell'account
     * @param email    l'indirizzo email istituzionale
     * @param nome     il nome di battesimo
     * @param cognome  il cognome
     * @param ssn      il codice fiscale (Social Security Number)
     */
    public Docente(String username, String password, String email, String nome, String cognome, String ssn){
        // Passiamo i dati comuni alla classe padre (Utente)
        super(username, password, email, nome, cognome);
        this.ssn = ssn;
    }

    /**
     * Permette al docente di proporre un nuovo argomento di tirocinio.
     * Crea e restituisce l'oggetto in memoria RAM.
     *
     * @param id        l'identificativo numerico del tirocinio
     * @param argomento la descrizione o il titolo dell'attività
     * @return il nuovo oggetto Tirocinio pronto per essere salvato o usato
     */
    public Tirocinio aggiungiTirocinio(int id, String argomento) {
        // Istanzia il tirocinio solo a livello di modello (il DB viene gestito dal Controller)
        return new Tirocinio(id, argomento);
    }

    /**
     * Valuta la richiesta di uno studente che vuole partecipare a un tirocinio.
     * Modifica lo stato interno della richiesta in base alla decisione del professore.
     *
     * @param richiesta l'oggetto che rappresenta la richiesta inviata dallo studente
     * @param approvata true se il docente accetta, false se la rifiuta
     */
    public void valutaRichiesta(RichiestaTirocinio richiesta, boolean approvata) {
        if (approvata) {
            richiesta.setStato(StatoRichiesta.ACCETTATO);
        } else {
            richiesta.setStato(StatoRichiesta.RIFIUTATO);
        }
    }

    /**
     * Valuta l'elaborato finale (Tesi) caricato dallo studente.
     * Aggiorna lo stato della tesi permettendo o bloccando l'accesso alla seduta di laurea.
     *
     * @param tesi      l'oggetto Tesi associato allo studente
     * @param approvata true per approvare l'elaborato, false per respingerlo
     */
    public void valutaTesi(Tesi tesi, boolean approvata) {
        if (approvata) {
            tesi.setStato(StatoTesi.ACCETTATO);
        } else {
            tesi.setStato(StatoTesi.RIFIUTATO);
        }
    }

    // --- GETTER & SETTER ---

    /**
     * Restituisce il codice fiscale del docente.
     * @return una stringa contenente l'SSN
     */
    public String getSsn() {
        return ssn;
    }

    /**
     * Aggiorna il codice fiscale del docente.
     * @param ssn il nuovo identificativo da impostare
     */
    public void setSsn(String ssn) {
        this.ssn = ssn;
    }
}