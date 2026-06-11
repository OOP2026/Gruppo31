package gui;

import controller.Controller;

/**
 * Classe di avvio (Entry Point) dell'applicazione "Sistema Gestione Lauree".
 * Si occupa esclusivamente del bootstrap del sistema: inizializza il Controller
 * (il motore logico) e lancia la prima schermata dell'interfaccia grafica (Login),
 * mettendo in moto l'intera architettura MVC.
 */
public class Main {

    /**
     * Metodo principale eseguito all'avvio del programma dall'IDE o dal sistema operativo.
     *
     * @param args argomenti passati da riga di comando (non utilizzati in questa applicazione)
     */
    public static void main(String[] args) {
        // 1. Viene istanziato l'unico gestore logico del software.
        // Questo passaggio, dietro le quinte, istanzia anche tutti i DAO e prepara
        // l'applicazione a comunicare con il database PostgreSQL.
        Controller controller = new Controller();

        // 2. Viene creata la finestra di accesso iniziale.
        // Passiamo il controller al costruttore (Dependency Injection) in modo che
        // la View abbia un canale di comunicazione diretto con la logica di business.
        LoginFrame loginWindow = new LoginFrame(controller);

        // 3. Avvio effettivo dell'interfaccia grafica: rendiamo la finestra visibile all'utente.
        loginWindow.setVisible(true);
    }
}