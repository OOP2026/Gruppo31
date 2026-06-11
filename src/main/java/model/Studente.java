package model;

import java.util.Date;

/**
 * Rappresenta lo Studente iscritto all'università.
 * Eredita i dati di accesso base dalla classe Utente e aggiunge le funzionalità
 * necessarie per il suo percorso accademico: richiedere tirocini, caricare la tesi
 * e prenotarsi per la seduta di laurea finale.
 */
public class Studente extends Utente {

    /**
     * Il numero di matricola univoco assegnato allo studente.
     * È la chiave di ricerca principale utilizzata nel database per rintracciare
     * lo studente, le sue richieste e la sua tesi.
     */
    private String matricola;

    /**
     * Costruttore della classe Studente.
     *
     * @param username  l'username per l'accesso al sistema
     * @param password  la password dell'account
     * @param email     l'indirizzo email accademico
     * @param nome      il nome di battesimo
     * @param cognome   il cognome
     * @param matricola il numero di matricola identificativo (es. N46001)
     */
    public Studente(String username, String password, String email, String nome, String cognome, String matricola) {
        // Deleghiamo alla classe padre (Utente) la gestione dei dati anagrafici e di login
        super(username, password, email, nome, cognome);
        this.matricola = matricola;
    }

    /**
     * Genera una nuova richiesta per partecipare a un progetto di tirocinio.
     * Crea l'oggetto "RichiestaTirocinio" a livello di memoria RAM, collegando
     * lo studente al professore e al tirocinio scelto.
     *
     * @param relatore      il Docente che farà da supervisore
     * @param tirocinio     l'argomento del tirocinio scelto
     * @param dataRichiesta la data in cui viene generata la domanda
     * @return la richiesta formale appena creata (con stato di default IN_ATTESA)
     */
    @SuppressWarnings("java:S106")
    public RichiestaTirocinio richiediTirocinio(Docente relatore, Tirocinio tirocinio, Date dataRichiesta) {
        // Log di console per confermare la corretta generazione del modello in memoria
        System.out.println("Creazione richiesta di tirocinio con ID: " + tirocinio.getId() + " inviata al docente con SSN: " + relatore.getSsn());

        return new RichiestaTirocinio(dataRichiesta, this, relatore, tirocinio);
    }

    /**
     * Carica l'elaborato finale dello studente.
     * Instanzia un nuovo oggetto Tesi associato allo studente che esegue l'azione (this).
     *
     * @param titolo       il titolo dell'elaborato
     * @param percorsoFile il percorso in cui è salvato il file (es. C:/Desktop/tesi.pdf)
     * @return l'oggetto Tesi creato in memoria
     */
    @SuppressWarnings("java:S106")
    public Tesi caricaTesi(String titolo, String percorsoFile) {
        System.out.println("Tesi caricata con successo: " + titolo);

        return new Tesi(titolo, percorsoFile, this);
    }

    /**
     * Effettua la prenotazione formale alla seduta di laurea.
     * Aggrega lo studente, la sua tesi e la seduta scelta per creare la prenotazione finale.
     *
     * @param studente         il candidato che si prenota
     * @param tesi             l'elaborato finale da discutere
     * @param seduta           la seduta di laurea selezionata
     * @param dataPrenotazione la data in cui avviene la registrazione a sistema
     * @return l'oggetto PrenotazioneLaurea completo
     */
    @SuppressWarnings("java:S106")
    public PrenotazioneLaurea prenotaSedutaLaurea(Studente studente, Tesi tesi, SedutaDiLaurea seduta, Date dataPrenotazione) {
        System.out.println("Prenotazione alla seduta con codice " + seduta.getCodice() + " effettuata in data: " + dataPrenotazione);

        return new PrenotazioneLaurea(studente, tesi, seduta, dataPrenotazione);
    }

    // --- GETTER & SETTER ---

    /**
     * Restituisce la matricola dello studente.
     * @return una stringa contenente il numero di matricola
     */
    public String getMatricola() {
        return matricola;
    }
}