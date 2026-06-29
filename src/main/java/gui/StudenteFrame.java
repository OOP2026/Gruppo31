package gui;

import controller.Controller;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class StudenteFrame extends JFrame {

    private transient Controller controller;
    private JPanel panel1;
    private JTextField txtTitoloTesi;
    private JTextField txtPercorsoFile;
    private JButton btnCaricaTesi;
    private JTextField txtCodiceSeduta;
    private JButton btnPrenotaSeduta;
    private JTextField txtIdTirocinio;
    private JTextField txtSsnDocente;
    private JButton btnRichiediTirocinio;
    private JTable tblTirocini;
    private JTable tblStatoRichieste;
    private JTable tblSedute;
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

        // FIX SONAR: Spacchettiamo il costruttore delegando la logica a metodi separati
        inizializzaAzioniBottoni();

        aggiornaTabellaTirocini();
        aggiornaTabellaStatoRichieste();
        aggiornaTabellaSedute();
    }

    // --- METODI DI INIZIALIZZAZIONE ---

    private void inizializzaAzioniBottoni() {
        if (btnHome != null) {
            btnHome.addActionListener(e -> tornaAllaHome());
        }
        btnRichiediTirocinio.addActionListener(e -> gestisciRichiestaTirocinio());
        btnCaricaTesi.addActionListener(e -> gestisciCaricamentoTesi());
        btnPrenotaSeduta.addActionListener(e -> gestisciPrenotazioneSeduta());
    }

    private void tornaAllaHome() {
        new LoginFrame(controller).setVisible(true);
        dispose();
    }

    private void gestisciRichiestaTirocinio() {
        try {
            String ssnRelatore = txtSsnDocente.getText();
            String idTirocinioStr = txtIdTirocinio.getText();

            if(ssnRelatore.isEmpty() || idTirocinioStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleziona un tirocinio dalla tabella!", TITOLO_ERRORE, JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idTirocinio = Integer.parseInt(idTirocinioStr);
            controller.studenteRichiediTirocinio(ssnRelatore, idTirocinio);
            JOptionPane.showMessageDialog(this, "Richiesta di tirocinio inviata con successo!");
            aggiornaTabellaStatoRichieste();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "L'ID del tirocinio deve essere un numero!", "Errore Input", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, PREFISSO_ERRORE_DB + ex.getMessage(), TITOLO_ERRORE, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void gestisciCaricamentoTesi() {
        try {
            String titolo = txtTitoloTesi.getText();
            String percorso = txtPercorsoFile.getText();

            if(titolo.isEmpty() || percorso.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Inserisci titolo e percorso della tesi!", TITOLO_ERRORE, JOptionPane.WARNING_MESSAGE);
                return;
            }

            controller.studenteCaricaTesi(titolo, percorso);
            JOptionPane.showMessageDialog(this, "Tesi caricata con successo! Ora seleziona una seduta e prenota.");

        } catch (SQLException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), TITOLO_ERRORE, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void gestisciPrenotazioneSeduta() {
        try {
            String codiceSeduta = txtCodiceSeduta.getText();
            if(codiceSeduta.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleziona il codice della seduta!", TITOLO_ERRORE, JOptionPane.WARNING_MESSAGE);
            } else {
                controller.studentePrenotaSeduta(codiceSeduta);
                JOptionPane.showMessageDialog(this, "Prenotazione alla seduta " + codiceSeduta + " effettuata!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, PREFISSO_ERRORE_DB + ex.getMessage(), TITOLO_ERRORE, JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- METODI DELLE TABELLE ---

    private DefaultTableModel creaModelloTabella(String[] colonne) {
        return new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
    }

    private void aggiornaTabellaTirocini() {
        try {
            String[] colonne = {"ID Tirocinio", "Argomento", "Tipologia", "SSN Docente", "Relatore"};
            DefaultTableModel model = creaModelloTabella(colonne);

            for (String[] riga : controller.getElencoTirociniDisponibili()) model.addRow(riga);

            if(tblTirocini != null) {
                tblTirocini.setModel(model);
                aggiungiListenerTabellaTirocini(); // Spacchettato per ridurre la complessità
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, PREFISSO_ERRORE_DB + ex.getMessage(), TITOLO_ERRORE, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aggiungiListenerTabellaTirocini() {
        for(MouseListener ml : tblTirocini.getMouseListeners()) tblTirocini.removeMouseListener(ml);
        tblTirocini.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int r = tblTirocini.getSelectedRow();
                if (r != -1) {
                    if(txtIdTirocinio != null) txtIdTirocinio.setText(tblTirocini.getValueAt(r, 0).toString());
                    if(txtSsnDocente != null) txtSsnDocente.setText(tblTirocini.getValueAt(r, 3).toString());
                }
            }
        });
    }

    private void aggiornaTabellaStatoRichieste() {
        try {
            String[] colonne = {"Argomento", "Relatore", "Stato"};
            DefaultTableModel model = creaModelloTabella(colonne);
            for (String[] riga : controller.getStatoRichiesteStudente()) model.addRow(riga);
            if(tblStatoRichieste != null) tblStatoRichieste.setModel(model);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, PREFISSO_ERRORE_DB + ex.getMessage(), TITOLO_ERRORE, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aggiornaTabellaSedute() {
        try {
            String[] colonne = {"Codice Seduta", "Data", "Ora", "Luogo"};
            DefaultTableModel model = creaModelloTabella(colonne);
            for (String[] riga : controller.getElencoSeduteDisponibili()) model.addRow(riga);
            if(tblSedute != null) {
                tblSedute.setModel(model);
                aggiungiListenerTabellaSedute(); // Spacchettato per ridurre la complessità
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, PREFISSO_ERRORE_DB + ex.getMessage(), TITOLO_ERRORE, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aggiungiListenerTabellaSedute() {
        for(MouseListener ml : tblSedute.getMouseListeners()) tblSedute.removeMouseListener(ml);
        tblSedute.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int r = tblSedute.getSelectedRow();
                if (r != -1 && txtCodiceSeduta != null) txtCodiceSeduta.setText(tblSedute.getValueAt(r, 0).toString());
            }
        });
    }
}