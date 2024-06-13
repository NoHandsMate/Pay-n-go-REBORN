package boundary;

import control.ControllerUtente;
import dto.UtenteCorrente;

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
        getContentPane().add(new JScrollPane(loginPanel), BorderLayout.CENTER);
        getRootPane().setDefaultButton(loginButton);
        loginButton.addActionListener(actionEvent -> {

            AbstractMap.SimpleEntry<Boolean, String> result = validateInput();

            if (Boolean.FALSE.equals(result.getKey())) {
                JOptionPane.showMessageDialog(loginPanel, result.getValue(), "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                result = ControllerUtente.getInstance().loginUtente(emailField.getText(), passwordField.getPassword());

                if (Boolean.FALSE.equals(result.getKey())) {
                    JOptionPane.showMessageDialog(rootPane, result.getValue(), "Errore", JOptionPane.ERROR_MESSAGE);
                } else {
                    if (UtenteCorrente.getInstance().getIdUtenteCorrente() == 0)
                    {
                        JOptionPane.showMessageDialog(rootPane, "Il login è stato effettuato, ma l'id della " +
                                "sessione è invalido. Per favore riprova ad effettuare il login.", "Errore",
                                JOptionPane.ERROR_MESSAGE);
                    } else if (UtenteCorrente.getInstance().getIdUtenteCorrente() == 1) {
                        FormGestore formGestore = new FormGestore();
                        formGestore.setVisible(true);
                        setVisible(false);
                    } else {
                        MainWindow mainWindow = new MainWindow();
                        mainWindow.setVisible(true);
                        setVisible(false);
                    }
                }
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
}
