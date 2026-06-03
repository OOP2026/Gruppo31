package controller;

import model.*;
import java.util.Date;

public class Controller {

    private Utente utenteLoggato;

    // Costante per simulare la password
    private static final String DUMMY_PASS = "123";

    public Controller() {
        // Costruttore vuoto, nessuna simulazione DB per ora
    }

    // ==========================================================
    // LOGIN
    // ==========================================================

    @SuppressWarnings("java:S2068")
    public boolean effettuaLogin(String username, String password) {
        if(username.equals("studente") && password.equals(DUMMY_PASS)) {
            utenteLoggato = new Studente(username, password, "stud@unina.it", "Mario", "Rossi", "N46001");
            return true;
        } else if (username.equals("docente") && password.equals(DUMMY_PASS)) {
            utenteLoggato = new Docente(username, password, "doc@unina.it", "Luigi", "Verdi", "VRDLGU");
            return true;
        } else if (username.equals("coord") && password.equals(DUMMY_PASS)) {
            utenteLoggato = new Coordinatore(username, password, "coord@unina.it", "Anna", "Bianchi", "BNCNNA", "Informatica");
            return true;
        }
        return false;
    }

    public Utente getUtenteLoggato() {
        return utenteLoggato;
    }

    // ==========================================================
    // AZIONI DELLO STUDENTE
    // ==========================================================

    // MODIFICA: Sostituiti gli oggetti 'Docente' e 'Tirocinio' con i loro ID (String e int).
    // In questo modo la GUI passa solo i dati testuali scelti dall'utente.
    public void studenteRichiediTirocinio(String ssnRelatore, int idTirocinio) {
        if (utenteLoggato instanceof Studente) {
            Studente s = (Studente) utenteLoggato;

            // MODIFICA: Simuliamo la creazione degli oggetti basandoci sugli ID ricevuti.
            // Quando ci sarà il DB, qui farai una INSERT tramite il DAO.
            Docente relatore = new Docente("mock", "123", "mock@mail", "Mock", "Mock", ssnRelatore);
            Tirocinio tirocinio = new Tirocinio(idTirocinio, "Argomento");

            s.richiediTirocinio(relatore, tirocinio, new Date());
        }
    }

    // MODIFICA: Qui i parametri andavano già bene perché sono due Stringhe.
    // Nessun cambio di parametri effettuato.
    public void studenteCaricaTesi(String titolo, String percorsoFile) {
        if (utenteLoggato instanceof Studente) {
            Studente s = (Studente) utenteLoggato;
            s.caricaTesi(titolo, percorsoFile);
        }
    }

    // MODIFICA: Sostituiti gli oggetti 'Tesi' e 'SedutaDiLaurea' con i rispettivi titoli/codici (String).
    public void studentePrenotaSeduta(String titoloTesi, String codiceSeduta) {
        if (utenteLoggato instanceof Studente) {
            Studente s = (Studente) utenteLoggato;

            // MODIFICA: Oggetti fittizi creati nel Controller per rispettare il Model.
            // In futuro, il DAO cercherà la tesi e la seduta vera dal DB usando queste stringhe.
            Tesi tesi = new Tesi(titoloTesi, "percorso", s);
            SedutaDiLaurea seduta = new SedutaDiLaurea(new Date(), "09:00", "Luogo", codiceSeduta);

            s.prenotaSedutaLaurea(s, tesi, seduta);
        }
    }

    // ==========================================================
    // AZIONI DEL DOCENTE
    // ==========================================================

    // MODIFICA: Parametri già perfetti (int e String). Nessuna modifica.
    public void docenteAggiungiTirocinio(int id, String argomento) {
        if (utenteLoggato instanceof Docente) {
            Docente d = (Docente) utenteLoggato;
            d.aggiungiTirocinio(id, argomento);
        }
    }

    // MODIFICA: Invece dell'oggetto intero 'RichiestaTirocinio', riceviamo la matricola
    // dello studente a cui stiamo approvando la richiesta.
    @SuppressWarnings("java:S106")
    public void docenteValutaRichiesta(String matricolaStudente, boolean approva) {
        if (utenteLoggato instanceof Docente) {
            Docente d = (Docente) utenteLoggato;
            Studente mockStudente = new Studente("stud", "123", "stud@mail", "Nome", "Cognome", matricolaStudente);
            RichiestaTirocinio r = new RichiestaTirocinio(new Date(), mockStudente, d, new Tirocinio(1, "Mock"));

            d.valutaRichiesta(r, approva);

            // MODIFICA: Log dinamico per la console
            String esito = approva ? "ACCETTATA" : "RIFIUTATA";
            System.out.println("LOG: La richiesta di tirocinio per lo studente " + matricolaStudente + " è stata " + esito);
        }
    }
    // MODIFICA: Sostituito l'oggetto 'Tesi' con la matricola dello studente di cui si valuta la tesi.
    @SuppressWarnings("java:S106")
    public void docenteValutaTesi(String matricolaStudente, boolean approva) {
        if (utenteLoggato instanceof Docente) {
            Docente d = (Docente) utenteLoggato;
            Studente mockStudente = new Studente("stud", "123", "stud@mail", "Nome", "Cognome", matricolaStudente);
            Tesi t = new Tesi("Titolo Mock", "File Mock", mockStudente);

            d.valutaTesi(t, approva);

            // MODIFICA: Log dinamico per la console
            String esito = approva ? "ACCETTATA" : "RIFIUTATA";
            System.out.println("LOG: La tesi dello studente " + matricolaStudente + " è stata " + esito);
        }
    }
    // ==========================================================
    // AZIONI DEL COORDINATORE
    // ==========================================================

    // MODIFICA: Parametri già corretti (tutti tipi base). Nessun cambiamento.
    public void coordinatoreInserisciSeduta(Date data, String ora, String luogo, String codice) {
        if (utenteLoggato instanceof Coordinatore) {
            Coordinatore c = (Coordinatore) utenteLoggato;
            c.inserisciSeduta(data, ora, luogo, codice);
        }
    }

    // MODIFICA: Sostituiti gli oggetti 'Docente' e 'SedutaDiLaurea' con le rispettive Stringhe identificative (SSN e Codice).
    public void coordinatoreAggiungiDocenteACommissione(String ssnDocente, String codiceSeduta) {
        if (utenteLoggato instanceof Coordinatore) {
            Coordinatore c = (Coordinatore) utenteLoggato;

            Docente d = new Docente("doc", "123", "doc@mail", "Nome", "Cognome", ssnDocente);
            SedutaDiLaurea seduta = new SedutaDiLaurea(new Date(), "09:00", "Luogo", codiceSeduta);

            c.aggiungiDocenteACommissione(d, seduta);
        }
    }
}