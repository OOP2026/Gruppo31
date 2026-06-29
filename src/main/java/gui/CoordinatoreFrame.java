package gui;

import controller.Controller;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CoordinatoreFrame extends JFrame {

    // FIX SONAR: Costanti per evitare la duplicazione delle stringhe
    private static final String TITOLO_ERRORE = "Errore";
    private static final String PREFISSO_ERRORE_DB = "Errore DB: ";
    private static final String MSG_INSERISCI_CODICE = "Inserisci il codice della seduta!";
    private static final String COL_NOME = "Nome";
    private static final String COL_COGNOME = "Cognome";
    private static final String COL_SSN = "SSN";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

        // FIX SONAR: Spacchettiamo il costruttore delegando la logica a un metodo separato
        inizializzaAzioniBottoni();

        aggiornaTabellaSedute();
        aggiornaTabellaDocenti();
    }

    // --- METODI DI INIZIALIZZAZIONE (Riducono la Cognitive Complexity) ---

    private void inizializzaAzioniBottoni() {
        if(btnHome != null) {
            btnHome.addActionListener(e -> tornaAllaHome());
        }
        btnCreaSeduta.addActionListener(e -> gestisciCreazioneSeduta());
        btnComponiCommissione.addActionListener(e -> gestisciComposizioneCommissione());

        if (btnCercaStudenti != null) {
            btnCercaStudenti.addActionListener(e -> gestisciRicercaStudenti());
        }
        if (btnMostraCommissione != null) {
            btnMostraCommissione.addActionListener(e -> gestisciMostraCommissione());
        }
    }

    private void tornaAllaHome() {
        new LoginFrame(controller).setVisible(true);
        dispose();
    }

    private void gestisciCreazioneSeduta() {
        try {
            String dataStr = txtDataSeduta.getText();
            String ora = txtOraSeduta.getText();
            String luogo = txtLuogoSeduta.getText();
            String codice = txtCodiceSeduta.getText();

            if (dataStr.isEmpty() || ora.isEmpty() || luogo.isEmpty() || codice.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Compila tutti i campi per creare la seduta!");
                return;
            }

            LocalDate dataFormattata = LocalDate.parse(dataStr, DATE_FORMATTER);
            controller.coordinatoreInserisciSeduta(dataFormattata, ora, luogo, codice);
            JOptionPane.showMessageDialog(this, "Seduta inserita nel calendario accademico!");

            aggiornaTabellaSedute();
            svuotaCampiSeduta();

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato data errato! Usa AAAA-MM-GG", TITOLO_ERRORE, JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, PREFISSO_ERRORE_DB + ex.getMessage(), TITOLO_ERRORE, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void svuotaCampiSeduta() {
        txtDataSeduta.setText("");
        txtOraSeduta.setText("");
        txtLuogoSeduta.setText("");
        txtCodiceSeduta.setText("");
    }

    private void gestisciComposizioneCommissione() {
        try {
            String ssnDocente = txtSsnDocenteCommissione.getText();
            String codiceSeduta = txtCodiceSedutaCommissione.getText();

            if (ssnDocente.isEmpty() || codiceSeduta.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleziona un Docente e una Seduta dalle tabelle!");
                return;
            }

            controller.coordinatoreAggiungiDocenteACommissione(ssnDocente, codiceSeduta);
            JOptionPane.showMessageDialog(this, "Docente aggiunto alla commissione!");
            aggiornaTabellaCommissione(codiceSeduta);

        } catch (SQLException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Impossibile aggiungere Docente", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void gestisciRicercaStudenti() {
        String codiceCercato = txtRicercaSeduta.getText();
        if (codiceCercato.isEmpty()) JOptionPane.showMessageDialog(this, MSG_INSERISCI_CODICE);
        else aggiornaTabellaStudenti(codiceCercato);
    }

    private void gestisciMostraCommissione() {
        String codiceCercato = txtRicercaSeduta.getText();
        if (codiceCercato.isEmpty()) JOptionPane.showMessageDialog(this, MSG_INSERISCI_CODICE);
        else aggiornaTabellaCommissione(codiceCercato);
    }

    // --- METODI DELLE TABELLE ---

    private void aggiornaTabellaStudenti(String codiceSeduta) {
        try {
            String[] colonne = {"Matricola", COL_NOME, COL_COGNOME, "Titolo Tesi"};
            DefaultTableModel model = creaModelloTabella(colonne);
            for (String[] riga : controller.getStudentiPerSeduta(codiceSeduta)) model.addRow(riga);
            if (tblStudentiSeduta != null) tblStudentiSeduta.setModel(model);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, PREFISSO_ERRORE_DB + ex.getMessage(), TITOLO_ERRORE, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aggiornaTabellaCommissione(String codiceSeduta) {
        try {
            String[] colonne = {COL_SSN, COL_NOME, COL_COGNOME};
            DefaultTableModel model = creaModelloTabella(colonne);
            for (String[] riga : controller.getDocentiPerCommissione(codiceSeduta)) model.addRow(riga);
            if (tblCommissione != null) tblCommissione.setModel(model);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, PREFISSO_ERRORE_DB + ex.getMessage(), TITOLO_ERRORE, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aggiornaTabellaSedute() {
        try {
            String[] colonne = {"Codice Seduta", "Data", "Ora", "Luogo"};
            DefaultTableModel model = creaModelloTabella(colonne);
            for (String[] riga : controller.getElencoSeduteDisponibili()) model.addRow(riga);
            if (tblSedute != null) {
                tblSedute.setModel(model);
                aggiungiListenerTabellaSedute(); // Spacchettato per ridurre la complessità
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, PREFISSO_ERRORE_DB + ex.getMessage(), TITOLO_ERRORE, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aggiornaTabellaDocenti() {
        try {
            String[] colonne = {COL_SSN, COL_NOME, COL_COGNOME};
            DefaultTableModel model = creaModelloTabella(colonne);
            for (String[] riga : controller.getTuttiDocenti()) model.addRow(riga);
            if (tblDocenti != null) {
                tblDocenti.setModel(model);
                aggiungiListenerTabellaDocenti(); // Spacchettato per ridurre la complessità
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, PREFISSO_ERRORE_DB + ex.getMessage(), TITOLO_ERRORE, JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- HELPER METHODS PER LE TABELLE ---

    private DefaultTableModel creaModelloTabella(String[] colonne) {
        return new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
    }

    private void aggiungiListenerTabellaSedute() {
        for(MouseListener ml : tblSedute.getMouseListeners()) tblSedute.removeMouseListener(ml);
        tblSedute.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int r = tblSedute.getSelectedRow();
                if (r != -1) {
                    String cod = tblSedute.getValueAt(r, 0).toString();
                    if(txtCodiceSedutaCommissione != null) txtCodiceSedutaCommissione.setText(cod);
                    if(txtRicercaSeduta != null) txtRicercaSeduta.setText(cod);
                }
            }
        });
    }

    private void aggiungiListenerTabellaDocenti() {
        for(MouseListener ml : tblDocenti.getMouseListeners()) tblDocenti.removeMouseListener(ml);
        tblDocenti.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int r = tblDocenti.getSelectedRow();
                if (r != -1 && txtSsnDocenteCommissione != null) {
                    txtSsnDocenteCommissione.setText(tblDocenti.getValueAt(r, 0).toString());
                }
            }
        });
    }
}