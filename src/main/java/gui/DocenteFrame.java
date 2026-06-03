package gui;

import controller.Controller;
import model.*;
import javax.swing.*;
import java.util.Date;

public class DocenteFrame extends JFrame {

    private transient Controller controller;

    private JPanel panel1;
    private JTextField txtIdTirocinio;
    private JTextField txtArgomentoTirocinio;
    private JButton btnAggiungiTirocinio;
    private JButton btnApprovaRichiesta;
    private JButton btnApprovaTesi;

    public DocenteFrame(Controller controller) {
        this.controller = controller;
        setContentPane(panel1);
        setTitle("GUI Docente / Relatore");
        setSize(450, 350);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        btnAggiungiTirocinio.addActionListener(e -> {
            int id = Integer.parseInt(txtIdTirocinio.getText());
            String argomento = txtArgomentoTirocinio.getText();

            controller.docenteAggiungiTirocinio(id, argomento);
            JOptionPane.showMessageDialog(DocenteFrame.this, "Nuovo argomento di tirocinio aggiunto!");
        });

        btnApprovaRichiesta.addActionListener(e -> {
            // MODIFICA: Rimossa la creazione fittizia di 'Studente', 'Tirocinio' e 'RichiestaTirocinio'.
            // La GUI invia al controller solo la matricola dello studente associato alla richiesta.
            String matricolaStudente = "M001";

            controller.docenteValutaRichiesta(matricolaStudente, true);
            JOptionPane.showMessageDialog(DocenteFrame.this, "Richiesta dello studente approvata!");
        });

        btnApprovaTesi.addActionListener(e -> {
            // MODIFICA: Rimossa l'interazione diretta con il Model ('new Studente' e 'new Tesi').
            // Passiamo al controller solo la stringa della matricola dello studente di cui valutare la tesi.
            String matricolaStudente = "M001";

            controller.docenteValutaTesi(matricolaStudente, true);
            JOptionPane.showMessageDialog(DocenteFrame.this, "Elaborato finale approvato con successo!");
        });
    }
}