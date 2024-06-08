package boundary;

import javax.swing.*;
import java.awt.*;

public class FormLogin extends JFrame {
    private JPanel loginPanel;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton backButton;

    public FormLogin() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login");
        //setSize(800, 800);
        //getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new JScrollPane(loginPanel), BorderLayout.CENTER);
    }

    private void createUIComponents() {
        loginPanel = new JPanel();
        loginPanel.setBorder(BorderFactory.createTitledBorder("Login"));
        loginPanel.setLayout(null);
        loginPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        loginPanel.add(emailField);
        loginPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        loginPanel.add(passwordField);
        loginButton = new JButton("Login");
        loginPanel.add(loginButton);
    }

    public static void main(String[] args) {
        FormLogin frame = new FormLogin();
        frame.setVisible(true);
    }
}
