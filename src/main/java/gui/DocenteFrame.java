package gui;

import controller.Controller;
import javax.swing.*;

/**
 * Schermata dedicata ai Docenti.
 * Da qui i prof possono proporre tirocini (interni o esterni) e valutare sia
 * le richieste di tirocinio degli studenti che le tesi finali caricate.
 */
public class DocenteFrame extends JFrame {

    private static final String ERRORE_DB_PREFIX = "Errore DB: ";
    private transient Controller controller;
    private JPanel panel1;

    private JTextField txtIdTirocinio;
    private JTextField txtArgomentoTirocinio;
    private JButton btnAggiungiTirocinio;
    private JCheckBox chkTirocinioEsterno;
    private JTextField txtAzienda;
    private JTextField txtReferenteAziendale;

    private JTextField txtMatricolaRichiesta;
    private JTextField txtIdTirocinioValutazione;
    private JButton btnApprovaRichiesta;
    private JTable tblRichiesteStudenti;
    private JTextField txtMatricolaTesi;
    private JButton btnApprovaTesi;
    private JTable tblTesiStudenti;

    private JButton btnHome;

    /**
     * Costruttore della plancia Docente.
     * Associa le azioni ai pulsanti e gestisce l'attivazione/disattivazione
     * dei campi per le aziende (se spunto la casella "esterno").
     *
     * @param controller Il Controller che gestisce i dati
     */
    public DocenteFrame(Controller controller) {
        this.controller = controller;
        setContentPane(panel1);
        setTitle("GUI Docente / Relatore");
        setSize(900, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inizializzaListenerAggiungiTirocinio();
        inizializzaListenerApprovaRichiesta();
        inizializzaListenerApprovaTesi();

        if (btnHome != null) {
            btnHome.addActionListener(e -> {
                new LoginFrame(controller).setVisible(true);
                dispose();
            });
        }

        // Se spunto la checkbox "Tirocinio Esterno", sblocco i campi Azienda e Referente[cite: 541].
        if (chkTirocinioEsterno != null) {
            chkTirocinioEsterno.addActionListener(e -> {
                boolean isEsterno = chkTirocinioEsterno.isSelected();
                txtAzienda.setEnabled(isEsterno);
                txtReferenteAziendale.setEnabled(isEsterno);
                if (!isEsterno) {
                    txtAzienda.setText("");
                    txtReferenteAziendale.setText("");
                }
            });
            txtAzienda.setEnabled(false);
            txtReferenteAziendale.setEnabled(false);
        }

        aggiornaTabellaRichieste();
        aggiornaTabellaTesi();
    }

    /**
     * Associa al bottone l'azione per inserire nel database l'argomento di tirocinio[cite: 540].
     * Distingue automaticamente tra tirocinio interno o esterno [cite: 541] in base alla checkbox.
     */
    private void inizializzaListenerAggiungiTirocinio() {
        btnAggiungiTirocinio.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtIdTirocinio.getText());
                String argomento = txtArgomentoTirocinio.getText();

                if (chkTirocinioEsterno != null && chkTirocinioEsterno.isSelected()) {
                    String azienda = txtAzienda.getText();
                    String referente = txtReferenteAziendale.getText();

                    if (azienda.isEmpty() || referente.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Per i tirocini esterni devi indicare Azienda e Referente!", "Dati Mancanti", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    controller.docenteAggiungiTirocinioEsterno(id, argomento, azienda, referente);
                    JOptionPane.showMessageDialog(this, "Nuovo tirocinio ESTERNO (Aziendale) aggiunto!");
                } else {
                    controller.docenteAggiungiTirocinio(id, argomento);
                    JOptionPane.showMessageDialog(this, "Nuovo argomento di tirocinio INTERNO aggiunto!");
                }

                txtIdTirocinio.setText("");
                txtArgomentoTirocinio.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "L'ID deve essere un numero!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ERRORE_DB_PREFIX + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * Associa l'azione al bottone di approvazione. Apre un popup che chiede
     * al professore se vuole ACCETTARE o RIFIUTARE la richiesta del ragazzo[cite: 544].
     */
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
                    aggiornaTabellaRichieste();
                    aggiornaTabellaTesi(); // Aggiorniamo anche le tesi perché magari ora si è sbloccato!
                } else if (scelta == JOptionPane.NO_OPTION) {
                    controller.docenteValutaRichiesta(matricola, idTirocinio, false);
                    JOptionPane.showMessageDialog(this, "Richiesta rifiutata.");
                    aggiornaTabellaRichieste();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "L'ID tirocinio deve essere un numero!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ERRORE_DB_PREFIX + ex.getMessage());
            }
        });
    }

    /**
     * Come sopra, ma serve per valutare l'elaborato finale (tesi).
     * Il professore può guardarla e approvarla (o rifiutarla)[cite: 549].
     */
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
                    aggiornaTabellaTesi();
                } else if (scelta == JOptionPane.NO_OPTION) {
                    controller.docenteValutaTesi(matricolaStudente, false);
                    JOptionPane.showMessageDialog(this, "Elaborato finale rifiutato!");
                    aggiornaTabellaTesi();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ERRORE_DB_PREFIX + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * Ripopola la tabella con le richieste in attesa mandate dagli studenti[cite: 544].
     */
    private void aggiornaTabellaRichieste() {
        try {
            String[] colonne = {"Matricola", "Studente", "ID Tirocinio", "Argomento", "Stato"};
            javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(colonne, 0) {
                @Override
                public boolean isCellEditable(int row, int column) { return false; }
            };

            for (String[] riga : controller.getRichiestePerDocente()) {
                model.addRow(riga);
            }

            if (tblRichiesteStudenti != null) {
                tblRichiesteStudenti.setModel(model);
                for(java.awt.event.MouseListener ml : tblRichiesteStudenti.getMouseListeners()) tblRichiesteStudenti.removeMouseListener(ml);
                tblRichiesteStudenti.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        int rigaSelezionata = tblRichiesteStudenti.getSelectedRow();
                        if (rigaSelezionata != -1) {
                            txtMatricolaRichiesta.setText(tblRichiesteStudenti.getValueAt(rigaSelezionata, 0).toString());
                            txtIdTirocinioValutazione.setText(tblRichiesteStudenti.getValueAt(rigaSelezionata, 2).toString());
                        }
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Ripopola la tabella delle tesi caricate e visibili al docente.
     */
    private void aggiornaTabellaTesi() {
        try {
            String[] colonne = {"Matricola", "Studente", "Titolo Tesi", "File PDF", "Stato"};
            javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(colonne, 0) {
                @Override
                public boolean isCellEditable(int row, int column) { return false; }
            };
            for (String[] riga : controller.getTesiPerDocente()) {
                model.addRow(riga);
            }

            if (tblTesiStudenti != null) {
                tblTesiStudenti.setModel(model);
                for(java.awt.event.MouseListener ml : tblTesiStudenti.getMouseListeners()) tblTesiStudenti.removeMouseListener(ml);
                tblTesiStudenti.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        int rigaSelezionata = tblTesiStudenti.getSelectedRow();
                        if (rigaSelezionata != -1) {
                            txtMatricolaTesi.setText(tblTesiStudenti.getValueAt(rigaSelezionata, 0).toString());
                        }
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}