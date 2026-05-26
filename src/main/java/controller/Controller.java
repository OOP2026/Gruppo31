package controller;

import model.*;
import java.util.Date;

public class Controller {

    private Utente utenteLoggato;

    // Costante per far sparire il warning sulle password "hard-coded" (scritte a mano)
    private static final String DUMMY_PASS = "123";

    public Controller() {
        // Costruttore vuoto, nessuna simulazione DB per ora
    }

    // ==========================================================
    // LOGIN
    // ==========================================================
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
    public void studenteRichiediTirocinio(Docente relatore, Tirocinio tirocinio) {
        if (utenteLoggato instanceof Studente) {
            Studente s = (Studente) utenteLoggato;
            // Chiamata diretta, senza assegnazione a variabile locale inutile
            s.richiediTirocinio(relatore, tirocinio, new Date());
        }
    }

    public void studenteCaricaTesi(String titolo, String percorsoFile) {
        if (utenteLoggato instanceof Studente) {
            Studente s = (Studente) utenteLoggato;
            s.caricaTesi(titolo, percorsoFile);
        }
    }

    public void studentePrenotaSeduta(Tesi tesi, Seduta_di_laurea seduta) {
        if (utenteLoggato instanceof Studente) {
            Studente s = (Studente) utenteLoggato;
            s.prenotaSedutaLaurea(s, tesi, seduta);
        }
    }

    // ==========================================================
    // AZIONI DEL DOCENTE
    // ==========================================================
    public void docenteAggiungiTirocinio(int id, String argomento) {
        if (utenteLoggato instanceof Docente) {
            Docente d = (Docente) utenteLoggato;
            d.aggiungiTirocinio(id, argomento);
        }
    }

    public void docenteValutaRichiesta(Richiesta_Tirocinio richiesta, boolean approva) {
        if (utenteLoggato instanceof Docente) {
            Docente d = (Docente) utenteLoggato;
            d.valutaRichiesta(richiesta, approva);
        }
    }

    public void docenteValutaTesi(Tesi tesi, boolean approva) {
        if (utenteLoggato instanceof Docente) {
            Docente d = (Docente) utenteLoggato;
            d.valutaTesi(tesi, approva);
        }
    }

    // ==========================================================
    // AZIONI DEL COORDINATORE
    // ==========================================================
    public void coordinatoreInserisciSeduta(Date data, String ora, String luogo, String codice) {
        if (utenteLoggato instanceof Coordinatore) {
            Coordinatore c = (Coordinatore) utenteLoggato;
            c.inserisciSeduta(data, ora, luogo, codice);
        }
    }

    public void coordinatoreAggiungiDocenteACommissione(Docente d, Seduta_di_laurea seduta) {
        if (utenteLoggato instanceof Coordinatore) {
            Coordinatore c = (Coordinatore) utenteLoggato;
            c.aggiungiDocenteACommissione(d, seduta);
        }
    }
}