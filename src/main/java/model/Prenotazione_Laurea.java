package model;

public class Prenotazione_Laurea {
    public Seduta_di_laurea seduta;
    public StatoLaurea stato;

    // Associazioni
    public Studente studente;
    public Tesi tesi;

    public Prenotazione_Laurea(Studente studente, Tesi tesi, Seduta_di_laurea seduta) {
        this.stato = StatoLaurea.IN_ATTESA;
        this.studente = studente;
        this.tesi = tesi;
        this.seduta=seduta;
    }

}
