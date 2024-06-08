package boundary;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class HomePage extends JFrame {
    private JButton registrazioneButton;
    private JButton loginButton;
    private JPanel homepagePanel;
    private JPanel logoPanel;

    private Logger logger = Logger.getLogger("loggerHomePage");

    public HomePage() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pay n' Go REBORN - Homepage");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setResizable(true);
        getContentPane().add(new JScrollPane(homepagePanel), BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            java.util.logging.Logger.getLogger("loggerFormLogin").log(java.util.logging.Level.SEVERE, e.getMessage());
        }
        HomePage homePage = new HomePage();
        homePage.setVisible(true);
    }

    private void createUIComponents() {
        logoPanel = new HomePageLogoPanel();
    }
}
