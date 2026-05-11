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
    public Richiesta_Tirocinio richiediTirocinio(Docente relatore, Tirocinio tirocinio, Date dataRichiesta){
        System.out.println("Creazione richiesta di tirocinio per: " + tirocinio.argomento);
        return new Richiesta_Tirocinio(dataRichiesta, this, relatore, tirocinio);
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
    public Prenotazione_Laurea prenotaSedutaLaurea(Studente studente, Tesi tesi, Seduta_di_laurea seduta) {
        System.out.println("Prenotazione alla seduta del " + seduta.data + " effettuata.");
        return new Prenotazione_Laurea(studente, tesi, seduta);
    }
}
