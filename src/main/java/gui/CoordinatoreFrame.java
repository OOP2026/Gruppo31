package gui;

import controller.Controller;
import javax.swing.*;
import java.util.Date;

/**
 * Interfaccia grafica (View) per l'amministrazione, dedicata al Coordinatore.
 * Permette la gestione organizzativa e logistica delle lauree:
 * la pianificazione di nuove sedute e l'assegnazione dei docenti alle commissioni d'esame.
 */
public class CoordinatoreFrame extends JFrame {

    private transient Controller controller;
    private JPanel panel1;

    // Componenti per la creazione di una nuova seduta
    private JTextField txtOraSeduta;
    private JTextField txtLuogoSeduta;
    private JTextField txtCodiceSeduta;
    private JButton btnCreaSeduta;

    // Componenti per la composizione della commissione
    private JTextField txtSsnDocenteCommissione;
    private JTextField txtCodiceSedutaCommissione;
    private JButton btnComponiCommissione;

    /**
     * Costruttore della plancia del Coordinatore.
     * Inizializza i componenti visivi e mappa le azioni amministrative sui rispettivi bottoni.
     *
     * @param controller l'istanza del controller centrale
     */
    public CoordinatoreFrame(Controller controller) {
        this.controller = controller;
        setContentPane(panel1);
        setTitle("Amministrazione - Coordinatore");
        setSize(500, 450);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la finestra

        // =================================================================
        // AZIONE 1: CREAZIONE DI UNA NUOVA SEDUTA
        // =================================================================
        btnCreaSeduta.addActionListener(e -> {
            try {
                String ora = txtOraSeduta.getText();
                String luogo = txtLuogoSeduta.getText();
                String codice = txtCodiceSeduta.getText();

                // Sicurezza: se manca anche un solo dato, interrompiamo subito l'operazione
                if (ora.isEmpty() || luogo.isEmpty() || codice.isEmpty()) return;

                // Delega l'operazione al Controller.
                // Nota: Per la demo si utilizza new Date() che imposta la data odierna.
                controller.coordinatoreInserisciSeduta(new Date(), ora, luogo, codice);
                JOptionPane.showMessageDialog(CoordinatoreFrame.this, "Seduta inserita nel calendario accademico!");

            } catch (Exception ex) {
                // Cattura eventuali errori (es. Codice seduta già esistente nel DB)
                JOptionPane.showMessageDialog(CoordinatoreFrame.this, "Errore DB: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        // =================================================================
        // AZIONE 2: AGGIUNTA DI UN DOCENTE ALLA COMMISSIONE
        // =================================================================
        btnComponiCommissione.addActionListener(e -> {
            try {
                String ssnDocente = txtSsnDocenteCommissione.getText();
                String codiceSeduta = txtCodiceSedutaCommissione.getText();

                // Controllo sui campi obbligatori
                if (ssnDocente.isEmpty() || codiceSeduta.isEmpty()) return;

                // Chiama il Controller per creare la riga di congiunzione nel Database
                controller.coordinatoreAggiungiDocenteACommissione(ssnDocente, codiceSeduta);
                JOptionPane.showMessageDialog(CoordinatoreFrame.this, "Docente aggiunto alla commissione!");

            } catch (Exception ex) {
                // Cattura eventuali errori (es. SSN non trovato, o Seduta inesistente)
                JOptionPane.showMessageDialog(CoordinatoreFrame.this, "Errore DB: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}