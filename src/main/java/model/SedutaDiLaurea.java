package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Rappresenta un appello o evento di seduta di laurea.
 * È una classe centrale perché aggrega le informazioni logistiche (data, ora, luogo),
 * i professori che compongono la commissione esaminatrice e l'elenco degli studenti
 * prenotati per discutere la propria tesi.
 */
public class SedutaDiLaurea {

    /** La data ufficiale in cui si svolge la seduta. */
    private Date data;

    /** L'orario di inizio delle discussioni. */
    private String ora;

    /** L'aula, l'edificio o il link virtuale dove si terrà la seduta. */
    private String luogo;

    /** Un identificativo univoco per riconoscere la seduta (es. SED-2026-01). */
    private String codice;

    /** La lista dei professori (Docenti) assegnati dal Coordinatore a questa seduta. */
    private List<Docente> commissione;

    /** L'elenco degli studenti che si sono iscritti per laurearsi in questa specifica seduta. */
    private List<PrenotazioneLaurea> prenotazioni;

    /**
     * Costruttore per creare una nuova seduta di laurea.
     * Inizializza i dettagli logistici e prepara due liste vuote per accogliere
     * i futuri membri della commissione e gli studenti prenotati.
     *
     * @param data   il giorno della seduta
     * @param ora    l'orario di inizio
     * @param luogo  il posto fisico o virtuale dell'evento
     * @param codice il codice identificativo univoco
     */
    public SedutaDiLaurea(Date data, String ora, String luogo, String codice) {
        this.data = data;
        this.ora = ora;
        this.luogo = luogo;
        this.codice = codice;

        // Inizializziamo le liste vuote (ArrayList) per evitare fastidiosi NullPointerException
        // quando andremo ad aggiungere professori o studenti in un secondo momento.
        this.commissione = new ArrayList<>();
        this.prenotazioni = new ArrayList<>();
    }

    // --- GETTER & SETTER ---
    // Metodi standard per leggere o modificare le proprietà della seduta e le relative liste correlate.

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public List<Docente> getCommissione() {
        return commissione;
    }

    public void setCommissione(List<Docente> commissione) {
        this.commissione = commissione;
    }

    public List<PrenotazioneLaurea> getPrenotazioni() {
        return prenotazioni;
    }

    public void setPrenotazioni(List<PrenotazioneLaurea> prenotazioni) {
        this.prenotazioni = prenotazioni;
    }

    /**
     * Aggiunge un professore all'elenco dei membri della commissione esaminatrice.
     * Questo metodo viene tipicamente richiamato dal Coordinatore durante l'organizzazione
     * e la composizione della seduta.
     *
     * @param docente il professore da inserire nella commissione
     */
    public void aggiungiMembroCommissione(Docente docente) {
        this.commissione.add(docente);
    }
}