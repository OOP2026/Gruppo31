package gui;

import controller.Controller;
import model.*;
import javax.swing.*;
import java.util.Date;

public class DocenteFrame extends JFrame {

    // Aggiunto "transient" per la serializzazione
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

        // Sostituito JFrame con WindowConstants
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Azione 1: Proposta di un nuovo Tirocinio (Convertita in Lambda)
        btnAggiungiTirocinio.addActionListener(e -> {
            int id = Integer.parseInt(txtIdTirocinio.getText());
            String argomento = txtArgomentoTirocinio.getText();

            controller.docenteAggiungiTirocinio(id, argomento);
            JOptionPane.showMessageDialog(DocenteFrame.this, "Nuovo argomento di tirocinio aggiunto!");
        });

        // Azione 2: Valutazione di una richiesta di tirocinio ricevevuta (Convertita in Lambda)
        btnApprovaRichiesta.addActionListener(e -> {
            Studente mockStudente = new Studente("mario99", "123", "mario@stud.it", "Mario", "Rossi", "M001");
            Tirocinio t = new Tirocinio(1, "Studio Algoritmi");
            RichiestaTirocinio richiesta = new RichiestaTirocinio(new Date(), mockStudente, (Docente)controller.getUtenteLoggato(), t);

            controller.docenteValutaRichiesta(richiesta, true); // true = Approva
            JOptionPane.showMessageDialog(DocenteFrame.this, "Richiesta dello studente approvata!");
        });

        // Azione 3: Approvazione elaborato tesi (Convertita in Lambda)
        btnApprovaTesi.addActionListener(e -> {
            Studente mockStudente = new Studente("mario99", "123", "mario@stud.it", "Mario", "Rossi", "M001");
            Tesi tesi = new Tesi("Studio Smart Contract", "file://tesi.pdf", mockStudente);

            controller.docenteValutaTesi(tesi, true);
            JOptionPane.showMessageDialog(DocenteFrame.this, "Elaborato finale approvato con successo!");
        });
    }
}