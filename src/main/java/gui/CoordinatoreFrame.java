package gui;

import controller.Controller;
import model.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class CoordinatoreFrame extends JFrame {
    private Controller controller;

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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Azione 1: Inserimento nuova Seduta di Laurea ufficiale
        btnCreaSeduta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ora = txtOraSeduta.getText();
                String luogo = txtLuogoSeduta.getText();
                String codice = txtCodiceSeduta.getText();

                controller.coordinatoreInserisciSeduta(new Date(), ora, luogo, codice);
                JOptionPane.showMessageDialog(CoordinatoreFrame.this, "Seduta inserita nel calendario accademico!");
            }
        });

        // Azione 2: Composizione dei docenti membri della commissione
        btnComponiCommissione.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Docente mockDocente = new Docente("prof_verdi", "123", "verdi@unina.it", "Luigi", "Verdi", "SSN123");
                Seduta_di_laurea mockSeduta = new Seduta_di_laurea(new Date(), "09:00", "Aula Magna", "SED-ABC");

                controller.coordinatoreAggiungiDocenteACommissione(mockDocente, mockSeduta);
                JOptionPane.showMessageDialog(CoordinatoreFrame.this, "Docente aggiunto ai membri della commissione!");
            }
        });
    }
}