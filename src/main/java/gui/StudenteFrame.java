package gui;

import controller.Controller;
import javax.swing.*;

public class StudenteFrame extends JFrame {
    private transient Controller controller;
    private JPanel panel1;
    private JTextField txtTitoloTesi;
    private JTextField txtPercorsoFile;
    private JButton btnCaricaTesi;
    private JTextField txtSsnDocente;
    private JTextField txtIdTirocinio;
    private JButton btnRichiediTirocinio;
    private JTextField txtCodiceSeduta;
    private JButton btnPrenotaSeduta;

    // --- Definizione delle costanti per risolvere i warning di SonarLint ---
    private static final String TITOLO_ERRORE = "Errore";
    private static final String PREFISSO_ERRORE_DB = "Errore DB: ";

    public StudenteFrame(Controller controller) {
        this.controller = controller;
        setContentPane(panel1);
        setTitle("Plancia Studente");
        setSize(500, 450);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        btnRichiediTirocinio.addActionListener(e -> {
            try {
                String ssnRelatore = txtSsnDocente.getText();
                int idTirocinio = Integer.parseInt(txtIdTirocinio.getText());

                controller.studenteRichiediTirocinio(ssnRelatore, idTirocinio);
                JOptionPane.showMessageDialog(StudenteFrame.this, "Richiesta di tirocinio inviata!");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(StudenteFrame.this, "L'ID del tirocinio deve essere un numero!", "Errore Input", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(StudenteFrame.this, PREFISSO_ERRORE_DB + ex.getMessage(), TITOLO_ERRORE, JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCaricaTesi.addActionListener(e -> {
            try {
                String titolo = txtTitoloTesi.getText();
                String percorso = txtPercorsoFile.getText();

                controller.studenteCaricaTesi(titolo, percorso);
                JOptionPane.showMessageDialog(StudenteFrame.this, "Tesi caricata con successo!");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(StudenteFrame.this, PREFISSO_ERRORE_DB + ex.getMessage(), TITOLO_ERRORE, JOptionPane.ERROR_MESSAGE);
            }
        });

        btnPrenotaSeduta.addActionListener(e -> {
            try {
                String titoloTesi = txtTitoloTesi.getText();
                String codiceSeduta = txtCodiceSeduta.getText();

                if(titoloTesi.isEmpty() || codiceSeduta.isEmpty()) {
                    JOptionPane.showMessageDialog(StudenteFrame.this, "Compila il titolo e il codice della seduta!", TITOLO_ERRORE, JOptionPane.WARNING_MESSAGE);
                } else {
                    controller.studentePrenotaSeduta(titoloTesi, codiceSeduta);
                    JOptionPane.showMessageDialog(StudenteFrame.this, "Prenotazione effettuata!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(StudenteFrame.this, PREFISSO_ERRORE_DB + ex.getMessage(), TITOLO_ERRORE, JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}