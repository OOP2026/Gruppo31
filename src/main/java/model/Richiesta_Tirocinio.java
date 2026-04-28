package model;

import java.util.Date;

public class Richiesta_Tirocinio {
    private StatoRichiesta stato;
    private Date data;

    private Studente studente;
    private Docente docente;
    private Tirocinio tirocinio;

    public Richiesta_Tirocinio(Date data, Studente studente, Docente docente, Tirocinio tirocinio) {
        this.stato = StatoRichiesta.In_Attesa; // Stato di default
        this.data = data;
        this.studente = studente;
        this.docente = docente;
        this.tirocinio = tirocinio;
    }
}