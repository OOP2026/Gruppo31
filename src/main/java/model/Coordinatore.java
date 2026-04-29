package model;

import java.util.Date;

public class Coordinatore extends Docente{
    protected String nCorsoLaurea;

    public Coordinatore(String username, String password, String email, String nome, String cognome, String ssn, String corso){
        super(username, password, email, nome, cognome, ssn);
        this.nCorsoLaurea =corso;
    }

    // Il metodo inserisciSeduta serve a creare una nuova seduta di laurea
    public Seduta_di_laurea inserisciSeduta(Date data, String ora, String luogo, String codice) {
        System.out.println("Nuova seduta creata il " + data + " in " + luogo);
        return new Seduta_di_laurea(data, ora, luogo, codice);
    }

    // AggiungiDocenteACommissione serve ad aggiungere uno specifico docente ad una seduta specifica
    public void aggiungiDocenteACommissione(Docente docente, Seduta_di_laurea seduta) {
        seduta.aggiungiMembroCommissione(docente);
        System.out.println("Docente " + docente.cognome + " aggiunto alla commissione in data" + seduta.data);
    }
}
