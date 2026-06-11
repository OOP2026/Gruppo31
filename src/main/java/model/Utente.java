package model;

/**
 * Rappresenta un utente generico del sistema.
 * Funge da classe base (superclasse) da cui derivano tutti gli altri attori
 * (Studente, Docente, Coordinatore). Centralizza la gestione dei dati anagrafici
 * e delle credenziali, evitando duplicazioni di codice nelle classi figlie.
 */
public class Utente {

    /**
     * Il nome utente utilizzato per accedere al sistema.
     * È visibile alle classi figlie grazie al modificatore 'protected'.
     */
    protected String username;

    /**
     * La chiave di sicurezza per l'autenticazione.
     */
    protected String password;

    /**
     * L'indirizzo di posta elettronica istituzionale dell'utente.
     */
    protected String email;

    /**
     * Il nome di battesimo dell'utente.
     */
    protected String nome;

    /**
     * Il cognome dell'utente.
     */
    protected String cognome;

    /**
     * Costruttore della classe base Utente.
     * Generalmente non viene usato per creare un utente generico, ma viene
     * richiamato (tramite il comando 'super()') dai costruttori delle classi figlie
     * per inizializzare i dati anagrafici condivisi.
     *
     * @param username l'identificativo per il login
     * @param password la password di accesso
     * @param email    il recapito email ufficiale
     * @param nome     il nome dell'utente
     * @param cognome  il cognome dell'utente
     */
    public Utente(String username, String password, String email, String nome, String cognome){
        this.username = username;
        this.password = password;
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
    }

    /**
     * Esegue una validazione locale (in RAM) delle credenziali di accesso.
     * Confronta l'username e la password forniti con quelli salvati nell'oggetto.
     * (Nota: nel flusso principale, il vero login di sicurezza viene gestito dal DAO interrogando il Database).
     *
     * @param username lo username digitato nel form di login
     * @param password la password digitata nel form di login
     * @return true se le credenziali corrispondono, false altrimenti
     */
    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    // --- GETTER ---

    /**
     * Restituisce l'identificativo di login dell'utente.
     * @return una stringa contenente lo username
     */
    public String getUsername() {
        return username;
    }
}