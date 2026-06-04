package gui;

import controller.Controller;
import model.Coordinatore;
import model.Docente;
import model.Studente;
import javax.swing.*;

public class LoginFrame extends JFrame {
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
        setSize(350, 200);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        lblErrore.setText("");

        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            try {
                boolean loginSuccesso = controller.effettuaLogin(username, password);

                if (loginSuccesso) {
                    lblErrore.setText("");
                    if (controller.getUtenteLoggato() instanceof Coordinatore) {
                        new CoordinatoreFrame(controller).setVisible(true);
                    } else if (controller.getUtenteLoggato() instanceof Docente) {
                        new DocenteFrame(controller).setVisible(true);
                    } else if (controller.getUtenteLoggato() instanceof Studente) {
                        new StudenteFrame(controller).setVisible(true);
                    }
                    dispose();
                } else {
                    lblErrore.setText("Username o Password errati!");
                    lblErrore.setForeground(java.awt.Color.RED);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(LoginFrame.this,
                        "Errore di connessione al Database:\n" + ex.getMessage(),
                        "Errore di Sistema", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}