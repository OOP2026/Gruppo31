package gui;

import controller.Controller;
import model.Coordinatore;
import model.Docente;
import model.Studente;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private Controller controller;

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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        lblErrore.setText("");

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());

                boolean loginSuccesso = controller.effettuaLogin(username, password);

                if (loginSuccesso) {
                    lblErrore.setText("");

                    // A seconda di chi si connette, apriamo la finestra corretta passandogli il controller
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
            }
        });
    }
}