package boundary;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.util.Enumeration;

import control.ControllerUtente;
import dto.*;

public class MainWindow extends JFrame {
    private JPanel mainWindowPanel;
    private JLabel welcomeLabel;
    private JTabbedPane contentTab;
    private JPanel circularLogoPanel;
    private JButton accountTabButton;
    private JButton homeTabButton;
    private JButton prenotazioniTabButton;
    private JTable table1;
    private JButton gestisciPrenotazioneButton;

    public MainWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pay-n-go REBORN");
        setSize(1000, 800);
        setMinimumSize(new Dimension(1000, 800));
        setLocationRelativeTo(null);
        setResizable(true);
        getContentPane().add(new JScrollPane(mainWindowPanel), BorderLayout.CENTER);
        welcomeLabel.setText(String.format("Ciao %s %s!",
                UtenteCorrente.getInstance().getNome(), UtenteCorrente.getInstance().getCognome()));

        contentTab.setUI(new BasicTabbedPaneUI() {
            @Override
            protected int calculateTabAreaHeight(int tab_placement, int run_count, int max_tab_height) {
                    return -1;
            }
        });
        homeTabButton.addActionListener(actionEvent -> {
            contentTab.setSelectedIndex(0);
            homeTabButton.setBackground(new Color(15, 53, 156));
            homeTabButton.setForeground(Color.WHITE);
            accountTabButton.setBackground(new Color(240, 155, 50));
            accountTabButton.setForeground(Color.BLACK);
            prenotazioniTabButton.setBackground(new Color(240, 155, 50));
            prenotazioniTabButton.setForeground(Color.BLACK);
        });
        accountTabButton.addActionListener(actionEvent -> {
            contentTab.setSelectedIndex(1);
            homeTabButton.setBackground(new Color(240, 155, 50));
            homeTabButton.setForeground(Color.BLACK);
            accountTabButton.setBackground(new Color(15, 53, 156));
            accountTabButton.setForeground(Color.WHITE);
            prenotazioniTabButton.setBackground(new Color(240, 155, 50));
            prenotazioniTabButton.setForeground(Color.BLACK);
        });
        prenotazioniTabButton.addActionListener(actionListener -> {
            contentTab.setSelectedIndex(2);
            homeTabButton.setBackground(new Color(240, 155, 50));
            homeTabButton.setForeground(Color.BLACK);
            accountTabButton.setBackground(new Color(240, 155, 50));
            accountTabButton.setForeground(Color.BLACK);
            prenotazioniTabButton.setBackground(new Color(15, 53, 156));
            prenotazioniTabButton.setForeground(Color.WHITE);
        });
        contentTab.addChangeListener(e -> {
            if (contentTab.getSelectedIndex() == 0) {

            } else if (contentTab.getSelectedIndex() == 1) {

            } else if (contentTab.getSelectedIndex() == 2) {
                /* TODO qualcosa del genere */
                // ControllerUtente.getInstance().visualizzaPrenotazioni
            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            Font f = new javax.swing.plaf.FontUIResource("Lucida Sans Typewriter", Font.PLAIN,16);
            Enumeration<Object> keys = UIManager.getDefaults().keys();
            while (keys.hasMoreElements()) {
                Object key = keys.nextElement();
                Object value = UIManager.get (key);
                if (value instanceof javax.swing.plaf.FontUIResource)
                    UIManager.put (key, f);
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            java.util.logging.Logger.getLogger("loggerFormLogin").log(java.util.logging.Level.SEVERE, e.getMessage());
        }
        MainWindow window = new MainWindow();
        window.setVisible(true);
    }

    private void createUIComponents() {
        circularLogoPanel = new MainWindowCircularLogoPanel();
    }
}
