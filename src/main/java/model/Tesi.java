package model;

public class Tesi {
    public String titolo;
    public String percorsoFile;
    public StatoTesi stato;
    public Studente studente;

    public Tesi(String titolo, String percorsoFile, Studente studente) {
        this.titolo = titolo;
        this.percorsoFile = percorsoFile;
        this.studente=studente;
    }
}