package boundary;

import javax.swing.*;
import java.awt.*;
import java.util.AbstractMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import control.ControllerUtente;


public class FormRegistrazione extends JFrame {
    private JTextField nomeField;
    private JTextField cognomeField;
    private JTextField emailField;
    private JTextField autoField;
    private JPasswordField passwordField;
    private JSpinner postiSpinner;
    private JButton registratiButton;
    private JTextField telefonoField;
    private JPanel registrazionePanel;

    public FormRegistrazione() {

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Registrazione");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().add(new JScrollPane(registrazionePanel), BorderLayout.CENTER);
        registratiButton.addActionListener(actionEvent -> {
          AbstractMap.SimpleEntry<Boolean, String> result = validateInput();

          if (Boolean.FALSE.equals(result.getKey())) {
              JOptionPane.showMessageDialog(rootPane, result.getValue(),
                                       "Errore", JOptionPane.ERROR_MESSAGE);
          } else {
              result = ControllerUtente.getInstance().registraUtente(nomeField.getText(), cognomeField.getText(),
                      emailField.getText(), autoField.getText(),
                      passwordField.getPassword(), (Integer) postiSpinner.getValue(),
                      telefonoField.getText());

              if (Boolean.FALSE.equals(result.getKey())) {
                  JOptionPane.showMessageDialog(rootPane, result.getValue(), "Errore", JOptionPane.ERROR_MESSAGE);
              } else {
                  JOptionPane.showMessageDialog(rootPane, result.getValue(), "OK", JOptionPane.INFORMATION_MESSAGE);
                  MainWindow mainWindow = new MainWindow();
                  mainWindow.setVisible(true);
                  setVisible(false);
              }

          }

        });
    }

    private AbstractMap.SimpleEntry<Boolean, String> validateInput() {
        if (nomeField.getText().isBlank() || cognomeField.getText().isBlank() ||
            emailField.getText().isBlank() || passwordField.getPassword().length == 0 ||
            telefonoField.getText().isBlank()) {
            return new AbstractMap.SimpleEntry<>(false, "Riempi i campi obbligatori");
        }

        if (!autoField.getText().isBlank() && (Integer) postiSpinner.getValue() == 0 ||
            (autoField.getText().isBlank() && (Integer) postiSpinner.getValue() != 0)) {
            return new AbstractMap.SimpleEntry<>(false, "Il valore dei posti disponibili è incorretto");
        }

        Pattern specialCharRegex = Pattern.compile("[^a-zA-Z]", Pattern.CASE_INSENSITIVE);
        Pattern numberRegex = Pattern.compile("[0-9]", Pattern.CASE_INSENSITIVE);
        Pattern telRegex = Pattern.compile("[^+0-9]", Pattern.CASE_INSENSITIVE);
        Matcher nomeMatcher = specialCharRegex.matcher(nomeField.getText());
        Matcher nomeMatcherNumber = numberRegex.matcher(nomeField.getText());
        Matcher cognomeMatcher = specialCharRegex.matcher(cognomeField.getText());
        Matcher cognomeMatcherNumber = numberRegex.matcher(cognomeField.getText());
        Matcher telefonoMatcher = telRegex.matcher(telefonoField.getText());


        if (nomeField.getText().length() > 50) {
            return new AbstractMap.SimpleEntry<>(false, "Il nome supera la lunghezza massima di 50 caratteri");
        }

        if (nomeMatcher.find() || nomeMatcherNumber.find()) {
            return new AbstractMap.SimpleEntry<>(false, "Il nome contiene caratteri non validi");
        }

        if (cognomeField.getText().length() > 50) {
            return new AbstractMap.SimpleEntry<>(false, "Il cognome supera la lunghezza massima di 50 caratteri");
        }

        if (cognomeMatcher.find() || cognomeMatcherNumber.find()) {
            return new AbstractMap.SimpleEntry<>(false, "Il cognome contiene caratteri non validi");
        }

        if (emailField.getText().length() > 50) {
            return new AbstractMap.SimpleEntry<>(false, "L'email supera la lunghezza massima di 50 caratteri");
        }

        if (emailField.getText().indexOf('@') == -1) {
           return new AbstractMap.SimpleEntry<>(false, "L'email non è valida");
        }

        if (telefonoField.getText().length() > 15) {
            return new AbstractMap.SimpleEntry<>(false, "Il contatto telefonico supera la lunghezza massima di 15 caratteri");
        }

        if (telefonoMatcher.find()) {
            return new AbstractMap.SimpleEntry<>(false, "Il contatto telefonico contiene caratteri non validi");
        }

        if (passwordField.getPassword().length > 50 || passwordField.getPassword().length < 4) {
            return new AbstractMap.SimpleEntry<>(false, "La password deve essere compresa tra 4 e 50 caratteri");
        }

        if (autoField.getText().length() > 50) {
            return new AbstractMap.SimpleEntry<>(false, "Il modello dell'automobile supera la lunghezza massima di 50 caratteri");
        }
        return new AbstractMap.SimpleEntry<>(true, "OK");
    }


    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
               FormRegistrazione window = new FormRegistrazione();
                window.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
