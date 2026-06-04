package model;

public class Docente extends Utente {
    private String ssn;

    public Docente(String username, String password, String email, String nome, String cognome, String ssn){
        super(username, password, email, nome, cognome);
        this.ssn=ssn;
    }

    public Tirocinio aggiungiTirocinio(int id, String argomento) {
        return new Tirocinio(id, argomento);
    }

    public void valutaRichiesta(RichiestaTirocinio richiesta, boolean approvata) {
        if (approvata) {
            richiesta.setStato(StatoRichiesta.ACCETTATO);
        } else {
            richiesta.setStato(StatoRichiesta.RIFIUTATO);
        }
    }

    public void valutaTesi(Tesi tesi, boolean approvata) {
        if (approvata) {
            tesi.setStato(StatoTesi.ACCETTATO);
        } else {
            tesi.setStato(StatoTesi.RIFIUTATO);
        }
    }

    public String getSsn() { return ssn; }
    public void setSsn(String ssn) { this.ssn = ssn; }
}