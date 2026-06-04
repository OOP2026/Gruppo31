package model;

/**
 * The type Tirocinio.
 */
public class Tirocinio {
    /**
     * The Id.
     */
    protected int id;
    /**
     * The Argomento.
     */
    protected String argomento;

    /**
     * Instantiates a new Tirocinio.
     *
     * @param id        the id
     * @param argomento the argomento
     */
    public Tirocinio(int id, String argomento){
        this.id=id;
        this.argomento=argomento;
    }

    public int getId() {
        return id;
    }
}
