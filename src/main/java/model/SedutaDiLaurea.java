package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The type Seduta di laurea.
 */
public class SedutaDiLaurea {

    private Date data;
    private String ora;
    private String luogo;
    private String codice;

    private List<Docente> commissione;
    private List<PrenotazioneLaurea> prenotazioni;

    /**
     * Instantiates a new Seduta di laurea.
     *
     * @param data   the data
     * @param ora    the ora
     * @param luogo  the luogo
     * @param codice the codice
     */
    public SedutaDiLaurea(Date data, String ora, String luogo, String codice) {
        this.data = data;
        this.ora = ora;
        this.luogo = luogo;
        this.codice = codice;

        this.commissione = new ArrayList<>();
        this.prenotazioni = new ArrayList<>();
    }

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
     * Aggiungi membro commissione.
     *
     * @param docente the docente
     */
    public void aggiungiMembroCommissione(Docente docente) {
        this.commissione.add(docente);
    }
}