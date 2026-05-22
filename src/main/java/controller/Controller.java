package controller;

import model.*;
import java.util.Date;

public class Controller {

    // Il controller si ricorda chi ha effettuato l'accesso
    private Utente utenteLoggato;

    public Controller() {
    }

    // ==========================================================
    // LOGIN
    // ==========================================================

    public boolean effettuaLogin(String username, String password) {
        // Dato che non abbiamo il database, creiamo degli utenti "fittizi"

        if(username.equals("studente") && password.equals("123")) {
            utenteLoggato = new Studente(username, password, "stud@unina.it", "Mario", "Rossi", "N46001");
            return true;
        } else if (username.equals("docente") && password.equals("123")) {
            utenteLoggato = new Docente(username, password, "doc@unina.it", "Luigi", "Verdi", "VRDLGU");
            return true;
        } else if (username.equals("coord") && password.equals("123")) {
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
        // Controlliamo che l'utente loggato sia davvero uno studente
        if (utenteLoggato instanceof Studente) {
            Studente s = (Studente) utenteLoggato;

            // Chiamiamo il metodo del model
            Richiesta_Tirocinio rt = s.richiediTirocinio(relatore, tirocinio, new Date());
        }
    }

    public void studenteCaricaTesi(String titolo, String percorsoFile) {
        if (utenteLoggato instanceof Studente) {
            Studente s = (Studente) utenteLoggato;

            // Chiamiamo il metodo del model
            Tesi t = s.caricaTesi(titolo, percorsoFile);
        }
    }

    // ==========================================================
    // AZIONI DEL DOCENTE
    // ==========================================================

    public void docenteAggiungiTirocinio(int id, String argomento) {
        if (utenteLoggato instanceof Docente) {
            Docente d = (Docente) utenteLoggato;
            Tirocinio t = d.aggiungiTirocinio(id, argomento);
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
            Seduta_di_laurea seduta = c.inserisciSeduta(data, ora, luogo, codice);
        }
    }
}