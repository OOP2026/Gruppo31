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
            String ruolo = userData.get(0);
            String email = userData.get(1);
            String nome = userData.get(2);
            String cognome = userData.get(3);
            String matricola = userData.get(4);
            String ssn = userData.get(5);

            if ("Studente".equalsIgnoreCase(ruolo)) utenteLoggato = new Studente(username, password, email, nome, cognome, matricola);
            else if ("Coordinatore".equalsIgnoreCase(ruolo)) utenteLoggato = new Coordinatore(username, password, email, nome, cognome, ssn);
            else utenteLoggato = new Docente(username, password, email, nome, cognome, ssn);
            return true;
        }
        return false;
    }

    public void effettuaRegistrazione(String username, String password, String email, String nome, String cognome, String ruolo, String matricola, String ssn) throws Exception {
        utenteDao.registraUtenteDB(username, password, email, nome, cognome, ruolo, matricola, ssn);
    }

    // --- DOCENTE ---
    public void docenteAggiungiTirocinio(int id, String argomento) throws Exception {
        if (utenteLoggato instanceof Docente) {
            tirocinioDao.aggiungiTirocinioDB(id, argomento, ((Docente) utenteLoggato).getSsn());
        }
    }

    public void docenteAggiungiTirocinioEsterno(int id, String argomento, String azienda, String referente) throws Exception {
        if (utenteLoggato instanceof Docente) {
            tirocinioDao.aggiungiTirocinioEsternoDB(id, argomento, azienda, referente, ((Docente) utenteLoggato).getSsn());
        }
    }

    public void docenteValutaRichiesta(String matricola, int idTirocinio, boolean approva) throws Exception {
        if (utenteLoggato instanceof Docente) {
            String stato = approva ? "ACCETTATO" : "RIFIUTATO";
            tirocinioDao.valutaRichiestaDB(matricola, idTirocinio, stato);
        }
    }

    public void docenteValutaTesi(String matricola, boolean approva) throws Exception {
        if (utenteLoggato instanceof Docente) {
            String stato = approva ? "ACCETTATO" : "RIFIUTATO";
            tesiDao.valutaTesiDB(matricola, stato);
        }
    }

    public java.util.List<String[]> getRichiestePerDocente() throws Exception {
        if (utenteLoggato instanceof Docente) return tirocinioDao.getRichiestePerDocenteDB(((Docente) utenteLoggato).getSsn());
        return new java.util.ArrayList<>();
    }

    public java.util.List<String[]> getTesiPerDocente() throws Exception {
        if (utenteLoggato instanceof Docente) return tesiDao.getTesiPerDocenteDB(((Docente) utenteLoggato).getSsn());
        return new java.util.ArrayList<>();
    }

    // --- STUDENTE ---
    public void studenteCaricaTesi(String titolo, String percorso) throws Exception {
        if (utenteLoggato instanceof Studente) {
            if (!tirocinioDao.haTirocinioApprovatoDB(utenteLoggato.getUsername())) {
                throw new Exception("Non puoi caricare la tesi se non hai prima un tirocinio approvato dal professore!");
            }
            tesiDao.caricaTesiDB(titolo, percorso, utenteLoggato.getUsername());
        }
    }

    public void studenteRichiediTirocinio(String ssnRelatore, int idTirocinio) throws Exception {
        if (utenteLoggato instanceof Studente) tirocinioDao.richiediTirocinioDB(new Date(), utenteLoggato.getUsername(), ssnRelatore, idTirocinio);
    }

    public void studentePrenotaSeduta(String codiceSeduta) throws Exception {
        if (utenteLoggato instanceof Studente) tesiDao.prenotaSedutaDB(new Date(), utenteLoggato.getUsername(), codiceSeduta);
    }

    public java.util.List<String[]> getElencoTirociniDisponibili() throws Exception { return tirocinioDao.getTirociniDisponibiliDB(); }
    public java.util.List<String[]> getElencoSeduteDisponibili() throws Exception { return sedutaDao.getSeduteDisponibiliDB(); }

    public java.util.List<String[]> getStatoRichiesteStudente() throws Exception {
        if (utenteLoggato instanceof Studente) return tirocinioDao.getRichiesteStudenteDB(utenteLoggato.getUsername());
        return new java.util.ArrayList<>();
    }

    // --- COORDINATORE ---
    public void coordinatoreInserisciSeduta(Date data, String ora, String luogo, String codice) throws Exception {
        if (utenteLoggato instanceof Coordinatore) sedutaDao.inserisciSedutaDB(data, ora, luogo, codice);
    }

    public void coordinatoreAggiungiDocenteACommissione(String ssnDocente, String codiceSeduta) throws Exception {
        if (utenteLoggato instanceof Coordinatore) {
            boolean idoneo = sedutaDao.verificaDocenteValidoPerCommissione(ssnDocente, codiceSeduta);
            if (!idoneo) throw new Exception("Docente non idoneo: non è relatore di alcun candidato approvato per questa seduta.");
            sedutaDao.aggiungiDocenteACommissioneDB(ssnDocente, codiceSeduta);
        }
    }

    public java.util.List<String[]> getStudentiPerSeduta(String codiceSeduta) throws Exception {
        return sedutaDao.getStudentiPerSedutaDB(codiceSeduta);
    }

    public java.util.List<String[]> getTuttiDocenti() throws Exception {
        return sedutaDao.getTuttiDocentiDB();
    }

    public java.util.List<String[]> getDocentiPerCommissione(String codiceSeduta) throws Exception {
        return sedutaDao.getDocentiPerCommissioneDB(codiceSeduta);
    }
}