package gui;

import controller.Controller;

public class Main {
    public static void main(String[] args) {
        // 1. Viene istanziato l'unico gestore logico del software
        Controller controller = new Controller();

        // 2. Viene creata la finestra passandogli il controller
        LoginFrame loginWindow = new LoginFrame(controller);

        // 3. Rendiamo la finestra visibile a schermo
        loginWindow.setVisible(true);
    }
}