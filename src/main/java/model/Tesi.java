package model;

/**
 * The type Tesi.
 */
public class Tesi {
    /**
     * The Titolo.
     */
    public String titolo;
    /**
     * The Percorso file.
     */
    public String percorsoFile;
    /**
     * The Stato.
     */
    public StatoTesi stato;
    /**
     * The Studente.
     */
    public Studente studente;

    /**
     * Instantiates a new Tesi.
     *
     * @param titolo       the titolo
     * @param percorsoFile the percorso file
     * @param studente     the studente
     */
    public Tesi(String titolo, String percorsoFile, Studente studente) {
        this.titolo = titolo;
        this.percorsoFile = percorsoFile;
        this.studente=studente;
    }
}