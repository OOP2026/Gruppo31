package model;

import java.util.Date;

public class PrenotazioneLaurea {
    private SedutaDiLaurea seduta;
    private StatoLaurea stato;
    private Studente studente;
    private Tesi tesi;
    private Date dataPrenotazione;

    public PrenotazioneLaurea(Studente studente, Tesi tesi, SedutaDiLaurea seduta, Date dataPrenotazione) {
        this.stato = StatoLaurea.IN_ATTESA;
        this.studente = studente;
        this.tesi = tesi;
        this.seduta = seduta;
        this.dataPrenotazione = dataPrenotazione;
    }

    public Date getDataPrenotazione() { return dataPrenotazione; }
    public void setDataPrenotazione(Date dataPrenotazione) { this.dataPrenotazione = dataPrenotazione; }
    public SedutaDiLaurea getSeduta() { return seduta; }
    public void setSeduta(SedutaDiLaurea seduta) { this.seduta = seduta; }
    public StatoLaurea getStato() { return stato; }
    public void setStato(StatoLaurea stato) { this.stato = stato; }
    public Studente getStudente() { return studente; }
    public void setStudente(Studente studente) { this.studente = studente; }
    public Tesi getTesi() { return tesi; }
    public void setTesi(Tesi tesi) { this.tesi = tesi; }
}