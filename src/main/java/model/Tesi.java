package model;

/**
 * Rappresenta l'elaborato finale (Tesi) redatto dallo studente.
 * Questa classe funge da contenitore per i dettagli del progetto di tesi,
 * mantenendo il tracciamento del file fisico, del titolo e dello stato
 * di approvazione da parte del docente relatore.
 */
public class Tesi {

    /**
     * Il titolo ufficiale dell'elaborato.
     */
    private String titolo;

    /**
     * Il percorso di salvataggio del file all'interno del sistema o del server
     * (es. "C:/Documenti/Tesi_Mario_Rossi.pdf").
     */
    private String percorsoFile;

    /**
     * Lo stato attuale di valutazione della tesi (es. IN_ATTESA, ACCETTATO, RIFIUTATO).
     */
    private StatoTesi stato;

    /**
     * L'autore dell'elaborato, ovvero lo studente che ha caricato la tesi a sistema.
     */
    private Studente studente;

    /**
     * Costruttore della classe Tesi.
     * Inizializza l'elaborato collegandolo immediatamente al suo autore.
     *
     * @param titolo       il titolo scelto per la tesi
     * @param percorsoFile il percorso in cui è memorizzato il documento
     * @param studente     l'oggetto Studente che rappresenta l'autore dell'elaborato
     */
    public Tesi(String titolo, String percorsoFile, Studente studente) {
        this.titolo = titolo;
        this.percorsoFile = percorsoFile;
        this.studente = studente;
    }

    // --- GETTER & SETTER ---
    // Metodi di accesso per consultare o modificare i dati dell'elaborato.
    // Lo stato verrà aggiornato dal sistema quando il docente valuterà la tesi.

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