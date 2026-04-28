package model;

import java.util.Date;

public class Studente extends Utente{
    private String matricola;

    public Studente(String username, String password, String email, String nome, String cognome, String matricola){
        super(username,password,email,nome,cognome);
        this.matricola=matricola;
    }
    // richiediTirocinio serve a richiedere la partecipazione ad un tirocinio
    public Richiesta_Tirocinio richiediTirocinio(Docente relatore, Tirocinio tirocinio, Date dataRichiesta){
        System.out.println("Creazione richiesta di tirocinio per: " + tirocinio.argomento);
        return new Richiesta_Tirocinio(dataRichiesta, this, relatore, tirocinio);
    }
    // caricaTesti serve a caricare una tesi che va valutata
    public Tesi caricaTesi(String titolo, String percorsoFile){
        System.out.println("Tesi caricata con successo:"+titolo);
        return new Tesi(titolo,percorsoFile,this);
    }

    // prenotaSedutaLaurea serve a prenotare una seduta di laurea
    public Prenotazione_Laurea prenotaSedutaLaurea(Studente studente, Tesi tesi, Seduta_di_laurea seduta) {
        System.out.println("Prenotazione alla seduta del " + seduta.data + " effettuata.");
        return new Prenotazione_Laurea(studente, tesi, seduta);
    }
}
