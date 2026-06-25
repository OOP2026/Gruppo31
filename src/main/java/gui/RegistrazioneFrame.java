package gui;

import controller.Controller;
import javax.swing.*;

public class RegistrazioneFrame extends JFrame {

    private transient Controller controller;

    // Queste variabili sono collegate in automatico al tuo file .form dall'IDE
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

        // Se non hai aggiunto i ruoli nel designer grafico, li inseriamo qui:
        if(cmbRuolo.getItemCount() == 0) {
            cmbRuolo.addItem("Studente");
            cmbRuolo.addItem("Docente");
        }

        // =================================================================
        // LOGICA DI DINAMICITÀ DEI CAMPI
        // =================================================================
        cmbRuolo.addActionListener(e -> {
            boolean isStudente = "Studente".equals(cmbRuolo.getSelectedItem());
            txtMatricola.setEnabled(isStudente);
            txtSsn.setEnabled(!isStudente);
            if (isStudente) txtSsn.setText("");
            else txtMatricola.setText("");
        });

        // Di default parte su Studente, quindi disattiviamo SSN all'avvio
        txtSsn.setEnabled(false);

        // =================================================================
        // AZIONE DI SALVATAGGIO
        // =================================================================
        btnSalva.addActionListener(e -> {
            String user = txtUsername.getText();
            String pass = new String(txtPassword.getPassword());
            String mail = txtEmail.getText();
            String nome = txtNome.getText();
            String cognome = txtCognome.getText();
            String ruolo = (String) cmbRuolo.getSelectedItem();
            String mat = txtMatricola.getText();
            String ssn = txtSsn.getText();

            // Validazione campi obbligatori
            if (user.isEmpty() || pass.isEmpty() || mail.isEmpty() || nome.isEmpty() || cognome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tutti i campi principali sono obbligatori!", "Errore validazione", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                controller.effettuaRegistrazione(user, pass, mail, nome, cognome, ruolo, mat, ssn);
                JOptionPane.showMessageDialog(this, "Registrazione completata con successo!");

                new LoginFrame(controller).setVisible(true);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Errore durante la registrazione: " + ex.getMessage(), "Errore DB", JOptionPane.ERROR_MESSAGE);
            }
        });

        // =================================================================
        // AZIONE ANNULLA
        // =================================================================
        btnAnnulla.addActionListener(e -> {
            new LoginFrame(controller).setVisible(true);
            dispose();
        });

    }
}