package gui;

import controller.Controller;
import model.*;
import javax.swing.*;
import java.util.Date;

public class CoordinatoreFrame extends JFrame {

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

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        btnCreaSeduta.addActionListener(e -> {
            String ora = txtOraSeduta.getText();
            String luogo = txtLuogoSeduta.getText();
            String codice = txtCodiceSeduta.getText();

            controller.coordinatoreInserisciSeduta(new Date(), ora, luogo, codice);
            JOptionPane.showMessageDialog(CoordinatoreFrame.this, "Seduta inserita nel calendario accademico!");
        });

        btnComponiCommissione.addActionListener(e -> {
            // MODIFICA: Rimosse le istanziazioni degli oggetti del Model 'Docente' e 'SedutaDiLaurea'.
            // Ora passiamo al controller esclusivamente i dati identificativi sotto forma di Stringhe (SSN e Codice).
            String ssnDocente = "SSN123";
            String codiceSeduta = "SED-ABC";

            controller.coordinatoreAggiungiDocenteACommissione(ssnDocente, codiceSeduta);
            JOptionPane.showMessageDialog(CoordinatoreFrame.this, "Docente aggiunto ai membri della commissione!");
        });
    }
}