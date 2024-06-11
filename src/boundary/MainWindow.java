package boundary;

import javax.swing.*;
import java.awt.*;
import dto.*;

public class MainWindow extends JFrame {
    private JPanel mainWindowPanel;
    private JLabel welcomeLabel;
    private JButton visualizzaPrenotazioniButton;
    private JButton esciButton;
    private JButton prenotaViaggioButton;
    private JButton condividiViaggioButton;

    public MainWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pay-n-go REBORN");
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().add(new JScrollPane(mainWindowPanel), BorderLayout.CENTER);
        welcomeLabel.setText(String.format("Ciao %s %s!",
                UtenteCorrente.getInstance().getNome(), UtenteCorrente.getInstance().getCognome()));
    }
}
