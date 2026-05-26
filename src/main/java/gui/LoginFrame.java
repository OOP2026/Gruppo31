package gui;

import controller.Controller;
import model.Coordinatore;
import model.Docente;
import model.Studente;

import javax.swing.*;

public class LoginFrame extends JFrame {

    // Aggiunto "transient" per la serializzazione
    private transient Controller controller;

    private JPanel panel1;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblErrore;

    public LoginFrame(Controller controller) {
        this.controller = controller;

        setContentPane(panel1);
        setTitle("Sistema Gestione Lauree - Login");
        setSize(400, 300);

        // Sostituito JFrame con WindowConstants
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        lblErrore.setText("");

        // Azione del bottone convertita in Lambda Expression
        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            boolean loginSuccesso = controller.effettuaLogin(username, password);

            if (loginSuccesso) {
                lblErrore.setText("");

                // A seconda di chi si connette, apriamo la finestra corretta
                if (controller.getUtenteLoggato() instanceof Coordinatore) {
                    new CoordinatoreFrame(controller).setVisible(true);
                }
                else if (controller.getUtenteLoggato() instanceof Docente) {
                    new DocenteFrame(controller).setVisible(true);
                }
                else if (controller.getUtenteLoggato() instanceof Studente) {
                    new StudenteFrame(controller).setVisible(true);
                }

                dispose(); // Chiudiamo solo la schermata di login
            } else {
                lblErrore.setText("Username o Password errati!");
                lblErrore.setForeground(java.awt.Color.RED);
            }
        });
    }
}