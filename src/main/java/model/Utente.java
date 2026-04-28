package model;

import java.util.ArrayList;

public class Utente {
    protected String username;
    protected String password;
    protected String email;
    protected String nome;
    protected String cognome;

    public Utente(String username, String password, String email, String nome, String cognome){
        this.username=username;
        this.password=password;
        this.email=email;
        this.nome=nome;
        this.cognome=cognome;
        //ricchione
    }
}
