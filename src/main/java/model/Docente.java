package model;

public class Docente extends Utente{
    public String ssn;

    public Docente(String username, String password, String email, String nome, String cognome, String ssn){
        super(username, password, email, nome, cognome);
        this.ssn=ssn;
    }
    // aggiungiTirocinio aggiunge un nuovo tirocinio tra i disponibili
    public Tirocinio aggiungiTirocinio(int id, String argomento) {
        System.out.println("Nuovo tirocinio proposto: " + argomento);
        return new Tirocinio(id, argomento);
    }

    // valutaRichiesta serve ad approvare o rifiutare una richiesta di tirocinio di uno studente
    public void valutaRichiesta(Richiesta_Tirocinio richiesta, boolean approvata) {
        if (approvata) {
            richiesta.stato = StatoRichiesta.ACCETTATO;
            System.out.println("Richiesta di tirocinio approvata.");
        } else {
            richiesta.stato = StatoRichiesta.RIFIUTATO;
            System.out.println("Richiesta di tirocinio rifiutata.");
        }
    }

    // valutaTesi serve ad approvare o rifiutare una tesi di uno studente
    public void valutaTesi(Tesi tesi, boolean approvata) {
        if (approvata) {
            tesi.stato = StatoTesi.ACCETTATO;
            System.out.println("Tesi approvata. Lo studente può procedere.");
        } else {
            tesi.stato = StatoTesi.RIFIUTATO;
            System.out.println("Tesi rifiutata. Lo studente deve caricare una nuova versione.");
        }
    }
}
