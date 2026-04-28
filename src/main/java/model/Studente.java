package model;

public class Studente extends Utente{
    private String matricola;

    public Studente(String username, String password, String email, String nome, String cognome, String matricola){
        super(username,password,email,nome,cognome);
        this.matricola=matricola;
    }
}
