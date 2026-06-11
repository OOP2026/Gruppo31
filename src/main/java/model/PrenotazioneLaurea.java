package model;

import java.util.Date;

/**
 * Rappresenta la prenotazione effettiva di uno studente a una seduta di laurea.
 * Questa classe è un'entità di collegamento (associazione) fondamentale, in quanto
 * unisce lo studente, il suo elaborato finale (Tesi) e la commissione esaminatrice (Seduta).
 */
public class PrenotazioneLaurea {

    /**
     * La seduta di laurea (data, ora, luogo e commissione) scelta dallo studente.
     */
    private SedutaDiLaurea seduta;

    /**
     * Lo stato di avanzamento della prenotazione (es. IN_ATTESA, APPROVATA, RIFIUTATA).
     */
    private StatoLaurea stato;

    /**
     * Lo studente candidato alla laurea.
     */
    private Studente studente;

    /**
     * L'elaborato finale presentato dallo studente per la seduta.
     */
    private Tesi tesi;

    /**
     * La data esatta in cui lo studente ha effettuato la richiesta nel sistema.
     */
    private Date dataPrenotazione;

    /**
     * Costruttore della Prenotazione di Laurea.
     * Quando uno studente si prenota, il sistema associa i suoi dati alla seduta
     * e imposta automaticamente lo stato di default su "IN_ATTESA", in attesa
     * di eventuali controlli da parte della segreteria o del coordinatore.
     *
     * @param studente         il candidato che effettua la prenotazione
     * @param tesi             l'elaborato associato al candidato
     * @param seduta           la seduta d'esame selezionata
     * @param dataPrenotazione il giorno in cui viene registrata la prenotazione
     */
    public PrenotazioneLaurea(Studente studente, Tesi tesi, SedutaDiLaurea seduta, Date dataPrenotazione) {
        // Ogni nuova prenotazione parte sempre in attesa di elaborazione
        this.stato = StatoLaurea.IN_ATTESA;

        this.studente = studente;
        this.tesi = tesi;
        this.seduta = seduta;
        this.dataPrenotazione = dataPrenotazione;
    }

    // --- GETTER & SETTER ---
    // Metodi di accesso standard per leggere e aggiornare le informazioni della prenotazione
    // (ad esempio, quando il coordinatore decide di cambiare lo stato da IN_ATTESA ad APPROVATA)

    public Date getDataPrenotazione() {
        return dataPrenotazione;
    }

    public void setDataPrenotazione(Date dataPrenotazione) {
        this.dataPrenotazione = dataPrenotazione;
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