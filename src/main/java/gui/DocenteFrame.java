package gui;

import controller.Controller;
import javax.swing.*;

public class DocenteFrame extends JFrame {

    private static final String ERRORE_DB_PREFIX = "Errore DB: ";
    private transient Controller controller;
    private JPanel panel1;
    private JTextField txtIdTirocinio;
    private JTextField txtArgomentoTirocinio;
    private JButton btnAggiungiTirocinio;
    private JTextField txtMatricolaRichiesta;
    private JButton btnApprovaRichiesta;
    private JTextField txtMatricolaTesi;
    private JButton btnApprovaTesi;
    private JTextField txtIdTirocinioValutazione;

    public DocenteFrame(Controller controller) {
        this.controller = controller;
        setContentPane(panel1);
        setTitle("GUI Docente / Relatore");
        setSize(500, 450);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inizializzaListenerAggiungiTirocinio();
        inizializzaListenerApprovaRichiesta();
        inizializzaListenerApprovaTesi();
    }

    private void inizializzaListenerAggiungiTirocinio() {
        btnAggiungiTirocinio.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtIdTirocinio.getText());
                String argomento = txtArgomentoTirocinio.getText();
                controller.docenteAggiungiTirocinio(id, argomento);
                JOptionPane.showMessageDialog(this, "Nuovo argomento di tirocinio aggiunto!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "L'ID deve essere un numero!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ERRORE_DB_PREFIX + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void inizializzaListenerApprovaRichiesta() {
        btnApprovaRichiesta.addActionListener(e -> {
            String matricola = txtMatricolaRichiesta.getText();
            String idTirocinioStr = txtIdTirocinioValutazione.getText();

            if (matricola.isEmpty() || idTirocinioStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Inserisci sia la matricola che l'ID tirocinio!");
                return;
            }

            try {
                int idTirocinio = Integer.parseInt(idTirocinioStr);
                int scelta = JOptionPane.showConfirmDialog(this,
                        "Approvare la richiesta per la matricola " + matricola + " (ID: " + idTirocinio + ")?",
                        "Valutazione", JOptionPane.YES_NO_CANCEL_OPTION);

                if (scelta == JOptionPane.YES_OPTION) {
                    controller.docenteValutaRichiesta(matricola, idTirocinio, true);
                    JOptionPane.showMessageDialog(this, "Richiesta approvata!");
                } else if (scelta == JOptionPane.NO_OPTION) {
                    controller.docenteValutaRichiesta(matricola, idTirocinio, false);
                    JOptionPane.showMessageDialog(this, "Richiesta rifiutata.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "L'ID tirocinio deve essere un numero!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ERRORE_DB_PREFIX + ex.getMessage());
            }
        });
    }

    private void inizializzaListenerApprovaTesi() {
        btnApprovaTesi.addActionListener(e -> {
            String matricolaStudente = txtMatricolaTesi.getText();
            if(matricolaStudente.isEmpty()) return;

            int scelta = JOptionPane.showConfirmDialog(this,
                    "Vuoi APPROVARE la tesi dello studente " + matricolaStudente + "?",
                    "Valutazione Tesi", JOptionPane.YES_NO_CANCEL_OPTION);

            try {
                if (scelta == JOptionPane.YES_OPTION) {
                    controller.docenteValutaTesi(matricolaStudente, true);
                    JOptionPane.showMessageDialog(this, "Elaborato finale approvato!");
                } else if (scelta == JOptionPane.NO_OPTION) {
                    controller.docenteValutaTesi(matricolaStudente, false);
                    JOptionPane.showMessageDialog(this, "Elaborato finale rifiutato!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ERRORE_DB_PREFIX + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}