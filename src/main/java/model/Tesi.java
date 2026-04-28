package model;

public class Tesi {
    private String titolo;
    private String percorsoFile;
    private StatoTesi stato;

    public Tesi(String titolo, String percorsoFile) {
        this.titolo = titolo;
        this.percorsoFile = percorsoFile;
        //Stato default per una nuova tesi
        this.stato = StatoTesi.In_Attesa;
    }

    public StatoTesi getStato() {
        return this.stato;
    }

    public void setStato(StatoTesi stato) {
        this.stato = stato;
    }
}