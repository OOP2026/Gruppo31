package gui;

import controller.Controller;
import javax.swing.*;
import java.util.Date;

public class CoordinatoreFrame extends JFrame {
    private transient Controller controller;
    private JPanel panel1;
    private JTextField txtOraSeduta;
    private JTextField txtLuogoSeduta;
    private JTextField txtCodiceSeduta;
    private JButton btnCreaSeduta;
    private JTextField txtSsnDocenteCommissione;
    private JTextField txtCodiceSedutaCommissione;
    private JButton btnComponiCommissione;

    public CoordinatoreFrame(Controller controller) {
        this.controller = controller;
        setContentPane(panel1);
        setTitle("Amministrazione - Coordinatore");
        setSize(500, 450);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        btnCreaSeduta.addActionListener(e -> {
            try {
                String ora = txtOraSeduta.getText();
                String luogo = txtLuogoSeduta.getText();
                String codice = txtCodiceSeduta.getText();
                if (ora.isEmpty() || luogo.isEmpty() || codice.isEmpty()) return;

                controller.coordinatoreInserisciSeduta(new Date(), ora, luogo, codice);
                JOptionPane.showMessageDialog(CoordinatoreFrame.this, "Seduta inserita nel calendario accademico!");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(CoordinatoreFrame.this, "Errore DB: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnComponiCommissione.addActionListener(e -> {
            try {
                String ssnDocente = txtSsnDocenteCommissione.getText();
                String codiceSeduta = txtCodiceSedutaCommissione.getText();
                if (ssnDocente.isEmpty() || codiceSeduta.isEmpty()) return;

                controller.coordinatoreAggiungiDocenteACommissione(ssnDocente, codiceSeduta);
                JOptionPane.showMessageDialog(CoordinatoreFrame.this, "Docente aggiunto alla commissione!");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(CoordinatoreFrame.this, "Errore DB: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}