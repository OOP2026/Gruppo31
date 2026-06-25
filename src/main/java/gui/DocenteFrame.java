package gui;

import controller.Controller;
import javax.swing.*;

/**
 * Interfaccia grafica (View) dedicata al Docente.
 * Mette a disposizione gli strumenti necessari per la gestione didattica:
 * la pubblicazione di nuove offerte di tirocinio e la valutazione (approvazione o rifiuto)
 * delle richieste di tirocinio e degli elaborati di tesi degli studenti.
 */
public class DocenteFrame extends JFrame {

    // Costante per uniformare i messaggi di errore legati al database
    private static final String ERRORE_DB_PREFIX = "Errore DB: ";

    private transient Controller controller;
    private JPanel panel1;

    // Componenti per l'aggiunta di un nuovo tirocinio
    private JTextField txtIdTirocinio;
    private JTextField txtArgomentoTirocinio;
    private JButton btnAggiungiTirocinio;

    // Componenti per la valutazione di una richiesta di tirocinio
    private JTextField txtMatricolaRichiesta;
    private JButton btnApprovaRichiesta;
    private JTextField txtIdTirocinioValutazione;

    // Componenti per la valutazione della tesi
    private JTextField txtMatricolaTesi;
    private JButton btnApprovaTesi;
    private JCheckBox chkTirocinioEsterno;
    private JTextField txtAzienda;
    private JTextField txtReferenteAziendale;
    private JTable tblRichiesteStudenti;
    private JButton btnHome;

    /**
     * Costruttore della plancia Docente.
     * Imposta le dimensioni e la posizione della finestra, e richiama dei metodi
     * "helper" (aiutanti) per inizializzare le azioni dei bottoni in modo ordinato.
     *
     * @param controller l'istanza del controller centrale
     */
    public DocenteFrame(Controller controller) {
        this.controller = controller;
        setContentPane(panel1);
        setTitle("GUI Docente / Relatore");
        setSize(500, 450);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la finestra

        // Separare l'inizializzazione dei bottoni mantiene il costruttore pulito
        inizializzaListenerAggiungiTirocinio();
        inizializzaListenerApprovaRichiesta();
        inizializzaListenerApprovaTesi();
    }

    /**
     * Gestisce l'evento di click sul bottone "Aggiungi Tirocinio".
     * Raccoglie l'ID e l'argomento e delega al controller il salvataggio.
     */
    private void inizializzaListenerAggiungiTirocinio() {
        btnAggiungiTirocinio.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtIdTirocinio.getText());
                String argomento = txtArgomentoTirocinio.getText();

                // Controlla se la spunta per il tirocinio esterno è stata cliccata
                if (chkTirocinioEsterno != null && chkTirocinioEsterno.isSelected()) {
                    String azienda = txtAzienda.getText();
                    String referente = txtReferenteAziendale.getText();

                    if (azienda.isEmpty() || referente.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Per i tirocini esterni devi indicare Azienda e Referente!", "Dati Mancanti", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // Chiama la nuova funzione del controller (ricordati di averla incollata in Controller.java)
                    controller.docenteAggiungiTirocinioEsterno(id, argomento, azienda, referente);
                    JOptionPane.showMessageDialog(this, "Nuovo tirocinio ESTERNO (Aziendale) aggiunto!");
                } else {
                    // Inserimento classico
                    controller.docenteAggiungiTirocinio(id, argomento);
                    JOptionPane.showMessageDialog(this, "Nuovo argomento di tirocinio INTERNO aggiunto!");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "L'ID deve essere un numero!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ERRORE_DB_PREFIX + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * Gestisce l'evento di click sul bottone per la valutazione delle richieste di tirocinio.
     * Utilizza un pannello di conferma (ConfirmDialog) per chiedere al professore
     * se intende approvare o rifiutare la candidatura dello studente.
     */
    private void inizializzaListenerApprovaRichiesta() {
        btnApprovaRichiesta.addActionListener(e -> {
            String matricola = txtMatricolaRichiesta.getText();
            String idTirocinioStr = txtIdTirocinioValutazione.getText();

            // Controllo campi vuoti
            if (matricola.isEmpty() || idTirocinioStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Inserisci sia la matricola che l'ID tirocinio!");
                return; // Interrompe l'esecuzione se mancano dati
            }

            try {
                int idTirocinio = Integer.parseInt(idTirocinioStr);

                // Mostra un popup con i tasti "Sì", "No", "Annulla"
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
                // Se preme "Annulla" o chiude la finestra, non fa nulla

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "L'ID tirocinio deve essere un numero!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ERRORE_DB_PREFIX + ex.getMessage());
            }
        });
    }

    /**
     * Gestisce l'evento di click sul bottone per la valutazione finale della tesi.
     * Simile al tirocinio, converte la scelta del docente (Sì/No) in un valore booleano
     * da passare al Controller.
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
                } else if (scelta == JOptionPane.NO_OPTION) {
                    controller.docenteValutaTesi(matricolaStudente, false);
                    JOptionPane.showMessageDialog(this, "Elaborato finale rifiutato!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ERRORE_DB_PREFIX + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        chkTirocinioEsterno.addActionListener(e -> {
            boolean isEsterno = chkTirocinioEsterno.isSelected();
            txtAzienda.setEnabled(isEsterno);
            txtReferenteAziendale.setEnabled(isEsterno);

            // Svuota i campi se togliamo la spunta
            if (!isEsterno) {
                txtAzienda.setText("");
                txtReferenteAziendale.setText("");
            }

        });
        btnHome.addActionListener(e -> {
            // Riapre la schermata di login passando il controller
            new LoginFrame(controller).setVisible(true);
            // Chiude la schermata attuale
            dispose();
        });
        // Stato di default all'apertura: campi disabilitati
        txtAzienda.setEnabled(false);
        txtReferenteAziendale.setEnabled(false);
        aggiornaTabellaRichieste();
    }
    private void aggiornaTabellaRichieste() {
        try {
            String[] colonne = {"Matricola", "Studente", "ID Tirocinio", "Argomento", "Stato"};
            javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(colonne, 0);
            for (String[] riga : controller.getRichiestePerDocente()) {
                model.addRow(riga);
            }
            if (tblRichiesteStudenti != null) tblRichiesteStudenti.setModel(model);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}