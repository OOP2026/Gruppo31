package gui;

import controller.Controller;
import javax.swing.*;

public class StudenteFrame extends JFrame {

    private transient Controller controller;

    private JPanel panel1;
    // Campi per la Tesi
    private JTextField txtTitoloTesi;
    private JTextField txtPercorsoFile;
    private JButton btnCaricaTesi;

    // NUOVI CAMPI: Aggiunti per raccogliere i dati del Tirocinio dal .form
    private JTextField txtSsnDocente;
    private JTextField txtIdTirocinio;
    private JButton btnRichiediTirocinio;

    // NUOVO CAMPO: Aggiunto per raccogliere il codice della Seduta dal .form
    private JTextField txtCodiceSeduta;
    private JButton btnPrenotaSeduta;

    public StudenteFrame(Controller controller) {
        this.controller = controller;
        setContentPane(panel1);
        setTitle("Plancia Studente");
        setSize(500, 450); // Ho aumentato un po' la dimensione per farci stare tutto

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ==========================================
        // AZIONE: Richiedi Tirocinio
        // ==========================================
        btnRichiediTirocinio.addActionListener(e -> {
            try {
                // Leggiamo i dati veri inseriti dall'utente!
                String ssnRelatore = txtSsnDocente.getText();
                int idTirocinio = Integer.parseInt(txtIdTirocinio.getText());

                controller.studenteRichiediTirocinio(ssnRelatore, idTirocinio);
                JOptionPane.showMessageDialog(StudenteFrame.this, "Richiesta di tirocinio inviata al docente!");

            } catch (NumberFormatException ex) {
                // Se lo studente scrive lettere al posto dell'ID (che è un numero), mostriamo errore
                JOptionPane.showMessageDialog(StudenteFrame.this, "Attenzione: l'ID del tirocinio deve essere un numero!", "Errore Input", JOptionPane.ERROR_MESSAGE);
            }
        });

        // ==========================================
        // AZIONE: Carica Tesi
        // ==========================================
        btnCaricaTesi.addActionListener(e -> {
            String titolo = txtTitoloTesi.getText();
            String percorso = txtPercorsoFile.getText();

            controller.studenteCaricaTesi(titolo, percorso);
            JOptionPane.showMessageDialog(StudenteFrame.this, "Tesi caricata con successo nel sistema!");
        });

        // ==========================================
        // AZIONE: Prenota Seduta
        // ==========================================
        btnPrenotaSeduta.addActionListener(e -> {
            String titoloTesi = txtTitoloTesi.getText(); // Usiamo il titolo scritto sopra
            String codiceSeduta = txtCodiceSeduta.getText(); // Leggiamo il codice seduta

            if(titoloTesi.isEmpty() || codiceSeduta.isEmpty()) {
                JOptionPane.showMessageDialog(StudenteFrame.this, "Compila il titolo della tesi e il codice della seduta!", "Errore", JOptionPane.WARNING_MESSAGE);
            } else {
                controller.studentePrenotaSeduta(titoloTesi, codiceSeduta);
                JOptionPane.showMessageDialog(StudenteFrame.this, "Prenotazione alla seduta effettuata!");
            }
        });
    }
}