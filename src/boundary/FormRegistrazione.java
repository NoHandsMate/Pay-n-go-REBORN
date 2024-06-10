package boundary;

import javax.swing.*;
import java.awt.*;
import java.util.AbstractMap;
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

          if (!result.getKey()) {
              JOptionPane.showMessageDialog(rootPane, result.getValue(),
                                       "Errore", JOptionPane.ERROR_MESSAGE);
          } else {
              /*TODO: Chiama il metodo registraUtente del controller*/


              result = ControllerUtente.getInstance().registraUtente(nomeField.getText(), cognomeField.getText(),
                      emailField.getText(), autoField.getText(),
                      passwordField.getPassword(), (Integer) postiSpinner.getValue(),
                      telefonoField.getText());

              if (!result.getKey()) {
                  JOptionPane.showMessageDialog(rootPane, result.getValue(), "Errore", JOptionPane.ERROR_MESSAGE);
              } else {
                  JOptionPane.showMessageDialog(rootPane, result.getValue(), "OK", JOptionPane.INFORMATION_MESSAGE);
              }

          }

        });
    }

    private AbstractMap.SimpleEntry<Boolean, String> validateInput() {
        if (nomeField.getText().isBlank() || cognomeField.getText().isBlank() ||
            emailField.getText().isBlank() || passwordField.getPassword().length == 0 ||
            telefonoField.getText().isBlank()) {
            return new AbstractMap.SimpleEntry<>(false, "Riempi i campi obbligatori!");
        }

        if (!autoField.getText().isBlank() && (Integer) postiSpinner.getValue() == 0 ||
            (autoField.getText().isBlank() && (Integer) postiSpinner.getValue() != 0)) {
            return new AbstractMap.SimpleEntry<>(false, "Il valore dei posti disponibili è incorretto");
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
