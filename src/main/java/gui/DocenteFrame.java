package gui;

import controller.Controller;
import javax.swing.*;

public class DocenteFrame extends JFrame {

    private transient Controller controller;

    private JPanel panel1;
    private JTextField txtIdTirocinio;
    private JTextField txtArgomentoTirocinio;
    private JButton btnAggiungiTirocinio;

    // NUOVI CAMPI per la valutazione
    private JTextField txtMatricolaRichiesta;
    private JButton btnApprovaRichiesta;

    private JTextField txtMatricolaTesi;
    private JButton btnApprovaTesi;

    public DocenteFrame(Controller controller) {
        this.controller = controller;
        setContentPane(panel1);
        setTitle("GUI Docente / Relatore");
        setSize(500, 450); // Ingrandito leggermente per i nuovi campi

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ==========================================
        // AZIONE: Aggiungi Tirocinio
        // ==========================================
        btnAggiungiTirocinio.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtIdTirocinio.getText());
                String argomento = txtArgomentoTirocinio.getText();

                controller.docenteAggiungiTirocinio(id, argomento);
                JOptionPane.showMessageDialog(DocenteFrame.this, "Nuovo argomento di tirocinio aggiunto!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(DocenteFrame.this, "L'ID deve essere un numero!");
            }
        });

        // ==========================================
        // AZIONE: Valuta Richiesta
        // ==========================================
        btnApprovaRichiesta.addActionListener(e -> {
            String matricolaStudente = txtMatricolaRichiesta.getText();

            if(matricolaStudente.isEmpty()) {
                JOptionPane.showMessageDialog(DocenteFrame.this, "Inserisci la matricola dello studente!", "Errore", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // MODIFICA: Chiediamo dinamicamente se vuole approvare o rifiutare
            int scelta = JOptionPane.showConfirmDialog(DocenteFrame.this,
                    "Vuoi APPROVARE la richiesta dello studente " + matricolaStudente + "?\n(Scegli 'No' per rifiutarla)",
                    "Valutazione Richiesta", JOptionPane.YES_NO_CANCEL_OPTION);

            if (scelta == JOptionPane.YES_OPTION) {
                controller.docenteValutaRichiesta(matricolaStudente, true); // true = Approvata
                JOptionPane.showMessageDialog(DocenteFrame.this, "Richiesta approvata con successo!");
            } else if (scelta == JOptionPane.NO_OPTION) {
                controller.docenteValutaRichiesta(matricolaStudente, false); // false = Rifiutata
                JOptionPane.showMessageDialog(DocenteFrame.this, "Richiesta rifiutata.");
            }
        });

        // ==========================================
        // AZIONE: Valuta Tesi
        // ==========================================
        btnApprovaTesi.addActionListener(e -> {
            String matricolaStudente = txtMatricolaTesi.getText();

            if(matricolaStudente.isEmpty()) {
                JOptionPane.showMessageDialog(DocenteFrame.this, "Inserisci la matricola dello studente!", "Errore", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // MODIFICA: Scelta dinamica anche per la tesi
            int scelta = JOptionPane.showConfirmDialog(DocenteFrame.this,
                    "Vuoi APPROVARE la tesi dello studente " + matricolaStudente + "?\n(Scegli 'No' per rifiutarla)",
                    "Valutazione Tesi", JOptionPane.YES_NO_CANCEL_OPTION);

            if (scelta == JOptionPane.YES_OPTION) {
                controller.docenteValutaTesi(matricolaStudente, true);
                JOptionPane.showMessageDialog(DocenteFrame.this, "Elaborato finale approvato!");
            } else if (scelta == JOptionPane.NO_OPTION) {
                controller.docenteValutaTesi(matricolaStudente, false);
                JOptionPane.showMessageDialog(DocenteFrame.this, "Elaborato finale rifiutato!");
            }
        });
    }
}