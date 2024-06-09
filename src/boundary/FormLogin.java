package boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormLogin extends JFrame {
    private JPanel loginPanel;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public FormLogin() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Login");
        setSize(600, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new JScrollPane(loginPanel), BorderLayout.CENTER);
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MainWindow mainWindow = new MainWindow();
                mainWindow.setVisible(true);
                setVisible(false);
            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            java.util.logging.Logger.getLogger("loggerFormLogin").log(java.util.logging.Level.SEVERE, e.getMessage());
        }
        FormLogin frame = new FormLogin();
        frame.setVisible(true);
    }
}
