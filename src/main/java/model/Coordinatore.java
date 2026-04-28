package model;

public class Coordinatore extends Docente{
    private String n_corso_laurea;

    public Coordinatore(String username, String password, String email, String nome, String cognome, String ssn, String corso){
        super(username, password, email, nome, cognome, ssn);
        this.n_corso_laurea=corso;
    }
}
