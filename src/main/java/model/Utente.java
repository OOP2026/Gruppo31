package model;

import java.util.ArrayList;

/**
 * The type Utente.
 */
public class Utente {
    /**
     * The Username.
     */
    protected String username;
    /**
     * The Password.
     */
    protected String password;
    /**
     * The Email.
     */
    protected String email;
    /**
     * The Nome.
     */
    protected String nome;
    /**
     * The Cognome.
     */
    protected String cognome;

    /**
     * Instantiates a new Utente.
     *
     * @param username the username
     * @param password the password
     * @param email    the email
     * @param nome     the nome
     * @param cognome  the cognome
     */
    public Utente(String username, String password, String email, String nome, String cognome){
        this.username=username;
        this.password=password;
        this.email=email;
        this.nome=nome;
        this.cognome=cognome;
    }

    /**
     * Login boolean.
     *
     * @param username the username
     * @param password the password
     * @return the boolean
     */
    public boolean login(String username, String password){
        if (this.username.equals(username) && this.password.equals(password)) {return true;}
        else return false;
    }
}
