package model;

/**
 * Rappresenta un'opportunità di tirocinio proposta da un docente.
 * Questa classe contiene le informazioni di base necessarie allo studente
 * per identificare l'attività e comprendere l'argomento su cui verterà il progetto.
 */
public class Tirocinio {

    /**
     * L'identificativo numerico univoco del tirocinio.
     * Viene utilizzato come chiave per rintracciare e associare il tirocinio
     * alle varie richieste all'interno del database.
     */
    protected int id;

    /**
     * Il tema, il titolo o la descrizione sintetica dell'attività di tirocinio.
     */
    protected String argomento;

    /**
     * Costruttore della classe Tirocinio.
     * Crea una nuova istanza dell'offerta di tirocinio in memoria RAM.
     *
     * @param id        l'identificativo numerico univoco
     * @param argomento il tema centrale dell'attività proposta
     */
    public Tirocinio(int id, String argomento){
        this.id = id;
        this.argomento = argomento;
    }

    // --- GETTER ---

    /**
     * Restituisce l'identificativo del tirocinio.
     * @return il numero intero che identifica in modo univoco il progetto
     */
    public int getId() {
        return id;
    }
}