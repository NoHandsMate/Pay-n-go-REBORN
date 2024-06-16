package boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;

/**
 * Classe del package boundary nel modello BCED, essa implementa la HomePage, landing point dell'applicazione.
 */
public class HomePage extends JFrame {
    private JButton registrazioneButton;
    private JButton loginButton;
    private JPanel homepagePanel;
    private JPanel logoPanel;

    public HomePage() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pay n' Go REBORN - Homepage");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 16));
        getContentPane().add(new JScrollPane(homepagePanel), BorderLayout.CENTER);
        registrazioneButton.addActionListener(actionEvent -> {
            FormRegistrazione formRegistrazione = new FormRegistrazione();
            formRegistrazione.setVisible(true);
            formRegistrazione.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    setVisible(true);
                }
            });
            setVisible(false);
            }
        );
        loginButton.addActionListener(actionEvent -> {
            FormLogin formLogin = new FormLogin();
            formLogin.setVisible(true);
            formLogin.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    setVisible(true);
                }
            });
            setVisible(false);
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
        HomePage homePage = new HomePage();
        homePage.setVisible(true);
    }

    private void createUIComponents() {
        logoPanel = new ImagePanel("./resources/payngo-reborn-logo.png", 2437, 1112);
    }
}
