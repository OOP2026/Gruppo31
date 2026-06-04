package model;

import java.util.Date;

public class Studente extends Utente {
    private String matricola;

    public Studente(String username, String password, String email, String nome, String cognome, String matricola){
        super(username,password,email,nome,cognome);
        this.matricola=matricola;
    }

    @SuppressWarnings("java:S106")
    public RichiestaTirocinio richiediTirocinio(Docente relatore, Tirocinio tirocinio, Date dataRichiesta){
        System.out.println("Creazione richiesta di tirocinio con ID: " + tirocinio.getId() + " inviata al docente con SSN: " + relatore.getSsn());
        return new RichiestaTirocinio(dataRichiesta, this, relatore, tirocinio);
    }

    @SuppressWarnings("java:S106")
    public Tesi caricaTesi(String titolo, String percorsoFile){
        System.out.println("Tesi caricata con successo: " + titolo);
        return new Tesi(titolo, percorsoFile, this);
    }

    @SuppressWarnings("java:S106")
    public PrenotazioneLaurea prenotaSedutaLaurea(Studente studente, Tesi tesi, SedutaDiLaurea seduta, Date dataPrenotazione) {
        System.out.println("Prenotazione alla seduta con codice " + seduta.getCodice() + " effettuata in data: " + dataPrenotazione);
        return new PrenotazioneLaurea(studente, tesi, seduta, dataPrenotazione);
    }

    public String getMatricola() { return matricola; }
}