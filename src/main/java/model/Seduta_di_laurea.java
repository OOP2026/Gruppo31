package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The type Seduta di laurea.
 */
public class Seduta_di_laurea {

    /**
     * The Data.
     */
    public Date data;
    /**
     * The Ora.
     */
    public String ora;
    /**
     * The Luogo.
     */
    public String luogo;
    /**
     * The Codice.
     */
    public String codice;

    /**
     * The Commissione.
     */
    List<Docente> commissione;
    /**
     * The Prenotazioni.
     */
    List<Prenotazione_Laurea> prenotazioni;

    /**
     * Instantiates a new Seduta di laurea.
     *
     * @param data   the data
     * @param ora    the ora
     * @param luogo  the luogo
     * @param codice the codice
     */
    public Seduta_di_laurea(Date data, String ora, String luogo, String codice) {
        this.data=data;
        this.ora=ora;
        this.luogo=luogo;
        this.codice=codice;

        this.commissione=new ArrayList<>();
        this.prenotazioni=new ArrayList<>();
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
