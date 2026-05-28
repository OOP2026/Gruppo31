package model;

import java.util.Date;

/**
 * The type Richiesta tirocinio.
 */
public class RichiestaTirocinio {
    protected StatoRichiesta stato;
    protected Date data;
    private Studente studente;
    private Docente docente;
    private Tirocinio tirocinio;

    /**
     * Instantiates a new Richiesta tirocinio.
     *
     * @param data      the data
     * @param studente  the studente
     * @param docente   the docente
     * @param tirocinio the tirocinio
     */
    public RichiestaTirocinio(Date data, Studente studente, Docente docente, Tirocinio tirocinio) {
        this.stato = StatoRichiesta.IN_ATTESA;
        this.data = data;
        this.studente = studente;
        this.docente = docente;
        this.tirocinio = tirocinio;
    }

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