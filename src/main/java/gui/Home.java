package gui;

import controller.Controller;
import model.Coordinatore;
import model.Docente;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Home {

    private JPanel panel1;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblErrore;

    private static JFrame frame;
    private Controller controller;

    public static void main(String[] args) {
        frame = new JFrame("Gestione Sedute di Laurea");
        Controller controller = new Controller();
        frame.setContentPane(new Home(controller).panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public Home(Controller controller) {
        this.controller = controller;

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eseguiLogin();
            }
        });
    }

    private void eseguiLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            lblErrore.setText("Inserisci username e password.");
            return;
        }

        boolean ok = controller.effettuaLogin(username, password);
        if (!ok) {
            lblErrore.setText("Credenziali non valide.");
            txtPassword.setText("");
            return;
        }

        if (controller.getUtenteLoggato() instanceof Coordinatore) {
            DashboardCoordinatore dc = new DashboardCoordinatore(controller, frame);
            dc.frame.setVisible(true);
        } else if (controller.getUtenteLoggato() instanceof Docente) {
            DashboardDocente dd = new DashboardDocente(controller, frame);
            dd.frame.setVisible(true);
        } else {
            DashboardStudente ds = new DashboardStudente(controller, frame);
            ds.frame.setVisible(true);
        }

        frame.setVisible(false);
    }
}