package model;

public class Docente extends Utente{
    private String ssn;

    public Docente(String username, String password, String email, String nome, String cognome, String ssn){
        super(username, password, email, nome, cognome);
        this.ssn=ssn;
    }
}
