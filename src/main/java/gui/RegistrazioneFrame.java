package gui;

import controller.Controller;
import model.DatiRegistrazione;
import javax.swing.*;

public class RegistrazioneFrame extends JFrame {
    private transient Controller controller;
    private JPanel panel1;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtEmail;
    private JTextField txtNome;
    private JTextField txtCognome;
    private JComboBox<String> cmbRuolo;
    private JTextField txtMatricola;
    private JTextField txtSsn;
    private JButton btnSalva;
    private JButton btnAnnulla;

    public RegistrazioneFrame(Controller controller) {
        this.controller = controller;
        setContentPane(panel1);
        setTitle("Sistema Gestione Lauree - Registrazione");
        setSize(400, 450);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        if(cmbRuolo.getItemCount() == 0) {
            cmbRuolo.addItem("Studente");
            cmbRuolo.addItem("Docente");
            cmbRuolo.addItem("Coordinatore");
        }

        cmbRuolo.addActionListener(e -> {
            String ruolo = (String) cmbRuolo.getSelectedItem();
            boolean isStudente = "Studente".equals(ruolo);
            txtMatricola.setEnabled(isStudente);
            txtSsn.setEnabled(!isStudente);
            if (isStudente) txtSsn.setText("");
            else txtMatricola.setText("");
        });
        txtSsn.setEnabled(false);

        btnSalva.addActionListener(e -> {
            String user = txtUsername.getText();
            String pass = new String(txtPassword.getPassword());
            String mail = txtEmail.getText();
            String nome = txtNome.getText();
            String cognome = txtCognome.getText();
            String ruolo = (String) cmbRuolo.getSelectedItem();
            String mat = txtMatricola.getText();
            String ssn = txtSsn.getText();

            if (user.isEmpty() || pass.isEmpty() || mail.isEmpty() || nome.isEmpty() || cognome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tutti i campi principali sono obbligatori!", "Errore validazione", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                // Utilizzo del DTO per risolvere il code smell dei troppi parametri
                DatiRegistrazione dati = new DatiRegistrazione(user, pass, mail, nome, cognome, ruolo, mat, ssn);
                controller.effettuaRegistrazione(dati);
                JOptionPane.showMessageDialog(this, "Registrazione completata!");
                new LoginFrame(controller).setVisible(true);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage(), "Errore DB", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnAnnulla.addActionListener(e -> {
            new LoginFrame(controller).setVisible(true);
            dispose();
        });
    }
}