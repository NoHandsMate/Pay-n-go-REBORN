package boundary;

import javax.swing.*;
import java.awt.*;
import java.util.AbstractMap;

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
        loginButton.addActionListener(actionEvent -> {

            AbstractMap.SimpleEntry<Boolean, String> result = validateInput();

            if (Boolean.FALSE.equals(result.getKey())) {
                JOptionPane.showMessageDialog(loginPanel, result.getValue(), "Error", JOptionPane.ERROR_MESSAGE);
            } else {

                /*TODO: Chiamata al loginUtente del Controller*/


                MainWindow mainWindow = new MainWindow();
                mainWindow.setVisible(true);
                setVisible(false);
            }


        });
    }

    private AbstractMap.SimpleEntry<Boolean, String> validateInput() {

        if (emailField.getText().isBlank()) {
            return new AbstractMap.SimpleEntry<>(false, "Il campo email non può essere vuoto");
        }

        if (emailField.getText().indexOf('@') < 0) {
            return new AbstractMap.SimpleEntry<>(false, "La email è invalida");
        }

        boolean empty = checkEmptyPassword();

        if (empty)
            return new AbstractMap.SimpleEntry<>(false, "Il campo password non può essere vuoto");

        return new AbstractMap.SimpleEntry<>(true, "OK");
    }

    private boolean checkEmptyPassword() {

        boolean empty = true;
        for(char c : passwordField.getPassword()) {
            if (c != 0) {
                empty = false;
            }
            if (c == 0) {
                empty = true;
            }
        }

        return empty;
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
