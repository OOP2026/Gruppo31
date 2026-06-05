package model;

import java.util.Date;

/**
 * The type Coordinatore.
 */
public class Coordinatore extends Docente{
    /**
     * The N corso laurea.
     */
    protected String nCorsoLaurea;

    /**
     * Instantiates a new Coordinatore.
     *
     * @param username the username
     * @param password the password
     * @param email    the email
     * @param nome     the nome
     * @param cognome  the cognome
     * @param ssn      the ssn
     */
    public Coordinatore(String username, String password, String email, String nome, String cognome, String ssn) {
        super(username, password, email, nome, cognome, ssn);
    }

    // Il metodo inserisciSeduta serve a creare una nuova seduta di laurea

    /**
     * Inserisci seduta seduta di laurea.
     *
     * @param data   the data
     * @param ora    the ora
     * @param luogo  the luogo
     * @param codice the codice
     * @return the seduta di laurea
     */
    @SuppressWarnings("java:S106")
    public SedutaDiLaurea inserisciSeduta(Date data, String ora, String luogo, String codice) {
        System.out.println("Nuova seduta creata il " + data + " in " + luogo);
        return new SedutaDiLaurea(data, ora, luogo, codice);
    }

    /**
     * Aggiungi docente a commissione.
     *
     * @param docente the docente
     * @param seduta  the seduta
     */
// AggiungiDocenteACommissione serve ad aggiungere uno specifico docente ad una seduta specifica
    @SuppressWarnings("java:S106")
    public void aggiungiDocenteACommissione(Docente docente, SedutaDiLaurea seduta) {
        seduta.aggiungiMembroCommissione(docente);

        // MODIFICA: Ora la console stampa in modo chiaro l'SSN del docente e il codice della seduta
        System.out.println("Docente con SSN: " + docente.getSsn() + " aggiunto alla commissione della seduta: " + seduta.getCodice());
    }
}
