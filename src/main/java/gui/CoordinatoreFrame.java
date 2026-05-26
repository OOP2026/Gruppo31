package gui;

import controller.Controller;
import model.*;
import javax.swing.*;
import java.util.Date;

public class CoordinatoreFrame extends JFrame {

    // Aggiunto "transient" per far felice SonarLint sulla serializzazione
    private transient Controller controller;

    private JPanel panel1;
    private JTextField txtOraSeduta;
    private JTextField txtLuogoSeduta;
    private JTextField txtCodiceSeduta;
    private JButton btnCreaSeduta;
    private JButton btnComponiCommissione;

    public CoordinatoreFrame(Controller controller) {
        this.controller = controller;
        setContentPane(panel1);
        setTitle("Amministrazione - Coordinatore");
        setSize(450, 350);

        // Sostituito JFrame con WindowConstants come suggerito
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Azione 1: Convertita in Lambda Expression (molto più pulita!)
        btnCreaSeduta.addActionListener(e -> {
            String ora = txtOraSeduta.getText();
            String luogo = txtLuogoSeduta.getText();
            String codice = txtCodiceSeduta.getText();

            controller.coordinatoreInserisciSeduta(new Date(), ora, luogo, codice);
            JOptionPane.showMessageDialog(CoordinatoreFrame.this, "Seduta inserita nel calendario accademico!");
        });

        // Azione 2: Convertita in Lambda Expression
        btnComponiCommissione.addActionListener(e -> {
            Docente mockDocente = new Docente("prof_verdi", "123", "verdi@unina.it", "Luigi", "Verdi", "SSN123");
            Seduta_di_laurea mockSeduta = new Seduta_di_laurea(new Date(), "09:00", "Aula Magna", "SED-ABC");

            controller.coordinatoreAggiungiDocenteACommissione(mockDocente, mockSeduta);
            JOptionPane.showMessageDialog(CoordinatoreFrame.this, "Docente aggiunto ai membri della commissione!");
        });
    }
}