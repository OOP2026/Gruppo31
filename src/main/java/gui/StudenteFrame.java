package gui;

import controller.Controller;
import javax.swing.*;

/**
 * Interfaccia grafica (View) dedicata allo Studente.
 * Questa finestra permette all'utente di interagire con il sistema per compiere
 * le operazioni fondamentali del suo percorso: richiedere un tirocinio,
 * caricare l'elaborato finale e prenotarsi per la seduta di laurea.
 * Segue il pattern architetturale mantenendo la GUI "leggera": non contiene logica di business,
 * ma delega tutte le operazioni al Controller.
 */
public class StudenteFrame extends JFrame {

    private transient Controller controller;
    private JPanel panel1;
    private JTextField txtTitoloTesi;
    private JTextField txtPercorsoFile;
    private JButton btnCaricaTesi;
    private JTextField txtSsnDocente;
    private JTextField txtIdTirocinio;
    private JButton btnRichiediTirocinio;
    private JTextField txtCodiceSeduta;
    private JButton btnPrenotaSeduta;
    private JTable tblTirocini;
    private JTable tblStatoRichieste;
    private JButton btnHome;

    // --- Definizione delle costanti per risolvere i warning di SonarLint ---
    // Centralizzare le stringhe ripetute è un'ottima pratica per la manutenibilità del codice
    private static final String TITOLO_ERRORE = "Errore";
    private static final String PREFISSO_ERRORE_DB = "Errore DB: ";

    /**
     * Costruttore della plancia Studente.
     * Inizializza i componenti grafici (generati dal form) e mappa i click dei bottoni
     * alle rispettive chiamate verso il Controller. Include la gestione delle eccezioni
     * per mostrare popup di errore user-friendly in caso di problemi.
     *
     * @param controller l'istanza del controller centrale che gestisce il flusso dell'applicazione
     */
    public StudenteFrame(Controller controller) {
        this.controller = controller;
        setContentPane(panel1);
        setTitle("Plancia Studente");
        setSize(500, 450);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la finestra sullo schermo

        // =================================================================
        // AZIONE 1: RICHIESTA DI TIROCINIO
        // =================================================================
        btnRichiediTirocinio.addActionListener(e -> {
            try {
                // Raccoglie i dati in input
                String ssnRelatore = txtSsnDocente.getText();
                int idTirocinio = Integer.parseInt(txtIdTirocinio.getText());

                // Delega al Controller l'operazione
                controller.studenteRichiediTirocinio(ssnRelatore, idTirocinio);
                JOptionPane.showMessageDialog(StudenteFrame.this, "Richiesta di tirocinio inviata!");

            } catch (NumberFormatException ex) {
                // Cattura l'errore se l'utente inserisce testo al posto di un numero nell'ID
                JOptionPane.showMessageDialog(StudenteFrame.this, "L'ID del tirocinio deve essere un numero!", "Errore Input", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                // Cattura eventuali errori provenienti dal Database (DAO) o dalla logica
                JOptionPane.showMessageDialog(StudenteFrame.this, PREFISSO_ERRORE_DB + ex.getMessage(), TITOLO_ERRORE, JOptionPane.ERROR_MESSAGE);
            }
        });

        // =================================================================
        // AZIONE 2: CARICAMENTO DELLA TESI
        // =================================================================
        btnCaricaTesi.addActionListener(e -> {
            try {
                String titolo = txtTitoloTesi.getText();
                String percorso = txtPercorsoFile.getText();

                controller.studenteCaricaTesi(titolo, percorso);
                JOptionPane.showMessageDialog(StudenteFrame.this, "Tesi caricata con successo!");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(StudenteFrame.this, PREFISSO_ERRORE_DB + ex.getMessage(), TITOLO_ERRORE, JOptionPane.ERROR_MESSAGE);
            }
        });

        // =================================================================
        // AZIONE 3: PRENOTAZIONE ALLA SEDUTA DI LAUREA
        // =================================================================
        btnPrenotaSeduta.addActionListener(e -> {
            try {
                String titoloTesi = txtTitoloTesi.getText();
                String codiceSeduta = txtCodiceSeduta.getText();

                // Controllo preliminare per evitare che lo studente invii campi vuoti
                if(titoloTesi.isEmpty() || codiceSeduta.isEmpty()) {
                    JOptionPane.showMessageDialog(StudenteFrame.this, "Compila il titolo e il codice della seduta!", TITOLO_ERRORE, JOptionPane.WARNING_MESSAGE);
                } else {
                    controller.studentePrenotaSeduta(titoloTesi, codiceSeduta);
                    JOptionPane.showMessageDialog(StudenteFrame.this, "Prenotazione effettuata!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(StudenteFrame.this, PREFISSO_ERRORE_DB + ex.getMessage(), TITOLO_ERRORE, JOptionPane.ERROR_MESSAGE);
            }
        });
        aggiornaTabellaTirocini();
        aggiornaTabellaStatoRichieste();
    }
    private void aggiornaTabellaTirocini() {
        try {
            // Aggiunte le due nuove colonne per il Docente
            String[] colonne = {"ID Tirocinio", "Argomento", "Tipologia", "SSN Docente", "Relatore"};
            javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(colonne, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Rende la tabella non modificabile
                }
            };

            for (String[] riga : controller.getElencoTirociniDisponibili()) {
                model.addRow(riga);
            }
            if(tblTirocini != null) {
                tblTirocini.setModel(model);

                // Rimuoviamo eventuali vecchi listener per evitare bug di click doppi
                for(java.awt.event.MouseListener ml : tblTirocini.getMouseListeners()) {
                    tblTirocini.removeMouseListener(ml);
                }

                // =========================================================
                // LA MAGIA: Autocompilazione al click sulla tabella
                // =========================================================
                tblTirocini.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        int rigaSelezionata = tblTirocini.getSelectedRow();
                        if (rigaSelezionata != -1) {
                            // Prende l'ID Tirocinio (Colonna 0) e lo mette nel campo
                            txtIdTirocinio.setText(tblTirocini.getValueAt(rigaSelezionata, 0).toString());
                            // Prende l'SSN del Docente (Colonna 3) e lo mette nel campo
                            txtSsnDocente.setText(tblTirocini.getValueAt(rigaSelezionata, 3).toString());
                        }
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void aggiornaTabellaStatoRichieste() {
        try {
            String[] colonne = {"Argomento", "Relatore", "Stato"};
            javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(colonne, 0);
            for (String[] riga : controller.getStatoRichiesteStudente()) {
                model.addRow(riga);
            }
            if (tblStatoRichieste != null) tblStatoRichieste.setModel(model);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // =================================================================
// TASTO HOME / LOGOUT
// =================================================================
        btnHome.addActionListener(e -> {
            // Riapre la schermata di login passando il controller
            new LoginFrame(controller).setVisible(true);
            // Chiude la schermata attuale
            dispose();
        });
    }

}