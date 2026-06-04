package controller;

import dao.*;
import implementazioneDao.*;
import model.*;
import java.util.ArrayList;
import java.util.Date;

public class Controller {

    private Utente utenteLoggato;
    private UtenteDAO utenteDao;
    private TirocinioDAO tirocinioDao;
    private SedutaDAO sedutaDao;
    private TesiDAO tesiDao;

    public Controller() {
        this.utenteDao = new UtentePostgresDao();
        this.tirocinioDao = new TirocinioPostgresDao();
        this.sedutaDao = new SedutaPostgresDao();
        this.tesiDao = new TesiPostgresDao();
    }

    // ==========================================================
    // LOGIN
    // ==========================================================
    public boolean effettuaLogin(String username, String password) throws Exception {
        ArrayList<String> userData = new ArrayList<>();
        if (utenteDao.loginDB(username, password, userData)) {
            String ruolo = userData.get(0);
            if (ruolo.equals("STUDENTE")) {
                utenteLoggato = new Studente(username, password, userData.get(1), userData.get(2), userData.get(3), userData.get(4));
            } else if (ruolo.equals("DOCENTE")) {
                utenteLoggato = new Docente(username, password, userData.get(1), userData.get(2), userData.get(3), userData.get(5));
            } else if (ruolo.equals("COORDINATORE")) {
                utenteLoggato = new Coordinatore(username, password, userData.get(1), userData.get(2), userData.get(3), userData.get(5), userData.get(6));
            }
            return true;
        }
        return false;
    }

    public Utente getUtenteLoggato() { return utenteLoggato; }

    // ==========================================================
    // AZIONI DELLO STUDENTE
    // ==========================================================
    public void studenteRichiediTirocinio(String ssnRelatore, int idTirocinio) throws Exception {
        if (utenteLoggato instanceof Studente) {
            // 1. Salva nel Database
            tirocinioDao.richiediTirocinioDB(new Date(), utenteLoggato.getUsername(), ssnRelatore, idTirocinio);

            // 2. Salva nel Model (RAM) e fa partire il System.out.println
            Studente s = (Studente) utenteLoggato;
            Docente relatore = new Docente("mock", "", "", "", "", ssnRelatore);
            Tirocinio tirocinio = new Tirocinio(idTirocinio, "Argomento");
            s.richiediTirocinio(relatore, tirocinio, new Date());
        }
    }

    public void studenteCaricaTesi(String titolo, String percorsoFile) throws Exception {
        if (utenteLoggato instanceof Studente) {
            // 1. Salva nel Database
            tesiDao.caricaTesiDB(titolo, percorsoFile, utenteLoggato.getUsername());

            // 2. Salva nel Model (RAM)
            Studente s = (Studente) utenteLoggato;
            s.caricaTesi(titolo, percorsoFile);
        }
    }

    public void studentePrenotaSeduta(String titoloTesi, String codiceSeduta) throws Exception {
        if (utenteLoggato instanceof Studente) {
            // 1. Salva nel Database
            tesiDao.prenotaSedutaDB(new Date(), utenteLoggato.getUsername(), codiceSeduta);

            // 2. Salva nel Model (RAM)
            Studente s = (Studente) utenteLoggato;
            Tesi tesi = new Tesi(titoloTesi, "percorso", s);
            SedutaDiLaurea seduta = new SedutaDiLaurea(new Date(), "09:00", "Luogo", codiceSeduta);
            s.prenotaSedutaLaurea(s, tesi, seduta, new Date());
        }
    }

    // ==========================================================
    // AZIONI DEL DOCENTE
    // ==========================================================
    public void docenteAggiungiTirocinio(int id, String argomento) throws Exception {
        if (utenteLoggato instanceof Docente) {
            // 1. Salva nel Database
            tirocinioDao.aggiungiTirocinioDB(id, argomento);

            // 2. Salva nel Model (RAM)
            Docente d = (Docente) utenteLoggato;
            d.aggiungiTirocinio(id, argomento);
            System.out.println("LOG: Tirocinio " + id + " aggiunto correttamente al sistema.");
        }
    }

    public void docenteValutaRichiesta(String matricola, int idTirocinio, boolean approva) throws Exception {
        if (utenteLoggato instanceof Docente) {
            String stato = approva ? "ACCETTATO" : "RIFIUTATO";
            // Passiamo anche l'ID al DAO se la tua query SQL lo richiede
            tirocinioDao.valutaRichiestaDB(matricola, idTirocinio, stato);

            // 2. Salva nel Model (RAM)
            Docente d = (Docente) utenteLoggato;
            Studente mockStudente = new Studente("stud", "", "", "", "", matricola);
            RichiestaTirocinio r = new RichiestaTirocinio(new Date(), mockStudente, d, new Tirocinio(1, "Mock"));
            d.valutaRichiesta(r, approva);
            System.out.println("LOG: La richiesta di tirocinio per lo studente " + matricola + " è stata " + stato);
        }
    }

    public void docenteValutaTesi(String matricolaStudente, boolean approva) throws Exception {
        if (utenteLoggato instanceof Docente) {
            // 1. Salva nel Database
            String stato = approva ? "ACCETTATO" : "RIFIUTATO";
            tesiDao.valutaTesiDB(matricolaStudente, stato);

            // 2. Salva nel Model (RAM)
            Docente d = (Docente) utenteLoggato;
            Studente mockStudente = new Studente("stud", "", "", "", "", matricolaStudente);
            Tesi t = new Tesi("Titolo Mock", "File Mock", mockStudente);
            d.valutaTesi(t, approva);
            System.out.println("LOG: La tesi dello studente " + matricolaStudente + " è stata " + stato);
        }
    }

    // ==========================================================
    // AZIONI DEL COORDINATORE
    // ==========================================================
    public void coordinatoreInserisciSeduta(Date data, String ora, String luogo, String codice) throws Exception {
        if (utenteLoggato instanceof Coordinatore) {
            // 1. Salva nel Database
            sedutaDao.inserisciSedutaDB(data, ora, luogo, codice);

            // 2. Salva nel Model (RAM)
            Coordinatore c = (Coordinatore) utenteLoggato;
            c.inserisciSeduta(data, ora, luogo, codice);
            System.out.println("LOG: Seduta " + codice + " creata con successo.");
        }
    }

    public void coordinatoreAggiungiDocenteACommissione(String ssnDocente, String codiceSeduta) throws Exception {
        if (utenteLoggato instanceof Coordinatore) {
            // 1. Salva nel Database
            sedutaDao.aggiungiDocenteACommissioneDB(ssnDocente, codiceSeduta);

            // 2. Salva nel Model (RAM)
            Coordinatore c = (Coordinatore) utenteLoggato;
            Docente d = new Docente("doc", "", "", "", "", ssnDocente);
            SedutaDiLaurea seduta = new SedutaDiLaurea(new Date(), "09:00", "Luogo", codiceSeduta);
            c.aggiungiDocenteACommissione(d, seduta);
        }
    }
}