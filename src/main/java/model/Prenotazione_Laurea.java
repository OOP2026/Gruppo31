package model;

/**
 * The type Prenotazione laurea.
 */
public class Prenotazione_Laurea {
    /**
     * The Seduta.
     */
    public Seduta_di_laurea seduta;
    /**
     * The Stato.
     */
    public StatoLaurea stato;

    /**
     * The Studente.
     */
// Associazioni
    public Studente studente;
    /**
     * The Tesi.
     */
    public Tesi tesi;

    /**
     * Instantiates a new Prenotazione laurea.
     *
     * @param studente the studente
     * @param tesi     the tesi
     * @param seduta   the seduta
     */
    public Prenotazione_Laurea(Studente studente, Tesi tesi, Seduta_di_laurea seduta) {
        this.stato = StatoLaurea.IN_ATTESA;
        this.studente = studente;
        this.tesi = tesi;
        this.seduta=seduta;
    }

}
