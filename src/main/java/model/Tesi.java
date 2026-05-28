package model;

/**
 * The type Tesi.
 */
public class Tesi {
    private String titolo;
    private String percorsoFile;
    private StatoTesi stato;
    private Studente studente;

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
        this.studente = studente;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getPercorsoFile() {
        return percorsoFile;
    }

    public void setPercorsoFile(String percorsoFile) {
        this.percorsoFile = percorsoFile;
    }

    public StatoTesi getStato() {
        return stato;
    }

    public void setStato(StatoTesi stato) {
        this.stato = stato;
    }

    public Studente getStudente() {
        return studente;
    }

    public void setStudente(Studente studente) {
        this.studente = studente;
    }
}