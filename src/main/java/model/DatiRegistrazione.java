package model;

//Classe creata per risolvere problemi di SonarCube

public class DatiRegistrazione {
    private String username;
    private String password;
    private String email;
    private String nome;
    private String cognome;
    private String ruolo;
    private String matricola;
    private String ssn;

    public DatiRegistrazione(String username, String password, String email, String nome, String cognome, String ruolo, String matricola, String ssn) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
        this.ruolo = ruolo;
        this.matricola = matricola;
        this.ssn = ssn;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getNome() { return nome; }
    public String getCognome() { return cognome; }
    public String getRuolo() { return ruolo; }
    public String getMatricola() { return matricola; }
    public String getSsn() { return ssn; }
}