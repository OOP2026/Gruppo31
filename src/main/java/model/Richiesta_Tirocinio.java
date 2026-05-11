package model;

import java.util.Date;

/**
 * The type Richiesta tirocinio.
 */
public class Richiesta_Tirocinio {
    /**
     * The Stato.
     */
    protected StatoRichiesta stato;
    /**
     * The Data.
     */
    protected Date data;

    /**
     * The Studente.
     */
    public Studente studente;
    /**
     * The Docente.
     */
    public Docente docente;
    /**
     * The Tirocinio.
     */
    public Tirocinio tirocinio;

    /**
     * Instantiates a new Richiesta tirocinio.
     *
     * @param data      the data
     * @param studente  the studente
     * @param docente   the docente
     * @param tirocinio the tirocinio
     */
    public Richiesta_Tirocinio(Date data, Studente studente, Docente docente, Tirocinio tirocinio) {
        this.stato = StatoRichiesta.IN_ATTESA; // Stato di default
        this.data = data;
        this.studente = studente;
        this.docente = docente;
        this.tirocinio = tirocinio;
    }
}