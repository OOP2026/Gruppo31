package controller;

import dao.*;
import implementazionedao.*;
import model.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Il "cervello" dell'applicazione (Pattern MVC - Controller).
 * Gestisce la logica applicativa, riceve gli input dall'interfaccia grafica (GUI),
 * verifica i permessi dell'utente loggato e orchestra le operazioni tra il Modello (RAM)
 * e i Data Access Object (Database).
 */
public class Controller {

    // L'utente attualmente connesso al sistema
    private Utente utenteLoggato;

    // Riferimenti alle interfacce DAO per mascherare l'implementazione del DB
    private UtenteDAO utenteDao;
    private TirocinioDAO tirocinioDao;
    private TesiDAO tesiDao;
    private SedutaDAO sedutaDao;

    /**
     * Costruttore del Controller.
     * Inizializza le implementazioni concrete dei DAO (PostgreSQL).
     * Grazie all'uso delle interfacce, se un domani si volesse cambiare database (es. MySQL),
     * basterebbe cambiare solo queste istanziazioni senza toccare il resto del codice.
     */
    public Controller() {
        this.utenteDao = new UtentePostgresDao();
        this.tirocinioDao = new TirocinioPostgresDao();
        this.tesiDao = new TesiPostgresDao();
        this.sedutaDao = new SedutaPostgresDao();
    }

    /**
     * Restituisce l'utente attualmente loggato nel sistema.
     * @return un oggetto di tipo Utente (che a runtime sarà Studente, Docente o Coordinatore)
     */
    public Utente getUtenteLoggato() {
        return utenteLoggato;
    }

    /**
     * Gestisce il processo di autenticazione.
     * Interroga il database e, in caso di successo, utilizza una lista (userData) per
     * recuperare i dati anagrafici e istanziare la classe corretta in base al ruolo.
     *
     * @param username l'identificativo inserito
     * @param password la chiave d'accesso inserita
     * @return true se il login ha successo, false altrimenti
     * @throws Exception in caso di problemi col database
     */
    public boolean effettuaLogin(String username, String password) throws Exception {
        ArrayList<String> userData = new ArrayList<>();
        // Il DAO riempie l'ArrayList userData "per riferimento"
        boolean success = utenteDao.loginDB(username, password, userData);

        if (success) {
            String ruolo = userData.get(0);
            String email = userData.get(1);
            String nome = userData.get(2);
            String cognome = userData.get(3);
            String matricola = userData.get(4);
            String ssn = userData.get(5);

            // Routing basato sul ruolo: istanzia la classe figlia corretta
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

    // =================================================================
    // FUNZIONI DEL DOCENTE
    // =================================================================

    /**
     * Permette a un docente di inserire una nuova proposta di tirocinio.
     */
    public void docenteAggiungiTirocinio(int id, String argomento) throws Exception {
        // Guardia di sicurezza: solo un docente può eseguire questa operazione
        if (utenteLoggato instanceof Docente) {
            // 1. Salva in modo persistente nel Database
            tirocinioDao.aggiungiTirocinioDB(id, argomento);

            // 2. Salva nello stato in memoria (RAM)
            Docente d = (Docente) utenteLoggato;
            d.aggiungiTirocinio(id, argomento);

            System.out.println("LOG: Tirocinio " + id + " aggiunto in RAM.");
        }
    }

    /**
     * Permette al docente di valutare la candidatura di uno studente per un tirocinio.
     * Utilizza un "mock object" per aggiornare la memoria locale senza eseguire pesanti query di selezione.
     */
    public void docenteValutaRichiesta(String matricola, int idTirocinio, boolean approva) throws Exception {
        if (utenteLoggato instanceof Docente) {
            String stato = approva ? "ACCETTATO" : "RIFIUTATO";

            // Aggiorna il database
            tirocinioDao.valutaRichiestaDB(matricola, idTirocinio, stato);

            // Aggiorna il modello in memoria usando oggetti fittizi (Mock) per soddisfare la firma del metodo
            Docente d = (Docente) utenteLoggato;
            Studente mockStudente = new Studente("stud", "", "", "", "", matricola);
            RichiestaTirocinio r = new RichiestaTirocinio(new Date(), mockStudente, d, new Tirocinio(idTirocinio, "Mock"));
            d.valutaRichiesta(r, approva);
        }
    }

    /**
     * Permette al docente/relatore di valutare l'elaborato finale di uno studente.
     */
    public void docenteValutaTesi(String matricola, boolean approva) throws Exception {
        if (utenteLoggato instanceof Docente) {
            String stato = approva ? "ACCETTATO" : "RIFIUTATO";

            // Aggiorna lo stato sul database passando matricola e nuovo stato
            tesiDao.valutaTesiDB(matricola, stato);

            // Salvataggio nel Model tramite Mock Object
            Docente d = (Docente) utenteLoggato;
            Studente mockStudente = new Studente("stud", "", "", "", "", matricola);
            Tesi t = new Tesi("Titolo Mock", "File Mock", mockStudente);
            d.valutaTesi(t, approva);

            System.out.println("LOG: La tesi dello studente " + matricola + " è stata " + stato);
        }
    }

    // =================================================================
    // FUNZIONI DELLO STUDENTE
    // =================================================================

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

    // =================================================================
    // FUNZIONI DEL COORDINATORE
    // =================================================================

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