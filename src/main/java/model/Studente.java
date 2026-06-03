package model;

import java.util.Date;

/**
 * The type Studente.
 */
public class Studente extends Utente{
    private String matricola;

    /**
     * Instantiates a new Studente.
     *
     * @param username  the username
     * @param password  the password
     * @param email     the email
     * @param nome      the nome
     * @param cognome   the cognome
     * @param matricola the matricola
     */
    public Studente(String username, String password, String email, String nome, String cognome, String matricola){
        super(username,password,email,nome,cognome);
        this.matricola=matricola;
    }

    /**
     * Richiedi tirocinio richiesta tirocinio.
     *
     * @param relatore      the relatore
     * @param tirocinio     the tirocinio
     * @param dataRichiesta the data richiesta
     * @return the richiesta tirocinio
     */
// richiediTirocinio serve a richiedere la partecipazione ad un tirocinio
    public RichiestaTirocinio richiediTirocinio(Docente relatore, Tirocinio tirocinio, Date dataRichiesta){
        // MODIFICA: Ora stampa l'ID del tirocinio (es. 123) e non la stringa fissa
        System.out.println("Creazione richiesta di tirocinio con ID: " + tirocinio.id);
        return new RichiestaTirocinio(dataRichiesta, this, relatore, tirocinio);
    }

    /**
     * Carica tesi tesi.
     *
     * @param titolo       the titolo
     * @param percorsoFile the percorso file
     * @return the tesi
     */
// caricaTesti serve a caricare una tesi che va valutata
    public Tesi caricaTesi(String titolo, String percorsoFile){
        System.out.println("Tesi caricata con successo:"+titolo);
        return new Tesi(titolo,percorsoFile,this);
    }

    /**
     * Prenota seduta laurea prenotazione laurea.
     *
     * @param studente the studente
     * @param tesi     the tesi
     * @param seduta   the seduta
     * @return the prenotazione laurea
     */
// prenotaSedutaLaurea serve a prenotare una seduta di laurea
    public PrenotazioneLaurea prenotaSedutaLaurea(Studente studente, Tesi tesi, SedutaDiLaurea seduta) {
        // MODIFICA: Ora stampa il Codice della seduta (es. 123) e non la data di oggi
        System.out.println("Prenotazione alla seduta con codice " + seduta.getCodice() + " effettuata.");
        return new PrenotazioneLaurea(studente, tesi, seduta);
    }

    public String getMatricola() {
        return matricola;
    }

}
