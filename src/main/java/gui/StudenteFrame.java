package gui;

import controller.Controller;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * La schermata principale (Plancia) che vede lo studente loggato.
 * Permette di iscriversi a un tirocinio [cite: 543], vedere come procede la richiesta [cite: 546]
 * e, a fine percorso, inviare il file della tesi [cite: 548] e prenotarsi per la laurea[cite: 553].
 */
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

    /**
     * Costruttore della plancia Studente. Carica i dati delle tabelle appena si apre.
     *
     * @param controller Il controller per comunicare col DB.
     */
    public StudenteFrame(Controller controller) {
        this.controller = controller;
        setContentPane(panel1);
        setTitle("Plancia Studente");
        setSize(1000, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inizializzaAzioniBottoni();
        aggiornaTabellaTirocini();
        aggiornaTabellaStatoRichieste();
        aggiornaTabellaSedute();
    }

    /**
     * Inizializza i bottoni collegandoli alle funzioni giuste.
     */
    private void inizializzaAzioniBottoni() {
        if (btnHome != null) {
            btnHome.addActionListener(e -> tornaAllaHome());
        }
        btnRichiediTirocinio.addActionListener(e -> gestisciRichiestaTirocinio());
        btnCaricaTesi.addActionListener(e -> gestisciCaricamentoTesi());
        btnPrenotaSeduta.addActionListener(e -> gestisciPrenotazioneSeduta());
    }

    /**
     * Permette il logout riportando lo studente alla schermata principale di accesso.
     */
    private void tornaAllaHome() {
        new LoginFrame(controller).setVisible(true);
        dispose();
    }

    /**
     * Lo studente preleva l'ID del tirocinio desiderato e l'SSN del professore
     * scelto dalla tabella e invia la domanda[cite: 543].
     */
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

            // Aggiorniamo subito l'altra tabella per fargli vedere lo stato [cite: 546]
            aggiornaTabellaStatoRichieste();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "L'ID del tirocinio deve essere un numero!", "Errore Input", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, PREFISSO_ERRORE_DB + ex.getMessage(), TITOLO_ERRORE, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Lo studente, se ha finito il tirocinio, carica il PDF (o percorso) della sua tesi[cite: 548].
     * Il Controller dietro le quinte bloccherà tutto se il tirocinio non è approvato.
     */
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
            // Qui stamperà l'errore del Controller se la tesi è rifiutata o pendente
            JOptionPane.showMessageDialog(this, ex.getMessage(), TITOLO_ERRORE, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Lo studente sceglie in quale data (Seduta) vuole laurearsi e si prenota[cite: 553].
     */
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

    /**
     * Funzione helper per non far modificare le celle delle tabelle all'utente.
     *
     * @param colonne array dei nomi delle colonne
     * @return il modello DefaultTableModel base
     */
    private DefaultTableModel creaModelloTabella(String[] colonne) {
        return new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
    }

    /**
     * Riempe la tabella con tutti i tirocini offerti dall'università[cite: 543].
     */
    private void aggiornaTabellaTirocini() {
        try {
            String[] colonne = {"ID Tirocinio", "Argomento", "Tipologia", "SSN Docente", "Relatore"};
            DefaultTableModel model = creaModelloTabella(colonne);

            for (String[] riga : controller.getElencoTirociniDisponibili()) model.addRow(riga);
            if(tblTirocini != null) {
                tblTirocini.setModel(model);
                aggiungiListenerTabellaTirocini();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, PREFISSO_ERRORE_DB + ex.getMessage(), TITOLO_ERRORE, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Aggiunge l'evento di click sulla tabella tirocini per far prelevare automaticamente
     * SSN docente e ID tirocinio senza farli digitare a mano.
     */
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

    /**
     * Riempe la tabella che mostra allo studente se la sua richiesta
     * è stata ACCETTATA, RIFIUTATA o è IN_ATTESA[cite: 546].
     */
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

    /**
     * Riempe la tabella con tutte le prossime sedute di laurea a cui lo studente può iscriversi[cite: 553].
     */
    private void aggiornaTabellaSedute() {
        try {
            String[] colonne = {"Codice Seduta", "Data", "Ora", "Luogo"};
            DefaultTableModel model = creaModelloTabella(colonne);
            for (String[] riga : controller.getElencoSeduteDisponibili()) model.addRow(riga);
            if(tblSedute != null) {
                tblSedute.setModel(model);
                aggiungiListenerTabellaSedute();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, PREFISSO_ERRORE_DB + ex.getMessage(), TITOLO_ERRORE, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Evento al click sulla tabella sedute: mette il codice nel textfield automaticamente.
     */
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