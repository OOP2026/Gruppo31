package model;

/**
 * The type Tirocinio esterno.
 */
public class TirocinioEsterno extends Tirocinio {
    private String nAziendale; //INDICA IL REFERENTE AZIENDALE
    private String azienda;

    /**
     * Instantiates a new Tirocinio esterno.
     *
     * @param id         the id
     * @param argomento  the argomento
     * @param nAziendale the n aziendale
     * @param azienda    the azienda
     */
    public TirocinioEsterno(int id, String argomento, String nAziendale, String azienda) {
        super(id, argomento);
        this.nAziendale = nAziendale;
        this.azienda = azienda;
    }

    public String getNAziendale() {
        return nAziendale;
    }

    public void setNAziendale(String nAziendale) {
        this.nAziendale = nAziendale;
    }

    public String getAzienda() {
        return azienda;
    }

    public void setAzienda(String azienda) {
        this.azienda = azienda;
    }
}