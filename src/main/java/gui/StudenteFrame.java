package gui;

import controller.Controller;
import model.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class StudenteFrame extends JFrame {
    private Controller controller;

    // Componenti da associare nel file .form
    private JPanel panel1;
    private JTextField txtTitoloTesi;
    private JTextField txtPercorsoFile;
    private JButton btnCaricaTesi;
    private JButton btnRichiediTirocinio;
    private JButton btnPrenotaSeduta;

    public StudenteFrame(Controller controller) {
        this.controller = controller;
        setContentPane(panel1);
        setTitle("Plancia Studente");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Azione 1: Richiesta di Tirocinio (Usa oggetti fittizi per invocare il Model)
        btnRichiediTirocinio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Docente mockDocente = new Docente("prof_verdi", "123", "verdi@unina.it", "Luigi", "Verdi", "SSN123");
                Tirocinio mockTirocinio = new Tirocinio(101, "Sviluppo Interfacce Grafiche Java");

                controller.studenteRichiediTirocinio(mockDocente, mockTirocinio);
                JOptionPane.showMessageDialog(StudenteFrame.this, "Richiesta di tirocinio inviata al docente!");
            }
        });

        // Azione 2: Caricamento Elaborato Tesi
        btnCaricaTesi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String titolo = txtTitoloTesi.getText();
                String percorso = txtPercorsoFile.getText();

                controller.studenteCaricaTesi(titolo, percorso);
                JOptionPane.showMessageDialog(StudenteFrame.this, "Tesi caricata con successo nel sistema!");
            }
        });

        // Azione 3: Prenotazione Appello di Laurea
        btnPrenotaSeduta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Studente s = (Studente) controller.getUtenteLoggato();
                Tesi mockTesi = new Tesi("Sviluppo Software", "C:/tesi.pdf", s);
                Seduta_di_laurea mockSeduta = new Seduta_di_laurea(new Date(), "09:00", "Aula Magna", "SED-2026");

                controller.studentePrenotaSeduta(mockTesi, mockSeduta);
                JOptionPane.showMessageDialog(StudenteFrame.this, "Prenotazione alla seduta effettuata!");
            }
        });
    }
}