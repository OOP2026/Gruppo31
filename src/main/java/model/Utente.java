package model;

/**
 * Superclasse che rappresenta un utente generico del nostro gestionale universitario.
 * Evita di farci riscrivere email e nome mille volte per Studente, Docente e Coordinatore.
 */
public class Utente {
    protected String username;
    protected String password;
    protected String email;
    protected String nome;
    protected String cognome;

    /**
     * Costruttore base.
     * Usato dalle classi figlie tramite il comando "super(...)".
     */
    public Utente(String username, String password, String email, String nome, String cognome){
        this.username = username;
        this.password = password;
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
    }

    /**
     * Ritorna lo username usato per fare l'accesso.
     * @return username in formato stringa
     */
    public String getUsername() {
        return username;
    }
}