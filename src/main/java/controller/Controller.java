package controller;

import dao.*;
import implementazionedao.*;
import model.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Questa è la classe principale che gestisce la logica del nostro sistema.
 * Praticamente fa da ponte tra l'interfaccia grafica (le varie View) e il database (i DAO).
 * Mantiene anche in memoria l'utente che ha fatto l'accesso in quel momento.
 */
public class Controller {
    private Utente utenteLoggato;
    private final UtenteDAO utenteDao;
    private final TirocinioDAO tirocinioDao;
    private final TesiDAO tesiDao;
    private final SedutaDAO sedutaDao;

    /**
     * Costruttore del Controller.
     * Qui inizializziamo tutti i DAO che ci serviranno per parlare col database Postgres.
     */
    public Controller() {
        this.utenteDao = new UtentePostgresDao();
        this.tirocinioDao = new TirocinioPostgresDao();
        this.tesiDao = new TesiPostgresDao();
        this.sedutaDao = new SedutaPostgresDao();
    }

    /**
     * Restituisce l'utente attualmente loggato nel sistema.
     * Serve alle interfacce per sapere chi sta facendo cosa.
     * * @return L'oggetto Utente loggato
     */
    public Utente getUtenteLoggato() {
        return utenteLoggato;
    }

    /**
     * Tenta di fare il login.
     * Passa username e password al DAO e, se il DB conferma, si salva i dati
     * e crea l'oggetto giusto (Studente, Docente o Coordinatore) in base al ruolo.
     * * @param username L'username inserito nel form
     * @param password La password inserita nel form
     * @return true se il login va a buon fine, false se le credenziali sono errate
     * @throws SQLException Se ci sono problemi di connessione col DB
     */
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

            // Istanzio la classe corretta sfruttando il polimorfismo
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

    /**
     * Prende i dati dal form di registrazione e li sbatte nel DB tramite il DAO.
     * * @param dati L'oggetto DTO con tutti i dati inseriti dall'utente
     * @throws SQLException Se la query fallisce (es. username già esistente)
     */
    public void effettuaRegistrazione(DatiRegistrazione dati) throws SQLException {
        utenteDao.registraUtenteDB(dati);
    }

    // =========================================================================
    // METODI PER IL DOCENTE
    // =========================================================================

    /**
     * Permette a un docente di aggiungere un nuovo argomento di tirocinio INTERNO.
     * * @param id L'identificativo scelto per il tirocinio
     * @param argomento Il titolo o descrizione del tirocinio
     * @throws SQLException In caso di errori col DB
     */
    public void docenteAggiungiTirocinio(int id, String argomento) throws SQLException {
        if (utenteLoggato instanceof Docente) {
            tirocinioDao.aggiungiTirocinioDB(id, argomento, ((Docente) utenteLoggato).getSsn());
        }
    }

    /**
     * Permette a un docente di aggiungere un tirocinio ESTERNO (in azienda).
     * Rispetto a quello interno, qui salviamo anche il nome dell'azienda e chi fa da referente.
     * * @param id L'ID del tirocinio
     * @param argomento La descrizione di cosa farà lo studente
     * @param azienda Il nome dell'azienda ospitante
     * @param referente Il nome del tutor aziendale
     * @throws SQLException In caso di errori col DB
     */
    public void docenteAggiungiTirocinioEsterno(int id, String argomento, String azienda, String referente) throws SQLException {
        if (utenteLoggato instanceof Docente) {
            tirocinioDao.aggiungiTirocinioEsternoDB(id, argomento, azienda, referente, ((Docente) utenteLoggato).getSsn());
        }
    }

    /**
     * Il professore decide se accettare o scartare la richiesta di tirocinio di uno studente.
     * * @param matricola La matricola dello studente che ha fatto richiesta
     * @param idTirocinio L'ID del tirocinio richiesto
     * @param approva true per accettare, false per rifiutare
     * @throws SQLException In caso di errori di scrittura sul DB
     */
    public void docenteValutaRichiesta(String matricola, int idTirocinio, boolean approva) throws SQLException {
        if (utenteLoggato instanceof Docente) {
            String stato = approva ? "ACCETTATO" : "RIFIUTATO";
            tirocinioDao.valutaRichiestaDB(matricola, idTirocinio, stato);
        }
    }

    /**
     * Il docente valuta l'elaborato (tesi) finale caricato dallo studente.
     * Se lo approva, lo studente potrà effettivamente laurearsi nella seduta prenotata.
     * * @param matricola La matricola dello studente
     * @param approva true se la tesi va bene, false se va rifatta/rifiutata
     * @throws SQLException Errore database
     */
    public void docenteValutaTesi(String matricola, boolean approva) throws SQLException {
        if (utenteLoggato instanceof Docente) {
            String stato = approva ? "ACCETTATO" : "RIFIUTATO";
            tesiDao.valutaTesiDB(matricola, stato);
        }
    }

    public List<String[]> getRichiestePerDocente() throws SQLException {
        if (utenteLoggato instanceof Docente) {
            return tirocinioDao.getRichiestePerDocenteDB(((Docente) utenteLoggato).getSsn());
        }
        return new ArrayList<>();
    }

    public List<String[]> getTesiPerDocente() throws SQLException {
        if (utenteLoggato instanceof Docente) {
            return tesiDao.getTesiPerDocenteDB(((Docente) utenteLoggato).getSsn());
        }
        return new ArrayList<>();
    }

    // =========================================================================
    // METODI PER LO STUDENTE
    // =========================================================================

    /**
     * Lo studente carica la sua tesi.
     * Prima di farlo, il metodo fa due controlli fondamentali imposti dalla segreteria:
     * 1. Devi avere per forza un tirocinio già approvato.
     * 2. Non puoi spammare tesi se ne hai già una in attesa di giudizio.
     * * @param titolo Il titolo dell'elaborato
     * @param percorso Il path del file PDF
     * @throws SQLException Problemi col DB
     * @throws IllegalArgumentException Se i controlli di validazione (regole di business) falliscono
     */
    public void studenteCaricaTesi(String titolo, String percorso) throws SQLException, IllegalArgumentException {
        if (utenteLoggato instanceof Studente) {
            // REGOLA 1: Deve avere un tirocinio approvato
            if (!tirocinioDao.haTirocinioApprovatoDB(utenteLoggato.getUsername())) {
                throw new IllegalArgumentException("Non puoi caricare la tesi se non hai prima un tirocinio approvato dal professore!");
            }

            // REGOLA 2: Non deve avere già una tesi pendente
            if (tesiDao.haTesiInAttesaDB(utenteLoggato.getUsername())) {
                throw new IllegalArgumentException("Hai già un elaborato in attesa di valutazione! Non puoi caricarne un altro finché il docente non si esprime.");
            }

            tesiDao.caricaTesiDB(titolo, percorso, utenteLoggato.getUsername());
        }
    }

    /**
     * Lo studente seleziona un tirocinio e manda la richiesta formale al professore.
     * * @param ssnRelatore L'SSN del professore referente
     * @param idTirocinio L'ID del tirocinio scelto
     * @throws SQLException Errore database
     */
    public void studenteRichiediTirocinio(String ssnRelatore, int idTirocinio) throws SQLException {
        if (utenteLoggato instanceof Studente) {
            tirocinioDao.richiediTirocinioDB(LocalDate.now(), utenteLoggato.getUsername(), ssnRelatore, idTirocinio);
        }
    }

    /**
     * Lo studente si iscrive (prenota) a una seduta di laurea specifica tra quelle disponibili.
     * * @param codiceSeduta Il codice della seduta (es. SED-01)
     * @throws SQLException Errore database
     */
    public void studentePrenotaSeduta(String codiceSeduta) throws SQLException {
        if (utenteLoggato instanceof Studente) {
            tesiDao.prenotaSedutaDB(LocalDate.now(), utenteLoggato.getUsername(), codiceSeduta);
        }
    }

    public List<String[]> getElencoTirociniDisponibili() throws SQLException {
        return tirocinioDao.getTirociniDisponibiliDB();
    }

    public List<String[]> getElencoSeduteDisponibili() throws SQLException {
        return sedutaDao.getSeduteDisponibiliDB();
    }

    public List<String[]> getStatoRichiesteStudente() throws SQLException {
        if (utenteLoggato instanceof Studente) {
            return tirocinioDao.getRichiesteStudenteDB(utenteLoggato.getUsername());
        }
        return new ArrayList<>();
    }

    // =========================================================================
    // METODI PER IL COORDINATORE
    // =========================================================================

    /**
     * Il coordinatore crea una nuova data utile per laurearsi (Seduta).
     * * @param data Data della seduta
     * @param ora Orario di inizio
     * @param luogo Aula o link Teams
     * @param codice Codice univoco (es. LUG2026)
     * @throws SQLException Errore database
     */
    public void coordinatoreInserisciSeduta(LocalDate data, String ora, String luogo, String codice) throws SQLException {
        if (utenteLoggato instanceof Coordinatore) {
            sedutaDao.inserisciSedutaDB(data, ora, luogo, codice);
        }
    }

    /**
     * Aggiunge un professore alla commissione di una determinata seduta.
     * Ci sono dei controlli tosti qui: il prof deve esistere e, da traccia, deve
     * essere relatore di ALMENO UNO studente approvato per quella seduta.
     * * @param ssnDocente L'SSN del professore da inserire in commissione
     * @param codiceSeduta Il codice della seduta di riferimento
     * @throws SQLException Errore database
     * @throws IllegalArgumentException Se il prof non esiste o non ha requisiti per stare in quella commissione
     */
    public void coordinatoreAggiungiDocenteACommissione(String ssnDocente, String codiceSeduta) throws SQLException, IllegalArgumentException {
        if (utenteLoggato instanceof Coordinatore) {

            // 1. Esiste questo prof?
            boolean esiste = sedutaDao.esisteDocenteDB(ssnDocente);
            if (!esiste) {
                throw new IllegalArgumentException("Errore: Il docente con SSN '" + ssnDocente + "' non esiste nel sistema universitario.");
            }

            // 2. Ha i requisiti? (Deve avere almeno un laureando in questa seduta)
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