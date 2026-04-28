package model;

public class Prenotazione_Laurea {
    private StatoLaurea stato;

    // Associazioni
    private Studente studente;
    private Tesi tesi;

    public Prenotazione_Laurea(Studente studente, Tesi tesi) {
        this.stato = StatoLaurea.In_Attesa;
        this.studente = studente;
        this.tesi = tesi;
    }
}
