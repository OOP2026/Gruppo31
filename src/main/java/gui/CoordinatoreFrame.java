package gui;

import controller.Controller;
import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CoordinatoreFrame extends JFrame {
    private transient Controller controller;
    private JPanel panel1;

    private JTextField txtDataSeduta;
    private JTextField txtOraSeduta;
    private JTextField txtLuogoSeduta;
    private JTextField txtCodiceSeduta;
    private JButton btnCreaSeduta;

    private JTextField txtSsnDocenteCommissione;
    private JTextField txtCodiceSedutaCommissione;
    private JButton btnComponiCommissione;

    private JTextField txtRicercaSeduta;
    private JButton btnCercaStudenti;
    private JButton btnMostraCommissione;

    private JTable tblStudentiSeduta;
    private JTable tblCommissione;
    private JTable tblSedute;
    private JTable tblDocenti;

    private JButton btnHome;

    public CoordinatoreFrame(Controller controller) {
        this.controller = controller;
        setContentPane(panel1);
        setTitle("Amministrazione - Coordinatore");
        setSize(1000, 750);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        if(btnHome != null) {
            btnHome.addActionListener(e -> {
                new LoginFrame(controller).setVisible(true);
                dispose();
            });
        }

        btnCreaSeduta.addActionListener(e -> {
            try {
                String dataStr = txtDataSeduta.getText();
                String ora = txtOraSeduta.getText();
                String luogo = txtLuogoSeduta.getText();
                String codice = txtCodiceSeduta.getText();

                if (dataStr.isEmpty() || ora.isEmpty() || luogo.isEmpty() || codice.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Compila tutti i campi per creare la seduta!");
                    return;
                }

                // Utilizzo della nuova API java.time
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate dataFormattata = LocalDate.parse(dataStr, format);

                controller.coordinatoreInserisciSeduta(dataFormattata, ora, luogo, codice);
                JOptionPane.showMessageDialog(this, "Seduta inserita nel calendario accademico!");

                aggiornaTabellaSedute();

                txtDataSeduta.setText("");
                txtOraSeduta.setText("");
                txtLuogoSeduta.setText("");
                txtCodiceSeduta.setText("");

            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Formato data errato! Usa AAAA-MM-GG (es. 2026-07-20)", "Errore Data", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Errore DB: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnComponiCommissione.addActionListener(e -> {
            try {
                String ssnDocente = txtSsnDocenteCommissione.getText();
                String codiceSeduta = txtCodiceSedutaCommissione.getText();

                if (ssnDocente.isEmpty() || codiceSeduta.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Seleziona un Docente e una Seduta dalle tabelle!");
                    return;
                }

                controller.coordinatoreAggiungiDocenteACommissione(ssnDocente, codiceSeduta);
                JOptionPane.showMessageDialog(this, "Docente aggiunto alla commissione con successo!");

                aggiornaTabellaCommissione(codiceSeduta);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Impossibile aggiungere Docente", JOptionPane.WARNING_MESSAGE);
            }
        });

        if (btnCercaStudenti != null) {
            btnCercaStudenti.addActionListener(e -> {
                String codiceCercato = txtRicercaSeduta.getText();
                if (codiceCercato.isEmpty()) JOptionPane.showMessageDialog(this, "Inserisci il codice della seduta!");
                else aggiornaTabellaStudenti(codiceCercato);
            });
        }

        if (btnMostraCommissione != null) {
            btnMostraCommissione.addActionListener(e -> {
                String codiceCercato = txtRicercaSeduta.getText();
                if (codiceCercato.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Inserisci il codice della seduta prima di cercare!");
                } else {
                    aggiornaTabellaCommissione(codiceCercato);
                }
            });
        }

        aggiornaTabellaSedute();
        aggiornaTabellaDocenti();
    }

    private void aggiornaTabellaStudenti(String codiceSeduta) {
        try {
            String[] colonne = {"Matricola", "Nome", "Cognome", "Titolo Tesi"};
            javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(colonne, 0) {
                @Override
                public boolean isCellEditable(int row, int column) { return false; }
            };
            for (String[] riga : controller.getStudentiPerSeduta(codiceSeduta)) {
                model.addRow(riga);
            }
            if (tblStudentiSeduta != null) tblStudentiSeduta.setModel(model);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore caricamento: " + ex.getMessage());
        }
    }

    private void aggiornaTabellaCommissione(String codiceSeduta) {
        try {
            String[] colonne = {"SSN", "Nome", "Cognome"};
            javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(colonne, 0) {
                @Override
                public boolean isCellEditable(int row, int column) { return false; }
            };
            for (String[] riga : controller.getDocentiPerCommissione(codiceSeduta)) {
                model.addRow(riga);
            }
            if (tblCommissione != null) tblCommissione.setModel(model);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore caricamento: " + ex.getMessage());
        }
    }

    private void aggiornaTabellaSedute() {
        try {
            String[] colonne = {"Codice Seduta", "Data", "Ora", "Luogo"};
            javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(colonne, 0) {
                @Override
                public boolean isCellEditable(int row, int column) { return false; }
            };
            for (String[] riga : controller.getElencoSeduteDisponibili()) {
                model.addRow(riga);
            }
            if (tblSedute != null) {
                tblSedute.setModel(model);
                for(java.awt.event.MouseListener ml : tblSedute.getMouseListeners()) tblSedute.removeMouseListener(ml);

                tblSedute.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        int r = tblSedute.getSelectedRow();
                        if (r != -1) {
                            String cod = tblSedute.getValueAt(r, 0).toString();
                            if(txtCodiceSedutaCommissione != null) txtCodiceSedutaCommissione.setText(cod);
                            if(txtRicercaSeduta != null) txtRicercaSeduta.setText(cod);
                        }
                    }
                });
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void aggiornaTabellaDocenti() {
        try {
            String[] colonne = {"SSN", "Nome", "Cognome"};
            javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(colonne, 0) {
                @Override
                public boolean isCellEditable(int row, int column) { return false; }
            };
            for (String[] riga : controller.getTuttiDocenti()) {
                model.addRow(riga);
            }
            if (tblDocenti != null) {
                tblDocenti.setModel(model);
                for(java.awt.event.MouseListener ml : tblDocenti.getMouseListeners()) tblDocenti.removeMouseListener(ml);

                tblDocenti.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        int r = tblDocenti.getSelectedRow();
                        if (r != -1 && txtSsnDocenteCommissione != null) {
                            txtSsnDocenteCommissione.setText(tblDocenti.getValueAt(r, 0).toString());
                        }
                    }
                });
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}