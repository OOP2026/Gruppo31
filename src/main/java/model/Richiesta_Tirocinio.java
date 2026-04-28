package model;

import java.util.Date;

public class Richiesta_Tirocinio {
    protected StatoRichiesta stato;
    protected Date data;

    public Studente studente;
    public Docente docente;
    public Tirocinio tirocinio;

    public Richiesta_Tirocinio(Date data, Studente studente, Docente docente, Tirocinio tirocinio) {
        this.stato = StatoRichiesta.In_Attesa; // Stato di default
        this.data = data;
        this.studente = studente;
        this.docente = docente;
        this.tirocinio = tirocinio;
    }
}