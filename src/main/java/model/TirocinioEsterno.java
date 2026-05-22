package model;

/**
 * The type Tirocinio esterno.
 */
public class TirocinioEsterno extends Tirocinio{
    private String n_aziendale; //INDICA IL REFERENTE AZIENDALE
    private String azienda;

    /**
     * Instantiates a new Tirocinio esterno.
     *
     * @param id          the id
     * @param argomento   the argomento
     * @param n_aziendale the n aziendale
     * @param azienda     the azienda
     */
    public TirocinioEsterno(int id, String argomento, String n_aziendale, String azienda){
        super(id, argomento);
        this.n_aziendale=n_aziendale;
        this.azienda=azienda;
    }
}
