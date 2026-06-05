package controller;

import dao.*;
import implementazionedao.*;
import model.*;
import java.util.ArrayList;
import java.util.Date;

public class Controller {

    private Utente utenteLoggato;
    private UtenteDAO utenteDao;
    private TirocinioDAO tirocinioDao;
    private TesiDAO tesiDao;
    private SedutaDAO sedutaDao;

    public Controller() {
        this.utenteDao = new UtentePostgresDao();
        this.tirocinioDao = new TirocinioPostgresDao();
        this.tesiDao = new TesiPostgresDao();
        this.sedutaDao = new SedutaPostgresDao();
    }

    public Utente getUtenteLoggato() { return utenteLoggato; }

    public boolean effettuaLogin(String username, String password) throws Exception {
        ArrayList<String> userData = new ArrayList<>();
        boolean success = utenteDao.loginDB(username, password, userData);

        if (success) {
            String ruolo = userData.get(0); String email = userData.get(1);
            String nome = userData.get(2); String cognome = userData.get(3);
            String matricola = userData.get(4); String ssn = userData.get(5);

            if ("Studente".equalsIgnoreCase(ruolo)) {
                utenteLoggato = new Studente(username, password, email, nome, cognome, matricola);
            } else if ("Coordinatore".equalsIgnoreCase(ruolo)) {
                utenteLoggato = new Coordinatore(username, password, email, nome, cognome, ssn);
            } else {
                utenteLoggato = new Docente(username, password, email, nome, cognome, ssn);
            }
            return true;
        }
        return false;
    }

    public void docenteAggiungiTirocinio(int id, String argomento) throws Exception {
        if (utenteLoggato instanceof Docente) {
            // Salva nel Database
            tirocinioDao.aggiungiTirocinioDB(id, argomento);

            // Salva nella RAM passando SOLO l'id (come avevi impostato tu all'inizio!)
            Docente d = (Docente) utenteLoggato;
            d.aggiungiTirocinio(id,argomento);

            System.out.println("LOG: Tirocinio " + id + " aggiunto in RAM.");
        }
    }

    public void docenteValutaRichiesta(String matricola, int idTirocinio, boolean approva) throws Exception {
        if (utenteLoggato instanceof Docente) {
            String stato = approva ? "ACCETTATO" : "RIFIUTATO";
            tirocinioDao.valutaRichiestaDB(matricola, idTirocinio, stato);

            Docente d = (Docente) utenteLoggato;
            Studente mockStudente = new Studente("stud", "", "", "", "", matricola);
            RichiestaTirocinio r = new RichiestaTirocinio(new Date(), mockStudente, d, new Tirocinio(idTirocinio, "Mock"));
            d.valutaRichiesta(r, approva);
        }
    }

    // Tornato alla versione originale (Senza idTesi)
    public void docenteValutaTesi(String matricola, boolean approva) throws Exception {
        if (utenteLoggato instanceof Docente) {
            String stato = approva ? "ACCETTATO" : "RIFIUTATO";

            // Passiamo solo la matricola e lo stato
            tesiDao.valutaTesiDB(matricola, stato);

            // Salvataggio nel Model
            Docente d = (Docente) utenteLoggato;
            Studente mockStudente = new Studente("stud", "", "", "", "", matricola);
            Tesi t = new Tesi("Titolo Mock", "File Mock", mockStudente);
            d.valutaTesi(t, approva);

            System.out.println("LOG: La tesi dello studente " + matricola + " è stata " + stato);
        }
    }

    public void studenteCaricaTesi(String titolo, String percorso) throws Exception {
        if (utenteLoggato instanceof Studente) {
            tesiDao.caricaTesiDB(titolo, percorso, utenteLoggato.getUsername());
        }
    }

    public void studenteRichiediTirocinio(String ssnRelatore, int idTirocinio) throws Exception {
        if (utenteLoggato instanceof Studente) {
            tirocinioDao.richiediTirocinioDB(new Date(), utenteLoggato.getUsername(), ssnRelatore, idTirocinio);
        }
    }

    public void studentePrenotaSeduta(String titoloTesi, String codiceSeduta) throws Exception {
        if (utenteLoggato instanceof Studente) {
            tesiDao.prenotaSedutaDB(new Date(), utenteLoggato.getUsername(), codiceSeduta);
        }
    }

    public void coordinatoreInserisciSeduta(Date data, String ora, String luogo, String codice) throws Exception {
        if (utenteLoggato instanceof Coordinatore) {
            sedutaDao.inserisciSedutaDB(data, ora, luogo, codice);
        }
    }

    public void coordinatoreAggiungiDocenteACommissione(String ssnDocente, String codiceSeduta) throws Exception {
        if (utenteLoggato instanceof Coordinatore) {
            sedutaDao.aggiungiDocenteACommissioneDB(ssnDocente, codiceSeduta);
        }
    }
}