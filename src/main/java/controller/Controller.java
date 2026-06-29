package controller;

import dao.*;
import implementazionedao.*;
import model.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private Utente utenteLoggato;
    private final UtenteDAO utenteDao;
    private final TirocinioDAO tirocinioDao;
    private final TesiDAO tesiDao;
    private final SedutaDAO sedutaDao;

    public Controller() {
        this.utenteDao = new UtentePostgresDao();
        this.tirocinioDao = new TirocinioPostgresDao();
        this.tesiDao = new TesiPostgresDao();
        this.sedutaDao = new SedutaPostgresDao();
    }

    public Utente getUtenteLoggato() { return utenteLoggato; }

    public boolean effettuaLogin(String username, String password) throws SQLException {
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

    public void effettuaRegistrazione(DatiRegistrazione dati) throws SQLException {
        utenteDao.registraUtenteDB(dati);
    }

    // --- DOCENTE ---
    public void docenteAggiungiTirocinio(int id, String argomento) throws SQLException {
        if (utenteLoggato instanceof Docente) {
            tirocinioDao.aggiungiTirocinioDB(id, argomento, ((Docente) utenteLoggato).getSsn());
        }
    }

    public void docenteAggiungiTirocinioEsterno(int id, String argomento, String azienda, String referente) throws SQLException {
        if (utenteLoggato instanceof Docente) {
            tirocinioDao.aggiungiTirocinioEsternoDB(id, argomento, azienda, referente, ((Docente) utenteLoggato).getSsn());
        }
    }

    public void docenteValutaRichiesta(String matricola, int idTirocinio, boolean approva) throws SQLException {
        if (utenteLoggato instanceof Docente) {
            String stato = approva ? "ACCETTATO" : "RIFIUTATO";
            tirocinioDao.valutaRichiestaDB(matricola, idTirocinio, stato);
        }
    }

    public void docenteValutaTesi(String matricola, boolean approva) throws SQLException {
        if (utenteLoggato instanceof Docente) {
            String stato = approva ? "ACCETTATO" : "RIFIUTATO";
            tesiDao.valutaTesiDB(matricola, stato);
        }
    }

    public List<String[]> getRichiestePerDocente() throws SQLException {
        if (utenteLoggato instanceof Docente) return tirocinioDao.getRichiestePerDocenteDB(((Docente) utenteLoggato).getSsn());
        return new ArrayList<>();
    }

    public List<String[]> getTesiPerDocente() throws SQLException {
        if (utenteLoggato instanceof Docente) return tesiDao.getTesiPerDocenteDB(((Docente) utenteLoggato).getSsn());
        return new ArrayList<>();
    }

    // --- STUDENTE ---
    public void studenteCaricaTesi(String titolo, String percorso) throws SQLException, IllegalArgumentException {
        if (utenteLoggato instanceof Studente) {
            // REGOLA 1: Deve avere un tirocinio approvato
            if (!tirocinioDao.haTirocinioApprovatoDB(utenteLoggato.getUsername())) {
                throw new IllegalArgumentException("Non puoi caricare la tesi se non hai prima un tirocinio approvato dal professore!");
            }

            // REGOLA 2 (FIX): Non deve avere già una tesi pendente in attesa di valutazione
            if (tesiDao.haTesiInAttesaDB(utenteLoggato.getUsername())) {
                throw new IllegalArgumentException("Hai già un elaborato in attesa di valutazione! Non puoi caricarne un altro finché il docente non si esprime.");
            }

            tesiDao.caricaTesiDB(titolo, percorso, utenteLoggato.getUsername());
        }
    }

    public void studenteRichiediTirocinio(String ssnRelatore, int idTirocinio) throws SQLException {
        if (utenteLoggato instanceof Studente) {
            tirocinioDao.richiediTirocinioDB(LocalDate.now(), utenteLoggato.getUsername(), ssnRelatore, idTirocinio);
        }
    }

    public void studentePrenotaSeduta(String codiceSeduta) throws SQLException {
        if (utenteLoggato instanceof Studente) {
            tesiDao.prenotaSedutaDB(LocalDate.now(), utenteLoggato.getUsername(), codiceSeduta);
        }
    }

    public List<String[]> getElencoTirociniDisponibili() throws SQLException { return tirocinioDao.getTirociniDisponibiliDB(); }
    public List<String[]> getElencoSeduteDisponibili() throws SQLException { return sedutaDao.getSeduteDisponibiliDB(); }

    public List<String[]> getStatoRichiesteStudente() throws SQLException {
        if (utenteLoggato instanceof Studente) return tirocinioDao.getRichiesteStudenteDB(utenteLoggato.getUsername());
        return new ArrayList<>();
    }

    // --- COORDINATORE ---
    public void coordinatoreInserisciSeduta(LocalDate data, String ora, String luogo, String codice) throws SQLException {
        if (utenteLoggato instanceof Coordinatore) sedutaDao.inserisciSedutaDB(data, ora, luogo, codice);
    }

    public void coordinatoreAggiungiDocenteACommissione(String ssnDocente, String codiceSeduta) throws SQLException, IllegalArgumentException {
        if (utenteLoggato instanceof Coordinatore) {
            boolean esiste = sedutaDao.esisteDocenteDB(ssnDocente);
            if (!esiste) {
                throw new IllegalArgumentException("Errore: Il docente con SSN '" + ssnDocente + "' non esiste nel sistema universitario.");
            }

            boolean idoneo = sedutaDao.verificaDocenteValidoPerCommissione(ssnDocente, codiceSeduta);
            if (!idoneo) {
                throw new IllegalArgumentException("Docente non idoneo: il docente selezionato non è relatore di alcun candidato approvato per questa specifica seduta.");
            }

            sedutaDao.aggiungiDocenteACommissioneDB(ssnDocente, codiceSeduta);
        }
    }

    public List<String[]> getStudentiPerSeduta(String codiceSeduta) throws SQLException {
        return sedutaDao.getStudentiPerSedutaDB(codiceSeduta);
    }

    public List<String[]> getTuttiDocenti() throws SQLException {
        return sedutaDao.getTuttiDocentiDB();
    }

    public List<String[]> getDocentiPerCommissione(String codiceSeduta) throws SQLException {
        return sedutaDao.getDocentiPerCommissioneDB(codiceSeduta);
    }
}