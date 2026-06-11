package model;

import java.util.Date;

/**
 * Rappresenta la richiesta formale inviata da uno studente per partecipare a un tirocinio.
 * Questa classe fa da "ponte" tra lo studente candidato, il docente supervisore
 * e l'argomento del tirocinio stesso, tenendo traccia dello stato di avanzamento.
 */
public class RichiestaTirocinio {

    /**
     * Lo stato attuale della richiesta (es. IN_ATTESA, ACCETTATO, RIFIUTATO).
     */
    protected StatoRichiesta stato;

    /**
     * La data esatta in cui lo studente ha sottomesso la richiesta nel sistema.
     */
    protected Date data;

    /**
     * Lo studente che si candida per svolgere il tirocinio.
     */
    private Studente studente;

    /**
     * Il docente referente/supervisore responsabile di quel tirocinio.
     */
    private Docente docente;

    /**
     * L'offerta di tirocinio specifica a cui lo studente è interessato.
     */
    private Tirocinio tirocinio;

    /**
     * Costruttore della classe RichiestaTirocinio.
     * Come per le prenotazioni di laurea, ogni nuova richiesta inviata nel sistema
     * parte automaticamente con lo stato "IN_ATTESA", finché il docente non la esamina e la valuta.
     *
     * @param data      la data di invio della richiesta
     * @param studente  l'oggetto Studente che presenta la domanda
     * @param docente   il professore a cui è indirizzata la richiesta
     * @param tirocinio il tirocinio specifico scelto dallo studente
     */
    public RichiestaTirocinio(Date data, Studente studente, Docente docente, Tirocinio tirocinio) {
        // Regola di base: la richiesta appena creata è sempre in attesa
        this.stato = StatoRichiesta.IN_ATTESA;

        this.data = data;
        this.studente = studente;
        this.docente = docente;
        this.tirocinio = tirocinio;
    }

    // --- GETTER & SETTER ---
    // Metodi di accesso per leggere e aggiornare i dettagli della richiesta.
    // L'aggiornamento dello stato avviene tipicamente quando il Docente accetta o rifiuta la candidatura.

    public StatoRichiesta getStato() {
        return stato;
    }

    public void setStato(StatoRichiesta stato) {
        this.stato = stato;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Studente getStudente() {
        return studente;
    }

    public void setStudente(Studente studente) {
        this.studente = studente;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public Tirocinio getTirocinio() {
        return tirocinio;
    }

    public void setTirocinio(Tirocinio tirocinio) {
        this.tirocinio = tirocinio;
    }
}