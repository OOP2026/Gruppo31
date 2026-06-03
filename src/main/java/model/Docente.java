package model;
/**
 * The type Docente.
 */
public class Docente extends Utente{
    /**
     * The Ssn.
     */
    private String ssn;

    /**
     * Instantiates a new Docente.
     *
     * @param username the username
     * @param password the password
     * @param email    the email
     * @param nome     the nome
     * @param cognome  the cognome
     * @param ssn      the ssn
     */
    public Docente(String username, String password, String email, String nome, String cognome, String ssn){
        super(username, password, email, nome, cognome);
        this.ssn=ssn;
    }

    /**
     * Aggiungi tirocinio tirocinio.
     *
     * @param id        the id
     * @param argomento the argomento
     * @return the tirocinio
     */
// aggiungiTirocinio aggiunge un nuovo tirocinio tra i disponibili
    @SuppressWarnings("java:S106")
    public Tirocinio aggiungiTirocinio(int id, String argomento) {
        System.out.println("Nuovo tirocinio proposto: " + argomento);
        return new Tirocinio(id, argomento);
    }

    /**
     * Valuta richiesta.
     *
     * @param richiesta the richiesta
     * @param approvata the approvata
     */
// valutaRichiesta serve ad approvare o rifiutare una richiesta di tirocinio di uno studente
    @SuppressWarnings("java:S106")
    public void valutaRichiesta(RichiestaTirocinio richiesta, boolean approvata) {
        if (approvata) {
            richiesta.stato = StatoRichiesta.ACCETTATO;
            System.out.println("Richiesta di tirocinio approvata.");
        } else {
            richiesta.stato = StatoRichiesta.RIFIUTATO;
            System.out.println("Richiesta di tirocinio rifiutata.");
        }
    }

    /**
     * Valuta tesi.
     *
     * @param tesi      the tesi
     * @param approvata the approvata
     */
// valutaTesi serve ad approvare o rifiutare una tesi di uno studente
    @SuppressWarnings("java:S106")
    public void valutaTesi(Tesi tesi, boolean approvata) {
        if (approvata) {
            tesi.setStato(StatoTesi.ACCETTATO);
            System.out.println("Tesi approvata. Lo studente può procedere.");
        } else {
            tesi.setStato(StatoTesi.RIFIUTATO);
            System.out.println("Tesi rifiutata. Lo studente deve caricare una nuova versione.");
        }
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }
}
