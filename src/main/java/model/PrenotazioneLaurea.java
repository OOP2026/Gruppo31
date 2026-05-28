package model;

/**
 * The type Prenotazione laurea.
 */
public class PrenotazioneLaurea {
    private SedutaDiLaurea seduta;
    private StatoLaurea stato;

    // Associazioni
    private Studente studente;
    private Tesi tesi;

    /**
     * Instantiates a new Prenotazione laurea.
     *
     * @param studente the studente
     * @param tesi     the tesi
     * @param seduta   the seduta
     */
    public PrenotazioneLaurea(Studente studente, Tesi tesi, SedutaDiLaurea seduta) {
        this.stato = StatoLaurea.IN_ATTESA;
        this.studente = studente;
        this.tesi = tesi;
        this.seduta = seduta;
    }

    public SedutaDiLaurea getSeduta() {
        return seduta;
    }

    public void setSeduta(SedutaDiLaurea seduta) {
        this.seduta = seduta;
    }

    public StatoLaurea getStato() {
        return stato;
    }

    public void setStato(StatoLaurea stato) {
        this.stato = stato;
    }

    public Studente getStudente() {
        return studente;
    }

    public void setStudente(Studente studente) {
        this.studente = studente;
    }

    public Tesi getTesi() {
        return tesi;
    }

    public void setTesi(Tesi tesi) {
        this.tesi = tesi;
    }
}