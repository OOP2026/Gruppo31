package gui;

import controller.Controller;
import model.Coordinatore;
import model.Docente;
import model.Studente;
import javax.swing.*;

/**
 * Interfaccia grafica (View) per l'accesso al sistema.
 * Funge da "porta d'ingresso" dell'applicazione: raccoglie le credenziali,
 * richiede la validazione al Controller e, in caso di successo, si occupa del "routing",
 * ovvero di reindirizzare l'utente verso la dashboard (Frame) corretta in base al suo ruolo.
 */
public class LoginFrame extends JFrame {

    // Il controller fa da tramite tra questa GUI e la logica di business (Model + DB)
    private transient Controller controller;

    private JPanel panel1;
    private JTextField txtUsername;
    private JPasswordField txtPassword; // Utilizzato per oscurare i caratteri digitati per sicurezza
    private JButton btnLogin;
    private JLabel lblErrore;

    /**
     * Costruttore della schermata di Login.
     * Inizializza i componenti visivi e imposta l'azione da compiere alla pressione
     * del pulsante di accesso.
     *
     * @param controller l'istanza del controller centrale
     */
    public LoginFrame(Controller controller) {
        this.controller = controller;
        setContentPane(panel1);
        setTitle("Sistema Gestione Lauree - Login");
        setSize(350, 200);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la finestra al centro dello schermo
        lblErrore.setText(""); // Pulisce eventuali messaggi di errore al primo avvio

        // =================================================================
        // AZIONE DI LOGIN E ROUTING
        // =================================================================
        btnLogin.addActionListener(e -> {
            // Estrazione sicura delle credenziali
            String username = txtUsername.getText();
            // JPasswordField.getPassword() restituisce un array di char, lo convertiamo in String
            String password = new String(txtPassword.getPassword());

            try {
                // Deleghiamo al controller il controllo sul database
                boolean loginSuccesso = controller.effettuaLogin(username, password);

                if (loginSuccesso) {
                    lblErrore.setText("");

                    // ROUTING BASATO SUL POLIMORFISMO
                    // Chiediamo al controller chi si è appena loggato e, a seconda
                    // della classe istanziata, apriamo la finestra dedicata.
                    if (controller.getUtenteLoggato() instanceof Coordinatore) {
                        new CoordinatoreFrame(controller).setVisible(true);
                    } else if (controller.getUtenteLoggato() instanceof Docente) {
                        new DocenteFrame(controller).setVisible(true);
                    } else if (controller.getUtenteLoggato() instanceof Studente) {
                        new StudenteFrame(controller).setVisible(true);
                    }

                    // Chiudiamo definitivamente la finestra di login dopo il reindirizzamento
                    dispose();

                } else {
                    // Feedback visivo immediato in caso di credenziali errate
                    lblErrore.setText("Username o Password errati!");
                    lblErrore.setForeground(java.awt.Color.RED);
                }
            } catch (Exception ex) {
                // Gestione degli errori critici (es. Database non raggiungibile, password DB errata, ecc.)
                JOptionPane.showMessageDialog(LoginFrame.this,
                        "Errore di connessione al Database:\n" + ex.getMessage(),
                        "Errore di Sistema", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}