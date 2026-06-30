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

/**
 * Questa è l'interfaccia dedicata al Coordinatore del corso di laurea.
 * Da qui può inserire le nuove sedute [cite: 552] e formare le commissioni
 * controllando che i docenti abbiano i requisiti giusti[cite: 554].
 */
public class CoordinatoreFrame extends JFrame {

    // Costanti per evitare la duplicazione delle stringhe (SonarQube ringrazia)
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

    /**
     * Costruttore della schermata del Coordinatore.
     * Prepara la finestra, collega i bottoni e carica subito i dati nelle tabelle.
     *
     * @param controller Il "motore" che passa i dati dal DB all'interfaccia
     */
    public CoordinatoreFrame(Controller controller) {
        this.controller = controller;
        setContentPane(panel1);
        setTitle("Amministrazione - Coordinatore");
        setSize(1000, 750);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Chiamiamo metodi separati per non avere un costruttore kilometrico
        inizializzaAzioniBottoni();
        aggiornaTabellaSedute();
        aggiornaTabellaDocenti();
    }

    /**
     * Associa ad ogni bottone della UI l'azione corrispondente.
     * Invece di scrivere mille righe qui, richiamiamo le funzioni specifiche.
     */
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

    /**
     * Chiude la finestra corrente e ci riporta alla schermata di Login.
     */
    private void tornaAllaHome() {
        new LoginFrame(controller).setVisible(true);
        dispose();
    }

    /**
     * Raccoglie i dati dai textfield e chiede al controller di creare una nuova seduta di laurea.
     * Come richiede la traccia, ogni seduta ha una data, un'ora e un luogo[cite: 552].
     */
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

    /**
     * Svuota le caselle di testo dopo aver creato una seduta.
     * Serve solo a mantenere l'interfaccia pulita.
     */
    private void svuotaCampiSeduta() {
        txtDataSeduta.setText("");
        txtOraSeduta.setText("");
        txtLuogoSeduta.setText("");
        txtCodiceSeduta.setText("");
    }

    /**
     * Aggiunge un docente selezionato alla commissione di una determinata seduta.
     * Il Controller si occuperà di verificare se il prof ha i requisiti[cite: 554].
     */
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

    /**
     * Cerca tutti gli studenti assegnati a una seduta e aggiorna la tabella.
     */
    private void gestisciRicercaStudenti() {
        String codiceCercato = txtRicercaSeduta.getText();
        if (codiceCercato.isEmpty()) JOptionPane.showMessageDialog(this, MSG_INSERISCI_CODICE);
        else aggiornaTabellaStudenti(codiceCercato);
    }

    /**
     * Cerca i docenti che fanno parte della commissione di una determinata seduta.
     */
    private void gestisciMostraCommissione() {
        String codiceCercato = txtRicercaSeduta.getText();
        if (codiceCercato.isEmpty()) JOptionPane.showMessageDialog(this, MSG_INSERISCI_CODICE);
        else aggiornaTabellaCommissione(codiceCercato);
    }

    /**
     * Ricrea le righe della tabella degli studenti per la seduta indicata.
     *
     * @param codiceSeduta Il codice della seduta da cercare
     */
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

    /**
     * Ricrea le righe della tabella della commissione.
     *
     * @param codiceSeduta Il codice della seduta di cui vogliamo vedere la commissione
     */
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

    /**
     * Ricarica l'elenco di tutte le sedute disponibili nel sistema.
     */
    private void aggiornaTabellaSedute() {
        try {
            String[] colonne = {"Codice Seduta", "Data", "Ora", "Luogo"};
            DefaultTableModel model = creaModelloTabella(colonne);
            for (String[] riga : controller.getElencoSeduteDisponibili()) model.addRow(riga);
            if (tblSedute != null) {
                tblSedute.setModel(model);
                aggiungiListenerTabellaSedute();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, PREFISSO_ERRORE_DB + ex.getMessage(), TITOLO_ERRORE, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Ricarica l'elenco di tutti i docenti e coordinatori disponibili.
     */
    private void aggiornaTabellaDocenti() {
        try {
            String[] colonne = {COL_SSN, COL_NOME, COL_COGNOME};
            DefaultTableModel model = creaModelloTabella(colonne);
            for (String[] riga : controller.getTuttiDocenti()) model.addRow(riga);
            if (tblDocenti != null) {
                tblDocenti.setModel(model);
                aggiungiListenerTabellaDocenti();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, PREFISSO_ERRORE_DB + ex.getMessage(), TITOLO_ERRORE, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Utility per creare un modello di tabella che non permetta all'utente
     * di modificare le celle facendo doppio click (isCellEditable = false).
     *
     * @param colonne L'array di stringhe con le intestazioni delle colonne
     * @return Il modello pronto per essere assegnato alla JTable
     */
    private DefaultTableModel creaModelloTabella(String[] colonne) {
        return new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
    }

    /**
     * Aggiunge l'evento di click alla tabella delle sedute.
     * Se ci clicchi, riempie in automatico le caselline di testo col codice della seduta.
     */
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

    /**
     * Aggiunge l'evento di click alla tabella dei docenti.
     * Se ci clicchi, preleva l'SSN del professore e lo mette nel form per la commissione.
     */
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