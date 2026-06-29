package gui;

import controller.Controller;
import javax.swing.*;

public class StudenteFrame extends JFrame {

    private transient Controller controller;
    private JPanel panel1;

    // Campi Tesi e Seduta
    private JTextField txtTitoloTesi;
    private JTextField txtPercorsoFile;
    private JButton btnCaricaTesi;
    private JTextField txtCodiceSeduta;
    private JButton btnPrenotaSeduta;

    // Campi Tirocinio
    private JTextField txtIdTirocinio;
    private JTextField txtSsnDocente;
    private JButton btnRichiediTirocinio;

    // Tabelle e Bottoni extra
    private JTable tblTirocini;
    private JTable tblStatoRichieste;
    private JTable tblSedute; // LA TUA NUOVA TABELLA!
    private JButton btnHome;

    private static final String TITOLO_ERRORE = "Errore";
    private static final String PREFISSO_ERRORE_DB = "Errore DB: ";

    public StudenteFrame(Controller controller) {
        this.controller = controller;
        setContentPane(panel1);
        setTitle("Plancia Studente");
        setSize(1000, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // =================================================================
        // TASTO HOME
        // =================================================================
        if (btnHome != null) {
            btnHome.addActionListener(e -> {
                new LoginFrame(controller).setVisible(true);
                dispose();
            });
        }

        // =================================================================
        // AZIONE 1: RICHIESTA TIROCINIO
        // =================================================================
        btnRichiediTirocinio.addActionListener(e -> {
            try {
                String ssnRelatore = txtSsnDocente.getText();
                String idTirocinioStr = txtIdTirocinio.getText();

                if(ssnRelatore.isEmpty() || idTirocinioStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Seleziona un tirocinio dalla tabella prima di richiederlo!", TITOLO_ERRORE, JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int idTirocinio = Integer.parseInt(idTirocinioStr);
                controller.studenteRichiediTirocinio(ssnRelatore, idTirocinio);
                JOptionPane.showMessageDialog(this, "Richiesta di tirocinio inviata con successo!");
                aggiornaTabellaStatoRichieste();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "L'ID del tirocinio deve essere un numero!", "Errore Input", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, PREFISSO_ERRORE_DB + ex.getMessage(), TITOLO_ERRORE, JOptionPane.ERROR_MESSAGE);
            }
        });

        // =================================================================
        // AZIONE 2: CARICAMENTO TESI
        // =================================================================
        btnCaricaTesi.addActionListener(e -> {
            try {
                String titolo = txtTitoloTesi.getText();
                String percorso = txtPercorsoFile.getText();

                if(titolo.isEmpty() || percorso.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Inserisci titolo e percorso della tesi!", TITOLO_ERRORE, JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Il blocco per il tirocinio non approvato scatterà nel controller in caso di errore!
                controller.studenteCaricaTesi(titolo, percorso);
                JOptionPane.showMessageDialog(this, "Tesi caricata con successo! Ora seleziona una seduta e prenota.");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, PREFISSO_ERRORE_DB + ex.getMessage(), TITOLO_ERRORE, JOptionPane.ERROR_MESSAGE);
            }
        });

        // =================================================================
        // AZIONE 3: PRENOTAZIONE SEDUTA
        // =================================================================
        btnPrenotaSeduta.addActionListener(e -> {
            try {
                // Leggiamo SOLO il codice della seduta, il titolo tesi non serve più!
                String codiceSeduta = txtCodiceSeduta.getText();

                if(codiceSeduta.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Seleziona il codice della seduta dalla tabella in basso!", TITOLO_ERRORE, JOptionPane.WARNING_MESSAGE);
                } else {
                    controller.studentePrenotaSeduta(codiceSeduta);
                    JOptionPane.showMessageDialog(this, "Prenotazione alla seduta " + codiceSeduta + " effettuata!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, PREFISSO_ERRORE_DB + ex.getMessage(), TITOLO_ERRORE, JOptionPane.ERROR_MESSAGE);
            }
        });

        // =================================================================
        // POPOLAMENTO INIZIALE TABELLE
        // =================================================================
        aggiornaTabellaTirocini();
        aggiornaTabellaStatoRichieste();
        aggiornaTabellaSedute(); // Popola la nuova tabella!
    }

    private void aggiornaTabellaTirocini() {
        try {
            String[] colonne = {"ID Tirocinio", "Argomento", "Tipologia", "SSN Docente", "Relatore"};
            javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(colonne, 0) {
                @Override
                public boolean isCellEditable(int row, int column) { return false; }
            };

            for (String[] riga : controller.getElencoTirociniDisponibili()) {
                model.addRow(riga);
            }
            if(tblTirocini != null) {
                tblTirocini.setModel(model);
                for(java.awt.event.MouseListener ml : tblTirocini.getMouseListeners()) tblTirocini.removeMouseListener(ml);
                tblTirocini.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        int r = tblTirocini.getSelectedRow();
                        if (r != -1) {
                            if(txtIdTirocinio != null) txtIdTirocinio.setText(tblTirocini.getValueAt(r, 0).toString());
                            if(txtSsnDocente != null) txtSsnDocente.setText(tblTirocini.getValueAt(r, 3).toString());
                        }
                    }
                });
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void aggiornaTabellaStatoRichieste() {
        try {
            String[] colonne = {"Argomento", "Relatore", "Stato"};
            javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(colonne, 0) {
                @Override
                public boolean isCellEditable(int row, int column) { return false; }
            };
            for (String[] riga : controller.getStatoRichiesteStudente()) {
                model.addRow(riga);
            }
            if(tblStatoRichieste != null) tblStatoRichieste.setModel(model);
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    // =====================================================================
    // IL METODO PER LA TUA NUOVA TABELLA
    // =====================================================================
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

            if(tblSedute != null) {
                tblSedute.setModel(model);
                for(java.awt.event.MouseListener ml : tblSedute.getMouseListeners()) tblSedute.removeMouseListener(ml);

                // Click sulla riga -> autocompila il codice seduta!
                tblSedute.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        int r = tblSedute.getSelectedRow();
                        if (r != -1 && txtCodiceSeduta != null) {
                            txtCodiceSeduta.setText(tblSedute.getValueAt(r, 0).toString());
                        }
                    }
                });
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}