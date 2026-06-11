package model;

/**
 * Rappresenta un'attività di tirocinio svolta all'esterno dell'università,
 * presso un ente o un'azienda partner.
 * Estende la classe base 'Tirocinio' ereditandone l'ID e l'argomento, ma
 * aggiunge i dettagli specifici dell'ambiente lavorativo ospitante.
 */
public class TirocinioEsterno extends Tirocinio {

    /**
     * Il nome o il contatto del referente aziendale (il "tutor" interno all'azienda)
     * che supervisionerà lo studente durante il periodo di stage.
     */
    private String nAziendale;

    /**
     * La denominazione ufficiale dell'azienda o dell'ente che ospita il tirocinante.
     */
    private String azienda;

    /**
     * Costruttore della classe TirocinioEsterno.
     * Sfrutta il costruttore della superclasse (Tirocinio) per impostare i dati base,
     * per poi inizializzare le informazioni specifiche dell'azienda.
     *
     * @param id         l'identificativo numerico univoco del tirocinio
     * @param argomento  il tema centrale del progetto o delle mansioni previste
     * @param nAziendale il nome del tutor/referente assegnato dall'azienda
     * @param azienda    il nome dell'azienda ospitante
     */
    public TirocinioEsterno(int id, String argomento, String nAziendale, String azienda) {
        // Passiamo l'ID e l'argomento alla classe padre
        super(id, argomento);
        this.nAziendale = nAziendale;
        this.azienda = azienda;
    }

    // --- GETTER & SETTER ---

    /**
     * Restituisce il nome del referente aziendale.
     * @return una stringa con il nome del tutor
     */
    public String getNAziendale() {
        return nAziendale;
    }

    /**
     * Aggiorna o modifica il referente aziendale.
     * @param nAziendale il nuovo tutor assegnato allo studente
     */
    public void setNAziendale(String nAziendale) {
        this.nAziendale = nAziendale;
    }

    /**
     * Restituisce il nome dell'azienda ospitante.
     * @return la denominazione dell'azienda
     */
    public String getAzienda() {
        return azienda;
    }

    /**
     * Modifica l'azienda associata a questo tirocinio.
     * @param azienda il nuovo nome dell'azienda partner
     */
    public void setAzienda(String azienda) {
        this.azienda = azienda;
    }
}