package gui;

import controller.Controller;
import javax.swing.*;
import java.util.Date;

public class CoordinatoreFrame extends JFrame {

    private transient Controller controller;

    private JPanel panel1;
    private JTextField txtOraSeduta;
    private JTextField txtLuogoSeduta;
    private JTextField txtCodiceSeduta;
    private JButton btnCreaSeduta;

    // NUOVI CAMPI per Componi Commissione
    private JTextField txtSsnDocenteCommissione;
    private JTextField txtCodiceSedutaCommissione;
    private JButton btnComponiCommissione;

    public CoordinatoreFrame(Controller controller) {
        this.controller = controller;
        setContentPane(panel1);
        setTitle("Amministrazione - Coordinatore");
        setSize(500, 450); // Finestra leggermente ingrandita

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ==========================================
        // AZIONE: Crea Seduta
        // ==========================================
        btnCreaSeduta.addActionListener(e -> {
            String ora = txtOraSeduta.getText();
            String luogo = txtLuogoSeduta.getText();
            String codice = txtCodiceSeduta.getText();

            if (ora.isEmpty() || luogo.isEmpty() || codice.isEmpty()) {
                JOptionPane.showMessageDialog(CoordinatoreFrame.this, "Compila tutti i campi per creare la seduta!", "Errore", JOptionPane.WARNING_MESSAGE);
                return;
            }

            controller.coordinatoreInserisciSeduta(new Date(), ora, luogo, codice);
            JOptionPane.showMessageDialog(CoordinatoreFrame.this, "Seduta inserita nel calendario accademico!");
        });

        // ==========================================
        // AZIONE: Componi Commissione
        // ==========================================
        btnComponiCommissione.addActionListener(e -> {
            // Leggiamo i dati dinamici dalle nuove caselle di testo
            String ssnDocente = txtSsnDocenteCommissione.getText();
            String codiceSeduta = txtCodiceSedutaCommissione.getText();

            if (ssnDocente.isEmpty() || codiceSeduta.isEmpty()) {
                JOptionPane.showMessageDialog(CoordinatoreFrame.this, "Inserisci l'SSN del docente e il Codice della Seduta!", "Errore", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Passiamo le stringhe al Controller
            controller.coordinatoreAggiungiDocenteACommissione(ssnDocente, codiceSeduta);
            JOptionPane.showMessageDialog(CoordinatoreFrame.this, "Docente aggiunto ai membri della commissione!");
        });
    }
}