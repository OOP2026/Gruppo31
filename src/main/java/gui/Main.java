package gui;

import controller.Controller;

/**
 * Classe di avvio (Entry Point) del nostro gestionale.
 * È da qui che parte tutto quando schiacciamo "Run" sull'IDE.
 */
public class Main {

    /**
     * Il main si occupa solo di far partire il motore logico (Controller)
     * e di aprire la prima finestra visibile all'utente (Login).
     * * @param args Argomenti da riga di comando (non usati)
     */
    public static void main(String[] args) {
        Controller controller = new Controller();
        LoginFrame loginWindow = new LoginFrame(controller);
        loginWindow.setVisible(true);
    }
}