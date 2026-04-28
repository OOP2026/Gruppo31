package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Seduta_di_laurea {

    public Date data;
    public String ora;
    public String luogo;
    public String codice;

    List<Docente> commissione;
    List<Prenotazione_Laurea> prenotazioni;

    public Seduta_di_laurea(Date data, String ora, String luogo, String codice) {
        this.data=data;
        this.ora=ora;
        this.luogo=luogo;
        this.codice=codice;

        this.commissione=new ArrayList<>();
        this.prenotazioni=new ArrayList<>();
    }

    public void aggiungiMembroCommissione(Docente docente) {
        this.commissione.add(docente);
    }
}
