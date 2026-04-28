package model;

public class Tirocinio_Esterno extends Tirocinio{
    private String n_aziendale; //INDICA IL REFERENTE AZIENDALE
    private String azienda;

    public Tirocinio_Esterno(int id, String argomento, String n_aziendale, String azienda){
        super(id, argomento);
        this.n_aziendale=n_aziendale;
        this.azienda=azienda;
    }
}
